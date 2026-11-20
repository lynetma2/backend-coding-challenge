package com.example.bankdata_challenge.currency.client.history;

import com.example.bankdata_challenge.currency.client.conversion.ConversionRate;
import com.example.bankdata_challenge.currency.client.conversion.ConversionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final ConversionRateCacheRepository cacheRepository;
    private final HistoryApiClient historyApiClient;
    private final ConversionService conversionService;

    public HistoryServiceImpl(ConversionRateCacheRepository cacheRepository, HistoryApiClient historyApiClient, ConversionService conversionService) {
        this.cacheRepository = cacheRepository;
        this.historyApiClient = historyApiClient;
        this.conversionService = conversionService;
    }

    @Override
    public Map<LocalDate, ConversionRate> getConversionHistoryInRange(
            LocalDate from,
            LocalDate to,
            Set<Year> excludedYears
    ) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("'from' must not be after 'to'");
        }

        Currency baseCurrency = Currency.getInstance("DKK");
        BigDecimal requestedAmount = new BigDecimal(100);
        Map<LocalDate, ConversionRate> result = new TreeMap<>();

        List<ConversionRateCacheEntity> cachedEntries =
                cacheRepository.findInRange(
                        baseCurrency.getCurrencyCode(),
                        requestedAmount,
                        from,
                        to
                );

        if (cachedEntries != null) {
            for (ConversionRateCacheEntity entry : cachedEntries) {
                LocalDate date = entry.getDate();

                if (!excludedYears.contains(Year.from(date))) {
                    result.put(date, toConversionRateDKKUSD(entry));
                }
            }
        }

        List<ConversionRateCacheEntity> newEntries = new ArrayList<>();
        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            if (excludedYears.contains(Year.from(date)) || result.containsKey(date)) {
                continue;
            }

            ConversionRateCacheEntity entity = historyApiClient.fetchDate(baseCurrency, date, requestedAmount);
            newEntries.add(entity);
            result.put(date, toConversionRateDKKUSD(entity));
        }
        cacheRepository.saveAll(newEntries);

        return result;
    }

    @Override
    public Map<LocalDate, ConversionRate> getConversionHistoryFrom2005To2015Excluding2012IncludingToday() {
        LocalDate from = LocalDate.of(2005, 1,1);
        LocalDate to = LocalDate.of(2014,12,31);
        Set<Year> excludedYears = Set.of(Year.of(2012));
        Map<LocalDate, ConversionRate> result = getConversionHistoryInRange(from, to, excludedYears);
        LocalDate today = LocalDate.now();
        ConversionRate todayRate = conversionService.convertHundredDKKtoUSD();
        result.put(today, todayRate);

        return result;
    }

    private ConversionRate toConversionRateDKKUSD(ConversionRateCacheEntity entity) {
        BigDecimal targetAmount = entity.getConversionAmounts().get("USD");
        return new ConversionRate(entity.getRequestedAmount(), targetAmount, "DKK", "USD");
    }
}
