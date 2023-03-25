package com.banking.bankingapp.model.dto.request;

import com.banking.bankingapp.model.constants.TransactionType;

import java.time.LocalDate;

public class Notification {
    private String affectedAccountNumber;
    private Double amount;
    private String subject;
    private String content;
    private TransactionType transactionType;
    private LocalDate transactionDate;

    public Notification() {
    }

    public Notification(String affectedAccountNumber, Double amount,
                        String subject, String content,
                        TransactionType transactionType,
                        LocalDate transactionDate) {

        this.affectedAccountNumber = affectedAccountNumber;
        this.amount = amount;
        this.subject = subject;
        this.content = content;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }

    public String getAffectedAccountNumber() {
        return affectedAccountNumber;
    }

    public void setAffectedAccountNumber(String affectedAccountNumber) {
        this.affectedAccountNumber = affectedAccountNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "affectedAccountNumber='" + affectedAccountNumber + '\'' +
                ", amount=" + amount +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", transactionType=" + transactionType +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
