package com.banking.bankingapp.service;

import com.banking.bankingapp.model.*;
import com.banking.bankingapp.repository.CurrentAccountRepository;
import com.banking.bankingapp.repository.CustomerRepository;
import com.banking.bankingapp.repository.SavingsAccountRepository;
import com.banking.bankingapp.repository.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {
    private final CurrentAccountRepository currentAccountRepository;
    private final SavingsAccountRepository savingsAccountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    public AccountService(CurrentAccountRepository currentAccountRepository,
                          SavingsAccountRepository savingsAccountRepository,
                          CustomerRepository customerRepository,
                          TransactionHistoryRepository transactionHistoryRepository) {

        this.currentAccountRepository = currentAccountRepository;
        this.savingsAccountRepository = savingsAccountRepository;
        this.customerRepository = customerRepository;
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    public BankAccount receivePayment(PaymentRequest paymentRequest) {
        Double newBalance = 0.0;
        BankAccount bankAccount = null;

        if (paymentRequest.isToSavingsAccount()) {
            SavingsAccount customerSavingsAccount = this.savingsAccountRepository
                    .findByAccountNumber(paymentRequest.getAccountNumber());

            if (Objects.nonNull(customerSavingsAccount)) {
                final double currentBalance = customerSavingsAccount.getBalance().doubleValue();
                newBalance = (currentBalance + currentBalance * 0.5f) + paymentRequest.getPaymentAmount();
                customerSavingsAccount.setBalance(BigDecimal.valueOf(newBalance));
                this.savingsAccountRepository.save(customerSavingsAccount);

                bankAccount = createBankAccountResponse(customerSavingsAccount.getAccountNumber(),
                        customerSavingsAccount.getAccountName(), customerSavingsAccount.getBalance());
            }
        } else {
            CurrentAccount currentAccount = this.currentAccountRepository
                    .findByAccountNumber(paymentRequest.getAccountNumber());

            if (Objects.nonNull(currentAccount)) {
                final double currentBalance = currentAccount.getBalance().doubleValue();
                newBalance = (currentBalance + currentBalance * 0.5f) + paymentRequest.getPaymentAmount();
                currentAccount.setBalance(BigDecimal.valueOf(newBalance));
                this.currentAccountRepository.save(currentAccount);

                bankAccount = createBankAccountResponse(currentAccount.getAccountNumber(),
                        currentAccount.getAccountName(), currentAccount.getBalance());
            }
        }

        createTransactionHistoryEntry("Incoming payment",
                BigDecimal.valueOf(paymentRequest.getPaymentAmount()),
                paymentRequest.getAccountNumber(), null);

        return bankAccount;
    }

    private BankAccount createBankAccountResponse(String accountNumber, String accountName, BigDecimal balance) {
        return new BankAccount(accountNumber, accountName, balance);
    }

    public Customer doTransfer(TransferRequest transferRequest) {
        Optional<Customer> customer = this.customerRepository
                .findById(Long.valueOf(transferRequest.getCustomerId()));

        Optional<CurrentAccount> currentAccount = this.currentAccountRepository
                .findById(Long.valueOf(transferRequest.getCustomerId()));

        Optional<SavingsAccount> savingsAccount = this.savingsAccountRepository
                .findById(Long.valueOf(transferRequest.getCustomerId()));

        if (currentAccount.isPresent() && savingsAccount.isPresent()) {
            CurrentAccount customerCurrentAccount = currentAccount.get();
            SavingsAccount customerSavingsAccount = savingsAccount.get();

            if (transferRequest.isFromSavingsAccount()) {
                transferFromSavingsAccount(transferRequest,
                        savingsAccount,
                        customerCurrentAccount,
                        customerSavingsAccount);

                createTransactionHistoryEntry("Transfer",
                        BigDecimal.valueOf(transferRequest.getTransferAmount()),
                        customerCurrentAccount.getAccountNumber(),
                        customerSavingsAccount.getAccountNumber());
            } else {
                transferFromCurrentAccount(transferRequest,
                        currentAccount,
                        customerCurrentAccount,
                        customerSavingsAccount);

                createTransactionHistoryEntry("Transfer", BigDecimal.
                                valueOf(transferRequest.getTransferAmount()),
                        customerSavingsAccount.getAccountNumber(),
                        customerCurrentAccount.getAccountNumber());
            }
        }
        return customer.get();
    }

    private void createTransactionHistoryEntry(String Transfer,
                                               BigDecimal transactionAmount,
                                               String toAccount,
                                               String fromAccount) {

        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setTransactionType(Transfer);
        transactionHistory.setTransactionAmount(transactionAmount);
        transactionHistory.setToAccount(toAccount);
        transactionHistory.setFromAccount(fromAccount);
        transactionHistory.setCreatedBy("system");
        transactionHistory.setModifiedBy("system");
        this.transactionHistoryRepository.save(transactionHistory);
    }

    private void transferFromSavingsAccount(TransferRequest transferRequest,
                                            Optional<SavingsAccount> savingsAccount,
                                            CurrentAccount customerCurrentAccount,
                                            SavingsAccount customerSavingsAccount) {

        if (savingsAccount.get().getBalance().doubleValue() >= transferRequest.getTransferAmount()) {
            customerCurrentAccount.setBalance(customerCurrentAccount.getBalance()
                    .add(new BigDecimal(transferRequest.getTransferAmount())));

            customerSavingsAccount.setBalance(customerSavingsAccount.getBalance()
                    .subtract(new BigDecimal(transferRequest.getTransferAmount())));

            updateCustomerAccounts(customerCurrentAccount, customerSavingsAccount);
        }
    }

    private void transferFromCurrentAccount(TransferRequest transferRequest,
                                            Optional<CurrentAccount> currentAccount,
                                            CurrentAccount customerCurrentAccount,
                                            SavingsAccount customerSavingsAccount) {

        if (currentAccount.get().getBalance().doubleValue() >= transferRequest.getTransferAmount()) {
            customerCurrentAccount.setBalance(customerCurrentAccount.getBalance()
                    .subtract(new BigDecimal(transferRequest.getTransferAmount())));

            customerSavingsAccount.setBalance(customerSavingsAccount.getBalance()
                    .add(new BigDecimal(transferRequest.getTransferAmount())));

            updateCustomerAccounts(customerCurrentAccount, customerSavingsAccount);
        }
    }

    public BankAccount debitAccount(AccountRequest accountRequest) {
        BankAccount bankAccount = new BankAccount();

        if (accountRequest.getAccountType().equals("Current")) {
            CurrentAccount currentAccount = this.currentAccountRepository
                    .findByAccountNumber(accountRequest.getAccountNumber());

            if (Objects.nonNull(currentAccount)) {
                final BigDecimal newBalance = currentAccount.getBalance()
                        .add(BigDecimal.valueOf(accountRequest.getAmount()));

                currentAccount.setBalance(newBalance);
                this.currentAccountRepository.save(currentAccount);

                bankAccount = this.createBankAccountResponse(currentAccount.getAccountNumber(),
                        currentAccount.getAccountName(),
                        currentAccount.getBalance());
            }
        } else {
            SavingsAccount savingsAccount = this.savingsAccountRepository
                    .findByAccountNumber(accountRequest.getAccountNumber());

            if (Objects.nonNull(savingsAccount)) {
                final BigDecimal newBalance = savingsAccount.getBalance()
                        .add(BigDecimal.valueOf(accountRequest.getAmount()));

                savingsAccount.setBalance(newBalance);
                this.savingsAccountRepository.save(savingsAccount);

                bankAccount = this.createBankAccountResponse(savingsAccount.getAccountNumber(),
                        savingsAccount.getAccountName(),
                        savingsAccount.getBalance());
            }
        }

        this.createTransactionHistoryEntry("Debit",
                BigDecimal.valueOf(accountRequest.getAmount()),
                accountRequest.getAccountNumber(), null);

        return bankAccount;
    }

    public BankAccount creditAccount(AccountRequest accountRequest) {
        BankAccount bankAccount = new BankAccount();

        if (accountRequest.getAccountType().equals("Current")) {
            CurrentAccount currentAccount = this.currentAccountRepository
                    .findByAccountNumber(accountRequest.getAccountNumber());

            final BigDecimal newBalance = currentAccount.getBalance()
                    .subtract(BigDecimal.valueOf(accountRequest.getAmount()));

            currentAccount.setBalance(newBalance);
            this.currentAccountRepository.save(currentAccount);

            bankAccount = this.createBankAccountResponse(currentAccount.getAccountNumber(),
                    currentAccount.getAccountName(),
                    currentAccount.getBalance());
        } else {
            SavingsAccount savingsAccount = this.savingsAccountRepository
                    .findByAccountNumber(accountRequest.getAccountNumber());

            final BigDecimal newBalance = savingsAccount.getBalance()
                    .subtract(BigDecimal.valueOf(accountRequest.getAmount()));

            savingsAccount.setBalance(newBalance);
            this.savingsAccountRepository.save(savingsAccount);

            bankAccount = this.createBankAccountResponse(savingsAccount.getAccountNumber(),
                    savingsAccount.getAccountName(),
                    savingsAccount.getBalance());

        }

        this.createTransactionHistoryEntry("Credit",
                BigDecimal.valueOf(accountRequest.getAmount()),
                null, accountRequest.getAccountNumber());

        return bankAccount;
    }
    private void updateCustomerAccounts(CurrentAccount customerCurrentAccount, SavingsAccount customerSavingsAccount) {
        this.savingsAccountRepository.save(customerSavingsAccount);
        this.currentAccountRepository.save(customerCurrentAccount);
    }

    public SavingsAccount createNewSavingsAccount() {
        SavingsAccount savingsAccount = new SavingsAccount();
        savingsAccount.setAccountName("Savings");
        savingsAccount.setAccountNumber(this.generateAccountNumber());
        savingsAccount.setBalance(new BigDecimal(500));
        this.savingsAccountRepository.save(savingsAccount);
        return savingsAccount;
    }

    public CurrentAccount createNewCurrentAccount() {
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setAccountName("Current");
        currentAccount.setAccountNumber(this.generateAccountNumber());
        currentAccount.setBalance(new BigDecimal(0));
        this.currentAccountRepository.save(currentAccount);
        return currentAccount;
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().replaceAll("[\\D.]", "");
    }
}
