package com.banking.bankingapp.service;

import com.banking.bankingapp.model.TransactionHistory;
import com.banking.bankingapp.repository.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionHistoryService {
    private final TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    public TransactionHistoryService(TransactionHistoryRepository transactionHistoryRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    public TransactionHistory saveTransaction(TransactionHistory transactionHistory) {
        return this.transactionHistoryRepository.save(transactionHistory);
    }

    public List<TransactionHistory> getAllTransactions() {
        return (List<TransactionHistory>) this.transactionHistoryRepository.findAll();
    }
}
