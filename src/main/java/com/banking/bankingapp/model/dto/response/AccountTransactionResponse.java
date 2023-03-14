package com.banking.bankingapp.model.dto.response;

import com.banking.bankingapp.model.constants.AccountType;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountTransactionResponse implements Serializable {
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal newBalance;
    public AccountTransactionResponse() {}

    public AccountTransactionResponse(String accountNumber, AccountType accountType, BigDecimal newBalance) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.newBalance = newBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(BigDecimal newBalance) {
        this.newBalance = newBalance;
    }
}
