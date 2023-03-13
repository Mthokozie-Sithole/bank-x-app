package com.banking.bankingapp.repository;

import com.banking.bankingapp.model.CurrentAccount;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("currentAccountRepo")
public interface CurrentAccountRepository extends CrudRepository<CurrentAccount, Long> {
    CurrentAccount findByAccountNumber(String accountNumber);
}
