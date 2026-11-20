
package com.example.bankdata_challenge.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AccountEntityTest {

    private AccountEntity account;

    @BeforeEach
    void setUp() {
        account = AccountEntity.open("John", "Doe");
    }

    @Test
    void openAccount_shouldCreateAccountWithZeroBalance() {
        // Arrange - handled by @BeforeEach

        // Act - handled by @BeforeEach

        // Assert
        assertThat(account.getFirstName()).isEqualTo("John");
        assertThat(account.getLastName()).isEqualTo("Doe");
        assertThat(account.getBalanceDKK()).isEqualByComparingTo("0.00");
    }

    @Test
    void deposit_shouldIncreaseBalance() {
        // Arrange
        BigDecimal depositAmountDKK = new BigDecimal("50.50");

        // Act
        account.deposit(depositAmountDKK);

        // Assert
        assertThat(account.getBalanceDKK()).isEqualByComparingTo("50.50");
    }

    @Test
    void deposit_shouldRejectZeroAmount() {
        // Arrange
        BigDecimal amountDKK = BigDecimal.ZERO;

        // Act & Assert
        assertThatThrownBy(() -> account.deposit(amountDKK))
                .isInstanceOf(IllegalArgumentException.class);

        // Assert
        assertThat(account.getBalanceDKK())
                .isEqualByComparingTo("0.00");
    }

    @Test
    void deposit_shouldRejectNegativeAmount() {
        // Arrange
        BigDecimal amountDKK = new BigDecimal("-10");

        // Act & Assert
        assertThatThrownBy(() -> account.deposit(amountDKK))
                .isInstanceOf(IllegalArgumentException.class);

        // Assert
        assertThat(account.getBalanceDKK())
                .isEqualByComparingTo("0.00");
    }

    @Test
    void withdraw_shouldDecreaseBalance() {
        // Arrange
        account.deposit(new BigDecimal("100"));
        BigDecimal withdrawalAmountDKK = new BigDecimal("49.95");

        // Act
        account.withdraw(withdrawalAmountDKK);

        // Assert
        assertThat(account.getBalanceDKK()).isEqualByComparingTo("50.05");
    }

    @Test
    void withdraw_shouldRejectInsufficientFunds() {
        // Arrange
        account.deposit(new BigDecimal("100.00"));
        BigDecimal withdrawalAmountDKK = new BigDecimal("150.00");

        // Act & Assert
        assertThatThrownBy(() -> account.withdraw(withdrawalAmountDKK))
                .isInstanceOf(InsufficientFunds.class);

        // Assert
        assertThat(account.getBalanceDKK())
                .isEqualByComparingTo("100.00");
    }

    @Test
    void withdraw_shouldRejectZeroAmount() {
        // Arrange
        BigDecimal amount = BigDecimal.ZERO;

        // Act & Assert
        assertThatThrownBy(() -> account.withdraw(amount))
                .isInstanceOf(IllegalArgumentException.class);

        // Assert
        assertThat(account.getBalanceDKK())
                .isEqualByComparingTo("0.00");
    }

    @Test
    void withdraw_shouldRejectNegativeAmount() {
        // Arrange
        BigDecimal amount = new BigDecimal("-50.00");

        // Act & Assert
        assertThatThrownBy(() -> account.withdraw(amount))
                .isInstanceOf(IllegalArgumentException.class);

        // Assert
        assertThat(account.getBalanceDKK())
                .isEqualByComparingTo("0.00");
    }
}
