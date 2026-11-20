package com.example.bankdata_challenge.account;

import com.example.bankdata_challenge.account.dto.AccountResponseDTO;
import com.example.bankdata_challenge.account.dto.CreateAccountRequestDTO;
import com.example.bankdata_challenge.account.dto.DepositRequestDTO;
import com.example.bankdata_challenge.account.dto.TransferRequestDTO;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/account")
    public AccountResponseDTO openAccount(@RequestBody CreateAccountRequestDTO createAccountRequestDTO) {
        AccountEntity newAccount = accountService
                .openAccount(createAccountRequestDTO.firstName(), createAccountRequestDTO.lastName());

        return AccountResponseDTO.from(newAccount);
    }

    @PostMapping("/account/{accountNumber}/deposit")
    public void deposit(@RequestBody DepositRequestDTO depositRequestDTO, @PathVariable Integer accountNumber) {
        accountService.deposit(accountNumber, depositRequestDTO.amountDKK());
    }

    @PostMapping("/account/{fromAccountNumber}/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO, @PathVariable Integer fromAccountNumber) {
        accountService.transfer(
                fromAccountNumber,
                transferRequestDTO.toAccountNumber(),
                transferRequestDTO.amountDKK()
        );
    }

    @GetMapping("/account/{accountNumber}")
    public AccountResponseDTO getAccount(@PathVariable Integer accountNumber) {
        AccountEntity account = accountService.getAccount(accountNumber);
        return AccountResponseDTO.from(account);
    }
}
