package com.example.bankdata_challenge.account;

import jakarta.persistence.EntityNotFoundException;

public class AccountNotFoundException extends EntityNotFoundException
{
    public AccountNotFoundException(Integer accountNumber) {
        super("Account not found: " + accountNumber);
    }
}
