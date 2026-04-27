package org.example.savingsservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.savingsservice.kafka.command.ReleaseAmountCommand;
import org.example.savingsservice.kafka.command.ReserveAmountCommand;
import org.example.savingsservice.kafka.command.TransferMoneyCommand;
import org.example.savingsservice.kafka.event.AmountReleasedEvent;
import org.example.savingsservice.kafka.event.AmountReservedEvent;
import org.example.savingsservice.kafka.event.MoneyTransferredEvent;
import org.example.savingsservice.service.SavingsAccountsService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SavingsAccountsKafkaListeners {

    private final SavingsAccountsService savingsAccountsService;
    private final KafkaSenderService kafkaSenderService;
    private final ObjectMapper objectMapper;

    public SavingsAccountsKafkaListeners(
            SavingsAccountsService savingsAccountsService,
            KafkaSenderService kafkaSenderService,
            ObjectMapper objectMapper
    ) {
        this.savingsAccountsService = savingsAccountsService;
        this.kafkaSenderService = kafkaSenderService;
        this.objectMapper = objectMapper;
    }

    // =========================
    // 💰 RESERVE MONEY
    // =========================
    @KafkaListener(topics = "amount.reserve", groupId = "savings-group")
    public void onReserveAmount(byte[] message) {
        try {
            ReserveAmountCommand cmd = objectMapper.readValue(message, ReserveAmountCommand.class);

            savingsAccountsService.reserveAmount(
                    cmd.getUserId(),
                    cmd.getCurrencyId(),
                    cmd.getAmount(),
                    cmd.getOperationId()
            );

            kafkaSenderService.send(
                    "amount.reserved",
                    cmd.getSagaId(),
                    new AmountReservedEvent(
                            cmd.getSagaId(),
                            cmd.getOperationId(),
                            true,
                            null
                    )
            );

        } catch (Exception e) {
            // В случае ошибки тоже нужно отправить событие с флагом success=false
            try {
                // Пытаемся извлечь sagaId и operationId, чтобы отправить ответ
                ReserveAmountCommand cmd = objectMapper.readValue(message, ReserveAmountCommand.class);
                kafkaSenderService.send(
                        "amount.reserved",
                        cmd.getSagaId(),
                        new AmountReservedEvent(
                                cmd.getSagaId(),
                                cmd.getOperationId(),
                                false,
                                e.getMessage()
                        )
                );
            } catch (Exception ex) {
                // Если совсем не можем разобрать сообщение, логируем
                // log.error("Failed to process amount.reserve", ex);
            }
        }
    }

    // =========================
    // 💰 RELEASE MONEY
    // =========================
    @KafkaListener(topics = "amount.release", groupId = "savings-group")
    public void onReleaseAmount(byte[] message) {
        try {
            ReleaseAmountCommand cmd = objectMapper.readValue(message, ReleaseAmountCommand.class);

            savingsAccountsService.releaseAmount(
                    cmd.getUserId(),
                    cmd.getCurrencyId(),
                    cmd.getAmount(),
                    cmd.getOperationId()
            );

            kafkaSenderService.send(
                    "amount.released",
                    cmd.getSagaId(),
                    new AmountReleasedEvent(
                            cmd.getSagaId(),
                            cmd.getOperationId(),
                            true,
                            null
                    )
            );

        } catch (Exception e) {
            try {
                ReleaseAmountCommand cmd = objectMapper.readValue(message, ReleaseAmountCommand.class);
                kafkaSenderService.send(
                        "amount.released",
                        cmd.getSagaId(),
                        new AmountReleasedEvent(
                                cmd.getSagaId(),
                                cmd.getOperationId(),
                                false,
                                e.getMessage()
                        )
                );
            } catch (Exception ex) {
                // log.error("Failed to process amount.release", ex);
            }
        }
    }

    // =========================
    // 🔁 TRANSFER MONEY
    // =========================
    @KafkaListener(topics = "money.transfer", groupId = "savings-group")
    public void onTransferMoney(byte[] message) {
        try {
            TransferMoneyCommand cmd = objectMapper.readValue(message, TransferMoneyCommand.class);

            savingsAccountsService.transferAmount(
                    cmd.getSenderId(),
                    cmd.getReceiverId(),
                    cmd.getCurrencyId(),
                    cmd.getAmount(),
                    cmd.getFee(),
                    cmd.getOperationId()
            );

            kafkaSenderService.send(
                    "money.transferred",
                    cmd.getSagaId(),
                    new MoneyTransferredEvent(
                            cmd.getSagaId(),
                            cmd.getOperationId(),
                            true,
                            null
                    )
            );

        } catch (Exception e) {
            try {
                TransferMoneyCommand cmd = objectMapper.readValue(message, TransferMoneyCommand.class);
                kafkaSenderService.send(
                        "money.transferred",
                        cmd.getSagaId(),
                        new MoneyTransferredEvent(
                                cmd.getSagaId(),
                                cmd.getOperationId(),
                                false,
                                e.getMessage()
                        )
                );
            } catch (Exception ex) {
                // log.error("Failed to process money.transfer", ex);
            }
        }
    }
}