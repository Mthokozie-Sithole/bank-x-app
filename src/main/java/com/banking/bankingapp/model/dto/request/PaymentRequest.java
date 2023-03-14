package com.banking.bankingapp.model.dto.request;

import com.banking.bankingapp.model.constants.AccountType;

public class PaymentRequest {
    private String accountNumber;
    private Double paymentAmount;
    private AccountType accountType;

    public PaymentRequest(Double paymentAmount, String accountNumber, AccountType accountType) {
        this.paymentAmount = paymentAmount;
        this.accountType = accountType;
        this.accountNumber = accountNumber;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
