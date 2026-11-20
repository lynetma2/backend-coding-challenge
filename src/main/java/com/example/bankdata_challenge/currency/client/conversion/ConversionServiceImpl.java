package com.example.bankdata_challenge.currency.client.conversion;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;

@Service
public class ConversionServiceImpl implements ConversionService{

    private final ConversionApiClient client;

    public ConversionServiceImpl(ConversionApiClient client) {
        this.client = client;
    }

    @Override
    public ConversionRate convertHundredDKKtoUSD() {
        return client.convert(Currency.getInstance("DKK"),
                Currency.getInstance("USD"),
                new BigDecimal(100));
    }
}
