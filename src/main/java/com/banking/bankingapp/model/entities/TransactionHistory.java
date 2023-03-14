package com.banking.bankingapp.model.entities;

import com.banking.bankingapp.model.Auditable;
import com.banking.bankingapp.model.constants.TransactionType;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "transaction_history")
public class TransactionHistory extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated
    private TransactionType transactionType;
    private BigDecimal transactionAmount;
    private String fromAccount;
    private String toAccount;
    private String transactionId;
    public TransactionHistory() {}
    public TransactionHistory(TransactionType transactionType, BigDecimal transactionAmount,
                              String fromAccount, String toAccount, String transactionId) {
        this.transactionType = transactionType;
        this.transactionAmount = transactionAmount;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.transactionId = transactionId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }
    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


}
