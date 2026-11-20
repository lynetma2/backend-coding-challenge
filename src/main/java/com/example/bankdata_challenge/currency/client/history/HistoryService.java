package com.example.bankdata_challenge.currency.client.history;

import com.example.bankdata_challenge.currency.client.conversion.ConversionRate;

import java.time.LocalDate;
import java.time.Year;
import java.util.Map;
import java.util.Set;

public interface HistoryService {

    Map<LocalDate, ConversionRate> getConversionHistoryInRange(LocalDate from, LocalDate to, Set<Year> excludedYears);

    Map<LocalDate, ConversionRate> getConversionHistoryFrom2005To2015Excluding2012IncludingToday();
}
