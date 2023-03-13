package com.banking.bankingapp.repository;

import com.banking.bankingapp.model.CurrentAccount;
import com.banking.bankingapp.model.SavingsAccount;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("savingsAccountRepo")
public interface SavingsAccountRepository extends CrudRepository<SavingsAccount, Long> {

    SavingsAccount findByAccountNumber(String accountNumber);
}
