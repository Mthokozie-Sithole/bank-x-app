package com.banking.bankingapp.model.dto.request;

import java.util.List;

public class ThirdPartyTransaction {

    private List<TransactionRequest> transactionRequests;

    public ThirdPartyTransaction() {
    }

    public ThirdPartyTransaction(List<TransactionRequest> transactionRequests) {
        this.transactionRequests = transactionRequests;
    }

    public List<TransactionRequest> getTransactionRequests() {
        return transactionRequests;
    }

    public void setTransactionRequests(List<TransactionRequest> transactionRequests) {
        this.transactionRequests = transactionRequests;
    }
}
