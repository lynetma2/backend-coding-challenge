package com.example.bankdata_challenge.currency.client.conversion;

import com.example.bankdata_challenge.account.AccountEntity;
import com.example.bankdata_challenge.account.dto.AccountResponseDTO;
import com.example.bankdata_challenge.account.dto.CreateAccountRequestDTO;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ConversionController {

    private final ConversionService conversionService;

    public ConversionController(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @GetMapping("/conversion-rate")
    public Map<String, BigDecimal> conversionRate() {
        return toResponse(conversionService.convertHundredDKKtoUSD());
    }

    private static Map<String, BigDecimal> toResponse(ConversionRate rate) {
        Map<String, BigDecimal> response = new HashMap<>();
        response.put(rate.baseCurrency(), rate.baseAmount());
        response.put(rate.targetCurrency(), rate.targetAmount());
        return response;
    }
}
