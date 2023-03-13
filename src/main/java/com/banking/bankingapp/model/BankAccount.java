package com.banking.bankingapp.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class BankAccount implements Serializable {
    private String accountNumber;
    private String accountName;

    private BigDecimal balance;

    public BankAccount() {
    }

    public BankAccount(String accountNumber, String accountName, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
