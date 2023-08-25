package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.models.Transaction;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientRepository clientRepository;


    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccountById(@PathVariable Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account != null) {
            List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());
            return new AccountDTO(account, new HashSet<>(transactions));
        } else {
            return null;
        }
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication
            ) {
        Client client03 = clientRepository.findByEmail(authentication.getName());
        String prefix = "VIN-";
        Random random = new Random();
        int randomNumber = random.nextInt(99999999) + 1;
        String accountNumber = prefix + randomNumber;

        List<Account> clientAccounts = accountRepository.findByClient(client03);
        if (clientAccounts.size() >= 3) {
            return new ResponseEntity<>("Cannot create a new account", HttpStatus.FORBIDDEN);
        }

        Account newAccount = new Account(accountNumber, 0, client03, LocalDate.now());
        accountRepository.save(newAccount);

        return new ResponseEntity<>("Account created correctly", HttpStatus.CREATED);
    }

}
