package com.example.bankdata_challenge.currency.client.history;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

@Entity
public class ConversionRateCacheEntity {

    @EmbeddedId
    private ConversionRateCacheId id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json", nullable = false)
    private Map<String, BigDecimal> conversionAmounts;

    protected ConversionRateCacheEntity() {
        // Needed for JPA
    }

    public ConversionRateCacheEntity(LocalDate date, String baseCurrency, BigDecimal requestedAmount, Map<String, BigDecimal> conversionAmounts) {
        Objects.requireNonNull(date);
        Objects.requireNonNull(baseCurrency);
        Objects.requireNonNull(requestedAmount);
        Objects.requireNonNull(conversionAmounts);

        this.id = new ConversionRateCacheId(date, baseCurrency, requestedAmount);
        this.conversionAmounts = conversionAmounts;
    }

    public static ConversionRateCacheEntity from(HistoryApiResponseDTO dto) {
        Objects.requireNonNull(dto);

        if (!dto.isSuccess()) {
            throw new IllegalArgumentException(
                    "Cannot create cache entity from unsuccessful API response");
        }

        LocalDate date = LocalDate.of(dto.year(), dto.month(), dto.day());

        return new ConversionRateCacheEntity(
                date,
                dto.baseCode(),
                dto.requestedAmount(),
                dto.conversionAmounts()
        );
    }

    public LocalDate getDate() {
        return this.id.date();
    }

    public BigDecimal getRequestedAmount() {
        return this.id.requestedAmount();
    }

    public Map<String, BigDecimal> getConversionAmounts() {
        return conversionAmounts;
    }
}
