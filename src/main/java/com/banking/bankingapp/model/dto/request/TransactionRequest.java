package com.banking.bankingapp.model.dto.request;

import com.banking.bankingapp.model.constants.TransactionType;

public class TransactionRequest {
    private String accountNumber;
    private Double amount;
    private TransactionType transactionType;
    private String transactionId;

    public TransactionRequest(String accountNumber, Double amount,
                              TransactionType transactionType, String transactionId) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionId = transactionId;
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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
