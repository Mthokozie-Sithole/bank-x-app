package com.banking.bankingapp.model.entities;

import com.banking.bankingapp.model.Auditable;
import com.banking.bankingapp.model.constants.AccountType;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "bank_account")
public class BankAccount extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String accountNumber;
    private BigDecimal balance;

    private boolean paymentEnabled;
    @Enumerated(EnumType.ORDINAL)
    private AccountType accountType;

    @OneToOne
    private Customer customer;

    public BankAccount() {
    }

    public BankAccount(Long id, String accountNumber,
                       BigDecimal balance, AccountType accountType,
                       Customer customer, boolean paymentEnabled) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
        this.customer = customer;
        this.paymentEnabled = paymentEnabled;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public AccountType getAccountType() {
        return accountType;
    }
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isPaymentEnabled() {
        return paymentEnabled;
    }

    public void setPaymentEnabled(boolean paymentEnabled) {
        this.paymentEnabled = paymentEnabled;
    }
}
