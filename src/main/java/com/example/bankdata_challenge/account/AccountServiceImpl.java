package com.example.bankdata_challenge.account;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public AccountEntity openAccount(String firstName, String lastName) {
        AccountEntity account = AccountEntity.open(firstName, lastName);

        accountRepository.save(account);
        return account;
    }

    @Override
    @Transactional
    public void deposit(Integer accountNumber, BigDecimal amountDKK) {
        AccountEntity account = getAccount(accountNumber);
        account.deposit(amountDKK);
    }

    @Override
    @Transactional
    public void transfer(Integer sourceAccountNumber, Integer destinationAccountNumber, BigDecimal amountDKK) {
        AccountEntity sourceAccount = getAccount(sourceAccountNumber);
        AccountEntity destinationAccount = getAccount(destinationAccountNumber);

        sourceAccount.withdraw(amountDKK);
        destinationAccount.deposit(amountDKK);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountEntity getAccount(Integer accountNumber) {
        return accountRepository.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }
}
