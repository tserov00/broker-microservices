package org.example.authservice.controller;

import jakarta.validation.Valid;
import org.example.authservice.dto.request.CustomerRegistrationDto;
import org.example.authservice.dto.response.CustomerRegistrationResponseDto;
import org.example.authservice.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody CustomerRegistrationDto customer) {
        CustomerRegistrationResponseDto responseDto = registrationService.registerCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

}
