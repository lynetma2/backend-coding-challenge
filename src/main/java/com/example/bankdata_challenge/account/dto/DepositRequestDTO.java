package com.example.bankdata_challenge.account.dto;

import java.math.BigDecimal;

public record DepositRequestDTO(
        BigDecimal amountDKK
) {
}
