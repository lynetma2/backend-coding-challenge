package com.example.bankdata_challenge.currency.client.conversion;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

@Component
public class ConversionApiClient {

    private static final String API_KEY = "404c377f9ebf3b2be2c8a889"; //Very bad practice, only here to make it simple to show it works.
    private static final String BASE_URL = "https://v6.exchangerate-api.com";
    private static final String CONVERSION_PATH = "/v6/{key}/pair/{base}/{target}/{amount}";

    private final RestClient restClient;

    ConversionApiClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl(BASE_URL).build();
    }

    ConversionRate convert(Currency base, Currency target, BigDecimal amount) {
        Map<String, String> uriVariables = Map.of(
                "key", API_KEY,
                "base", base.getCurrencyCode(),
                "target", target.getCurrencyCode(),
                "amount", amount.toPlainString());

        try {
            return ConversionRate.from(fetch(uriVariables), amount);
        } catch (RestClientException e) {
            throw new ExchangeRateUnavailableException("Conversion API call failed", e);
        }
    }

    private ConversionApiResponseDTO fetch(Map<String, String> uriVariables) {
        try {
            return requireSuccess(restClient.get()
                    .uri(CONVERSION_PATH, uriVariables)
                    .retrieve()
                    .body(ConversionApiResponseDTO.class));
        } catch (RestClientResponseException e) {
            return requireSuccess(e.getResponseBodyAs(ConversionApiResponseDTO.class));
        }
    }

    private ConversionApiResponseDTO requireSuccess(ConversionApiResponseDTO response) {
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