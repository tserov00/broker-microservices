package org.example.authservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.authservice.dto.request.CustomerAuthenticationDto;
import org.example.authservice.dto.response.JwtResponse;
import org.example.authservice.repository.CustomerAccountRepository;
import org.example.authservice.security.JwtGenerator;
import org.example.authservice.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final CustomerAccountRepository accountRepository;
    private final JwtGenerator jwtGenerator;

    public AuthenticationController(AuthenticationService authenticationService, CustomerAccountRepository accountRepository, JwtGenerator jwtGenerator) {
        this.authenticationService = authenticationService;
        this.accountRepository = accountRepository;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateCustomer(@Valid @RequestBody CustomerAuthenticationDto authenticationDto, HttpServletRequest request) {
        Authentication authentication = authenticationService.authenticateCustomer(authenticationDto);

        String token = jwtGenerator.generateToken(authentication);
        return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(token));
    }
}
