package com.example.bankdata_challenge.currency.client.history;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HistoryApiResponseDTO(
        String result,
        @JsonProperty("error-type") String errorType,
        Integer year,
        Integer month,
        Integer day,
        @JsonProperty("base_code") String baseCode,
        @JsonProperty("requested_amount") BigDecimal requestedAmount,
        @JsonProperty("conversion_amounts") Map<String, BigDecimal> conversionAmounts) {

    public boolean isSuccess() {
        return "success".equals(result);
    }

}