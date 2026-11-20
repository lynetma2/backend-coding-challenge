
package com.example.bankdata_challenge.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountEntity account;
    private AccountEntity destinationAccount;

    private static final Integer ACCOUNT_NUMBER = 1;
    private static final Integer DESTINATION_ACCOUNT_NUMBER = 2;
    private static final Integer MISSING_ACCOUNT_NUMBER = 999;

    @BeforeEach
    void setUp() {
        account = AccountEntity.open("John", "Doe");
        destinationAccount = AccountEntity.open("Jane", "Doe");
    }


    @Test
    void openAccount_shouldCreateAndSaveAccount() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";

        when(accountRepository.save(any(AccountEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        accountService.openAccount(firstName, lastName);

        // Assert
        ArgumentCaptor<AccountEntity> accountCaptor =
                ArgumentCaptor.forClass(AccountEntity.class);

        verify(accountRepository).save(accountCaptor.capture());

        AccountEntity savedAccount = accountCaptor.getValue();

        assertThat(savedAccount.getFirstName()).isEqualTo(firstName);
        assertThat(savedAccount.getLastName()).isEqualTo(lastName);
        assertThat(savedAccount.getBalanceDKK()).isEqualByComparingTo("0.00");
    }


    @Test
    void getAccount_shouldReturnAccount_whenAccountExists() {
        // Arrange
        mockAccountFound();

        // Act
        AccountEntity result = accountService.getAccount(ACCOUNT_NUMBER);

        // Assert
        assertThat(result).isSameAs(account);
    }

    @Test
    void getAccount_shouldThrowException_whenAccountDoesNotExist() {
        // Arrange
        mockAccountNotFound(MISSING_ACCOUNT_NUMBER);

        // Act & Assert
        assertThatThrownBy(() -> accountService.getAccount(MISSING_ACCOUNT_NUMBER))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void deposit_shouldIncreaseAccountBalance() {
        // Arrange
        BigDecimal depositAmountDKK = new BigDecimal("100");
        mockAccountFound();

        // Act
        accountService.deposit(ACCOUNT_NUMBER, depositAmountDKK);

        // Assert
        assertThat(account.getBalanceDKK()).isEqualByComparingTo("100");
    }

    @Test
    void deposit_shouldThrowException_whenAccountDoesNotExist() {
        // Arrange
        BigDecimal depositAmountDKK = new BigDecimal("100");
        mockAccountNotFound(MISSING_ACCOUNT_NUMBER);

        // Act & Assert
        assertThatThrownBy(() -> accountService.deposit(MISSING_ACCOUNT_NUMBER, depositAmountDKK))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void transfer_shouldMoveMoneyBetweenAccounts() {
        // Arrange
        account.deposit(new BigDecimal("200"));
        BigDecimal transferAmountDKK = new BigDecimal("100");
        mockAccountFound();
        mockDestinationAccountFound();

        // Act
        accountService.transfer(ACCOUNT_NUMBER, DESTINATION_ACCOUNT_NUMBER, transferAmountDKK);

        // Assert
        assertThat(account.getBalanceDKK()).isEqualByComparingTo("100");
        assertThat(destinationAccount.getBalanceDKK()).isEqualByComparingTo("100");
    }

    @Test
    void transfer_shouldThrowException_whenSourceAccountDoesNotExist() {
        // Arrange
        destinationAccount.deposit(new BigDecimal("100"));
        BigDecimal transferAmountDKK = new BigDecimal("100");
        mockAccountNotFound(ACCOUNT_NUMBER);

        // Act & Assert
        assertThatThrownBy(() -> accountService.transfer(ACCOUNT_NUMBER, DESTINATION_ACCOUNT_NUMBER, transferAmountDKK))
                .isInstanceOf(AccountNotFoundException.class);

        // Assert
        assertThat(account.getBalanceDKK()).isEqualByComparingTo("0.00");
        assertThat(destinationAccount.getBalanceDKK()).isEqualByComparingTo("100");
    }

    @Test
    void transfer_shouldThrowException_whenDestinationAccountDoesNotExist() {
        // Arrange
        account.deposit(new BigDecimal("200"));
        BigDecimal transferAmountDKK = new BigDecimal("100");
        mockAccountFound();
        mockAccountNotFound(DESTINATION_ACCOUNT_NUMBER);

        // Act & Assert
        assertThatThrownBy(() -> accountService.transfer(ACCOUNT_NUMBER, DESTINATION_ACCOUNT_NUMBER, transferAmountDKK))
                .isInstanceOf(AccountNotFoundException.class);

        // Assert
        assertThat(account.getBalanceDKK()).isEqualByComparingTo("200");
    }

    private void mockAccountFound() {
        when(accountRepository.findById(ACCOUNT_NUMBER))
                .thenReturn(Optional.of(account));
    }

    private void mockDestinationAccountFound() {
        when(accountRepository.findById(DESTINATION_ACCOUNT_NUMBER))
                .thenReturn(Optional.of(destinationAccount));
    }

    private void mockAccountNotFound(Integer accountNumber) {
        when(accountRepository.findById(accountNumber))
                .thenReturn(Optional.empty());
    }
}
