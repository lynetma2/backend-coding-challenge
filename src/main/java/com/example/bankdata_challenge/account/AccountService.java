package com.example.bankdata_challenge.account;

import java.math.BigDecimal;

public interface AccountService {

    AccountEntity openAccount(String firstName, String lastName);

    void deposit(Integer accountNumber, BigDecimal amountDKK);

    void transfer(Integer sourceAccountNumber, Integer destinationAccountNumber, BigDecimal amountDKK);

    AccountEntity getAccount(Integer accountNumber);
}
