package com.example.bankdata_challenge.currency.client.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ConversionRateCacheRepository extends JpaRepository<ConversionRateCacheEntity, ConversionRateCacheId> {

    @Query("""
            SELECT c
            FROM ConversionRateCacheEntity c
            WHERE c.id.baseCurrency = :baseCurrency
              AND c.id.requestedAmount = :amount
              AND c.id.date BETWEEN :from AND :to
            """)
    List<ConversionRateCacheEntity> findInRange(
            @Param("baseCurrency") String baseCurrency,
            @Param("amount") BigDecimal amount,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}
