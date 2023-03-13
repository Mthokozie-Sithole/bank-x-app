package com.banking.bankingapp.model;

public class TransferRequest {
    private Double transferAmount;
    private String customerId;
    private boolean fromSavingsAccount;

    public TransferRequest() {
    }

    public TransferRequest(Double transferAmount, String customerId, boolean fromSavingsAccount) {
        this.transferAmount = transferAmount;
        this.customerId = customerId;
        this.fromSavingsAccount = fromSavingsAccount;
    }

    public Double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Double transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public boolean isFromSavingsAccount() {
        return fromSavingsAccount;
    }

    public void setFromSavingsAccount(boolean fromSavingsAccount) {
        this.fromSavingsAccount = fromSavingsAccount;
    }
}
