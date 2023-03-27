package com.banking.bankingapp.service;

import com.banking.bankingapp.model.constants.AccountType;
import com.banking.bankingapp.model.entities.BankAccount;
import com.banking.bankingapp.model.entities.Customer;
import com.banking.bankingapp.repository.BankAccountRepository;
import com.banking.bankingapp.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;
    private AccountService accountService;
    @Autowired
    public CustomerService(CustomerRepository customerRepository,
                           BankAccountRepository bankAccountRepository,
                           AccountService accountService) {

        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.accountService = accountService;
    }
    public Customer onboardCustomer(Customer customer) {
        BankAccount savingAccount = this.accountService
                .createBankAccount(AccountType.SAVINGS, 500d, false);

        BankAccount currentAccount = this.accountService
                .createBankAccount(AccountType.CURRENT, 0.0d, true);

        this.bankAccountRepository.save(savingAccount);
        this.bankAccountRepository.save(currentAccount);

        List<BankAccount> customerAccounts = Arrays.asList(savingAccount, currentAccount);
        customer.setBankAccounts(customerAccounts);
        return this.customerRepository.save(customer);
    }
    public Optional<Customer> findCustomerById(Long customerId) {
        return Optional.ofNullable(this.customerRepository.findById(customerId).orElse(null));
    }
    public List<Customer> findAllCustomers() {
        return (List<Customer>) this.customerRepository.findAll();
    }
}
