package com.example.bankdata_challenge.currency.client.history;

import com.example.bankdata_challenge.currency.client.conversion.ConversionRate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/history-rate")
    public Map<LocalDate, ConversionRate> historyRate() {
        return historyService.getConversionHistoryFrom2005To2015Excluding2012IncludingToday();
    }
}
