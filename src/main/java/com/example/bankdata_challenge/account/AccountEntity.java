package com.example.bankdata_challenge.account;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountNumber;

    @Version
    private long version;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balanceDKK;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    protected AccountEntity() {
        // Needed for JPA
    }

    private AccountEntity(String firstName, String lastName, BigDecimal balanceDKK) {
        this.firstName = Objects.requireNonNull(firstName);
        this.lastName = Objects.requireNonNull(lastName);
        this.balanceDKK = Objects.requireNonNull(balanceDKK);
    }

    public static AccountEntity open(String firstName, String lastName) {
        return new AccountEntity(firstName, lastName, BigDecimal.ZERO);
    }

    public void deposit(BigDecimal amountDKK) {
        if (amountDKK.signum() <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        this.balanceDKK = this.balanceDKK.add(amountDKK);
    }

    public void withdraw(BigDecimal amountDKK) {
        if (this.balanceDKK.compareTo(amountDKK) < 0 || this.balanceDKK.signum() <= 0) {
            throw new InsufficientFunds("Tried to withdraw, more than balance has.");
        }

        this.balanceDKK = this.balanceDKK.subtract(amountDKK);
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalanceDKK() {
        return balanceDKK;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
