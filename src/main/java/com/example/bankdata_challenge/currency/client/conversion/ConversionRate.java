package com.example.bankdata_challenge.currency.client.conversion;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record ConversionRate(
        BigDecimal baseAmount,
        BigDecimal targetAmount,
        String baseCurrency,
        String targetCurrency
) {

    public static ConversionRate from(ConversionApiResponseDTO responseDTO, BigDecimal baseAmount) {
        BigDecimal scaledResult = responseDTO.conversionResult().setScale(2, RoundingMode.HALF_EVEN);

        return new ConversionRate(
                baseAmount,
                scaledResult,
                responseDTO.baseCode(),
                responseDTO.targetCode()
        );
    }
}
