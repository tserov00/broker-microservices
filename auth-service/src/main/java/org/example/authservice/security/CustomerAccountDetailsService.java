package org.example.authservice.security;

import org.example.authservice.entity.CustomerAccount;
import org.example.authservice.repository.CustomerAccountRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerAccountDetailsService implements UserDetailsService {
    private final CustomerAccountRepository accountRepository;

    public CustomerAccountDetailsService(CustomerAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public CustomerAccountDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        CustomerAccount account = accountRepository.findByLogin(login);
        if (account == null) {throw new UsernameNotFoundException(login);}
        return new CustomerAccountDetails(account);
    }
}
