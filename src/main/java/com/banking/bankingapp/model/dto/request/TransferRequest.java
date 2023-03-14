package com.banking.bankingapp.model.dto.request;

public class TransferRequest {
    private Double transferAmount;
    private String fromAccount;
    private String toAccount;
    public TransferRequest() {
    }
    public TransferRequest(Double transferAmount, String fromAccount, String toAccount) {
        this.transferAmount = transferAmount;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    public Double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Double transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }
}
