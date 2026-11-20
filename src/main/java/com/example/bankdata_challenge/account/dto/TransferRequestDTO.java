package com.example.bankdata_challenge.account.dto;

import java.math.BigDecimal;

public record TransferRequestDTO(
        Integer destinationAccountNumber,
        BigDecimal amountDKK
) {
}
