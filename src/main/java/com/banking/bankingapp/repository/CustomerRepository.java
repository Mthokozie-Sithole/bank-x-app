package com.banking.bankingapp.repository;

import com.banking.bankingapp.model.Customer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("customerRepo")
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
