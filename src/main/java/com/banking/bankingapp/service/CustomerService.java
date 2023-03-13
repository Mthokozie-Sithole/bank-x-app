package com.banking.bankingapp.service;

import com.banking.bankingapp.model.Customer;
import com.banking.bankingapp.repository.CurrentAccountRepository;
import com.banking.bankingapp.repository.CustomerRepository;
import com.banking.bankingapp.repository.SavingsAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CurrentAccountRepository currentAccountRepository;
    private final SavingsAccountRepository savingsAccountRepository;

    private AccountService accountService;
    @Autowired
    public CustomerService(@Qualifier("customerRepo") CustomerRepository customerRepository,
                           @Qualifier("currentAccountRepo") CurrentAccountRepository currentAccountRepository,
                           @Qualifier("savingsAccountRepository") SavingsAccountRepository savingsAccountRepository,
                           AccountService accountService) {

        this.customerRepository = customerRepository;
        this.currentAccountRepository = currentAccountRepository;
        this.savingsAccountRepository = savingsAccountRepository;
        this.accountService = accountService;
    }

    public Customer onboardCustomer(Customer customer) {
        customer.setSavingsAccount(this.accountService.createNewSavingsAccount());
        customer.setCurrentAccount(this.accountService.createNewCurrentAccount());
        return this.customerRepository.save(customer);
    }

    public Optional<Customer> findCustomerById(Long customerId) {
        return Optional.ofNullable(this.customerRepository.findById(customerId).orElse(null));
    }

    public List<Customer> findAllCustomers() {
        return (List<Customer>) this.customerRepository.findAll();
    }
}
