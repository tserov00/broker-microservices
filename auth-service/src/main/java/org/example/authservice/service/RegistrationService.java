package org.example.authservice.service;

import jakarta.transaction.Transactional;
import org.example.authservice.dto.request.CustomerRegistrationDto;
import org.example.authservice.dto.response.CustomerRegistrationResponseDto;
import org.example.authservice.entity.Customer;
import org.example.authservice.entity.CustomerAccount;
import org.example.authservice.mapper.CustomerMapper;
import org.example.authservice.repository.CustomerAccountRepository;
import org.example.authservice.repository.CustomerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final CustomerRepository customerRepository;
    private final CustomerAccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomerMapper customerMapper;

    public RegistrationService(CustomerRepository customerRepository, CustomerAccountRepository accountRepository, BCryptPasswordEncoder passwordEncoder, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerMapper = customerMapper;
    }

    @Transactional
    public CustomerRegistrationResponseDto registerCustomer(CustomerRegistrationDto registrationDto) {
        Customer customer = new Customer(registrationDto.getFirstName(), registrationDto.getLastName(), registrationDto.getBirthDate(),
                registrationDto.getPassportSeries(), registrationDto.getAddress(), registrationDto.getTaxId());

        customerRepository.save(customer);

        String encodedPassword = passwordEncoder.encode(registrationDto.getPassword());

        CustomerAccount account = new CustomerAccount(customer, registrationDto.getPhoneNumber(), registrationDto.getEmail(),
                registrationDto.getLogin(), encodedPassword);

        accountRepository.save(account);

        CustomerRegistrationResponseDto responseDto = customerMapper.toResponseDto(customer, account);

        return responseDto;
    }
}
