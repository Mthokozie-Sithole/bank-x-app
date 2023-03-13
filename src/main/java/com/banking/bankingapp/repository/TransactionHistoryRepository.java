package com.banking.bankingapp.repository;

import com.banking.bankingapp.model.TransactionHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionHistoryRepository extends CrudRepository<TransactionHistory, Long> {
}
