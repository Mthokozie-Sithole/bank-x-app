package com.banking.bankingapp.model;

public class PaymentRequest {
    private String accountNumber;
    private Double paymentAmount;
    private boolean toSavingsAccount;

    public PaymentRequest(Double paymentAmount, boolean toSavingsAccount, String accountNumber) {
        this.paymentAmount = paymentAmount;
        this.toSavingsAccount = toSavingsAccount;
        this.accountNumber = accountNumber;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public boolean isToSavingsAccount() {
        return toSavingsAccount;
    }

    public void setToSavingsAccount(boolean toSavingsAccount) {
        this.toSavingsAccount = toSavingsAccount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
