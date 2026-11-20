package com.example.bankdata_challenge.currency.client.history;


import com.example.bankdata_challenge.currency.client.conversion.ExchangeRateUnavailableException;
import com.example.bankdata_challenge.currency.client.conversion.UnsupportedCurrencyException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Map;

@Component
public class HistoryApiClient {

    private static final String API_KEY = "404c377f9ebf3b2be2c8a889"; //Very bad practice, only here to make it simple to show it works.
    private static final String BASE_URL = "https://v6.exchangerate-api.com";
    private static final String HISTORY_PATH = "/v6/{key}/history/{base}/{year}/{month}/{day}/{amount}";

    private final RestClient restClient;

    HistoryApiClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl(BASE_URL).build();
    }

    ConversionRateCacheEntity fetchDate(Currency base, LocalDate date, BigDecimal amount) {
        Map<String, String> uriVariables = Map.of(
                "key", API_KEY,
                "base", base.getCurrencyCode(),
                "year", String.valueOf(date.getYear()),
                "month", String.valueOf(date.getMonthValue()),
                "day", String.valueOf(date.getDayOfMonth()),
                "amount", amount.toPlainString());

        try {
            return ConversionRateCacheEntity.from(fetch(uriVariables));
        } catch (RestClientException e) {
            throw new ExchangeRateUnavailableException("Conversion API call failed", e);
        }
    }

    private HistoryApiResponseDTO fetch(Map<String, String> uriVariables) {
        try {
            return requireSuccess(restClient.get()
                    .uri(HISTORY_PATH, uriVariables)
                    .retrieve()
                    .body(HistoryApiResponseDTO.class));
        } catch (RestClientResponseException e) {
            return requireSuccess(e.getResponseBodyAs(HistoryApiResponseDTO.class));
        }
    }

    private HistoryApiResponseDTO requireSuccess(HistoryApiResponseDTO response) {
        if (response == null) {
            throw new ExchangeRateUnavailableException("Conversion API returned an empty response");
        }
        if (response.isSuccess()) {
            return response;
        }
        if ("unsupported-code".equals(response.errorType())) {
            throw new UnsupportedCurrencyException(response.errorType());
        }
        throw new ExchangeRateUnavailableException("Conversion API error: " + response.errorType());
    }
}
