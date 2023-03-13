package com.banking.bankingapp.model;

public class AccountRequest {
    private String accountNumber;
    private Double amount;

    private String accountType;

    public AccountRequest(String accountNumber, Double amount, String accountType) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.accountType = accountType;
    }

    public AccountRequest() {
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
