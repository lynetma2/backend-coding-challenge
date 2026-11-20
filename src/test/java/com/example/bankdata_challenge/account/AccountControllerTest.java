
package com.example.bankdata_challenge.account;

import com.example.bankdata_challenge.account.dto.CreateAccountRequestDTO;
import com.example.bankdata_challenge.account.dto.DepositRequestDTO;
import com.example.bankdata_challenge.account.dto.TransferRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountService accountService;

    private static final Integer ACCOUNT_NUMBER = 123;
    private static final Integer DESTINATION_ACCOUNT_NUMBER = 456;

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";

    @Test
    void createAccount_shouldReturnCreated() throws Exception {
        // Arrange
        CreateAccountRequestDTO request =
                new CreateAccountRequestDTO(FIRST_NAME, LAST_NAME);
        AccountEntity createdAccount = mockAccount(BigDecimal.ZERO);

        when(accountService.openAccount(FIRST_NAME, LAST_NAME))
                .thenReturn(createdAccount);

        // Act & Assert
        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountNumber").value(ACCOUNT_NUMBER))
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(LAST_NAME))
                .andExpect(jsonPath("$.balanceDKK").value(0.00));

        verify(accountService).openAccount(FIRST_NAME, LAST_NAME);
    }

    @Test
    void getAccount_shouldReturnAccount() throws Exception {
        // Arrange
        BigDecimal amountDKK = new BigDecimal("100");
        AccountEntity account = mockAccount(amountDKK);

        when(accountService.getAccount(ACCOUNT_NUMBER))
                .thenReturn(account);

        // Act & Assert
        mockMvc.perform(get("/account/" + ACCOUNT_NUMBER))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountNumber").value(ACCOUNT_NUMBER))
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(LAST_NAME))
                .andExpect(jsonPath("$.balanceDKK").value(amountDKK));

        verify(accountService).getAccount(ACCOUNT_NUMBER);

    }

    @Test
    void deposit_shouldReturnNoContent() throws Exception {
        // Arrange
        BigDecimal amountDKK = new BigDecimal("50.00");
        DepositRequestDTO request = new DepositRequestDTO(amountDKK);

        // Act & Assert
        mockMvc.perform(post("/account/" + ACCOUNT_NUMBER + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isNoContent());

        verify(accountService).deposit(ACCOUNT_NUMBER, amountDKK);
    }

    @Test
    void transfer_shouldReturnNoContent() throws Exception {
        // Arrange
        BigDecimal amountDKK = new BigDecimal("50.00");
        TransferRequestDTO request = new TransferRequestDTO(
                DESTINATION_ACCOUNT_NUMBER,
                amountDKK
        );

        // Act & Assert
        mockMvc.perform(post("/account/" + ACCOUNT_NUMBER + "/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isNoContent());

        verify(accountService).transfer(
                ACCOUNT_NUMBER,
                DESTINATION_ACCOUNT_NUMBER,
                amountDKK
        );
    }

    private String toJson(Object object) {
        return objectMapper.writeValueAsString(object);
    }

    private AccountEntity mockAccount(BigDecimal balance) {
        AccountEntity mockedAccount = mock(AccountEntity.class);

        when(mockedAccount.getAccountNumber()).thenReturn(ACCOUNT_NUMBER);
        when(mockedAccount.getFirstName()).thenReturn(FIRST_NAME);
        when(mockedAccount.getLastName()).thenReturn(LAST_NAME);
        when(mockedAccount.getBalanceDKK()).thenReturn(balance);

        return mockedAccount;
    }
}
