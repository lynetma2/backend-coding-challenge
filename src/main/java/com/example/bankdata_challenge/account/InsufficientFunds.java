package com.example.bankdata_challenge.account;

public class InsufficientFunds extends RuntimeException {
    public InsufficientFunds(String message) {
        super(message);
    }
}
