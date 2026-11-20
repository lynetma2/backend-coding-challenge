package com.example.bankdata_challenge.currency.client.history;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Embeddable
public record ConversionRateCacheId(
        LocalDate date,
        String baseCurrency,
        BigDecimal requestedAmount
) implements Serializable {
}
