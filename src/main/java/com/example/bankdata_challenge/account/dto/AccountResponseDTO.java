package com.example.bankdata_challenge.account.dto;

import com.example.bankdata_challenge.account.AccountEntity;

import java.math.BigDecimal;

public record AccountResponseDTO(
        Integer accountNumber,
        String firstName,
        String lastName,
        BigDecimal balanceDKK
) {

    public static AccountResponseDTO from(AccountEntity accountEntity) {
        return new AccountResponseDTO(
                accountEntity.getAccountNumber(),
                accountEntity.getFirstName(),
                accountEntity.getLastName(),
                accountEntity.getBalanceDKK()
        );
    }
}
