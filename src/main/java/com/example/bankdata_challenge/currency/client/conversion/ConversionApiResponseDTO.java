package com.example.bankdata_challenge.currency.client.conversion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConversionApiResponseDTO(
        String result,
        @JsonProperty("error-type") String errorType,
        @JsonProperty("time_last_update_unix") Long timeLastUpdateUnix,
        @JsonProperty("base_code") String baseCode,
        @JsonProperty("target_code") String targetCode,
        @JsonProperty("conversion_rate") BigDecimal conversionRate,
        @JsonProperty("conversion_result") BigDecimal conversionResult) {

    public boolean isSuccess() {
        return "success".equals(result);
    }
}