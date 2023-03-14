package com.banking.bankingapp.service;

import com.banking.bankingapp.exception.EntityNotFoundException;
import com.banking.bankingapp.exception.GlobalErrorCode;
import com.banking.bankingapp.exception.InsufficientFundsException;
import com.banking.bankingapp.model.constants.AccountType;
import com.banking.bankingapp.model.constants.TransactionType;
import com.banking.bankingapp.model.dto.request.TransactionRequest;
import com.banking.bankingapp.model.dto.request.PaymentRequest;
import com.banking.bankingapp.model.dto.request.TransferRequest;
import com.banking.bankingapp.model.dto.response.AccountTransactionResponse;
import com.banking.bankingapp.model.entities.BankAccount;
import com.banking.bankingapp.model.entities.TransactionHistory;
import com.banking.bankingapp.repository.BankAccountRepository;
import com.banking.bankingapp.repository.CustomerRepository;
import com.banking.bankingapp.repository.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountService {
    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    public AccountService(BankAccountRepository bankAccountRepository,
                          CustomerRepository customerRepository,
                          TransactionHistoryRepository transactionHistoryRepository) {

        this.bankAccountRepository = bankAccountRepository;
        this.customerRepository = customerRepository;
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    public AccountTransactionResponse receivePayment(PaymentRequest paymentRequest) {
        BankAccount bankAccount = this.bankAccountRepository
                .findByAccountNumber(paymentRequest.getAccountNumber())
                .orElseThrow(EntityNotFoundException::new);

        /*All payments made into the Savings Account will be credited with a 0.5% interest of the current balance.*/
        final double creditAmount = 0.5 * bankAccount.getBalance().doubleValue() / 100;

        final BigDecimal newBalance = subtractFromCurrentBalance(creditAmount
                , BigDecimal.valueOf(paymentRequest.getPaymentAmount()));

        bankAccount.setBalance(newBalance);

        this.bankAccountRepository.save(bankAccount);

        AccountTransactionResponse accountTransactionResponse = this
                .createAccountTransactionResponse(bankAccount.getAccountNumber(),
                        bankAccount.getAccountType(), bankAccount.getBalance());

        this.createTransactionHistoryEntry(TransactionType.INCOMING_PAYMENT,
                BigDecimal.valueOf(paymentRequest.getPaymentAmount()),
                paymentRequest.getAccountNumber(), null);

        return accountTransactionResponse;
    }

    private AccountTransactionResponse createAccountTransactionResponse(String accountNumber,
                                                                        AccountType accountType,
                                                                        BigDecimal balance) {
        return new AccountTransactionResponse(accountNumber, accountType, balance);
    }

    public AccountTransactionResponse doTransfer(TransferRequest transferRequest) {
        BankAccount fromAccount = this.bankAccountRepository
                .findByAccountNumber(transferRequest.getFromAccount())
                .orElseThrow(EntityNotFoundException::new);

        validateBalance(fromAccount, BigDecimal.valueOf(transferRequest.getTransferAmount()));

        BankAccount toAccount = this.bankAccountRepository
                .findByAccountNumber(transferRequest.getToAccount())
                .orElseThrow(EntityNotFoundException::new);

        fromAccount.setBalance(subtractFromCurrentBalance(transferRequest
                .getTransferAmount(), fromAccount.getBalance()));

        toAccount.setBalance(addToCurrentBalance(transferRequest
                .getTransferAmount(), toAccount.getBalance()));

        createTransactionHistoryEntry(TransactionType.TRANSFER, BigDecimal.
                        valueOf(transferRequest.getTransferAmount()),
                transferRequest.getToAccount(),
                transferRequest.getFromAccount());

        return new AccountTransactionResponse(toAccount.getAccountNumber()
                , toAccount.getAccountType()
                , toAccount.getBalance());
    }

    private void createTransactionHistoryEntry(TransactionType transactionType,
                                               BigDecimal transactionAmount,
                                               String toAccount,
                                               String fromAccount) {

        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setTransactionId(new StringBuilder("TRANS").append(this.generateRandomNumber()).toString());
        transactionHistory.setTransactionType(transactionType);
        transactionHistory.setTransactionAmount(transactionAmount);
        transactionHistory.setToAccount(toAccount);
        transactionHistory.setFromAccount(fromAccount);
        transactionHistory.setCreatedBy("system");
        transactionHistory.setModifiedBy("system");
        this.transactionHistoryRepository.save(transactionHistory);
    }

    public AccountTransactionResponse debitAccount(TransactionRequest transactionRequest) {
        BankAccount bankAccount = this.bankAccountRepository
                .findByAccountNumber(transactionRequest.getAccountNumber())
                .orElseThrow(EntityNotFoundException::new);

        bankAccount.setBalance(addToCurrentBalance(transactionRequest.getAmount(), bankAccount.getBalance()));
        this.bankAccountRepository.save(bankAccount);

        AccountTransactionResponse accountTransactionResponse = this
                .createAccountTransactionResponse(bankAccount.getAccountNumber(),
                        bankAccount.getAccountType(),
                        bankAccount.getBalance());

        this.createTransactionHistoryEntry(TransactionType.DEBIT,
                BigDecimal.valueOf(transactionRequest.getAmount()),
                null, bankAccount.getAccountNumber());

        return accountTransactionResponse;
    }

    public AccountTransactionResponse creditAccount(TransactionRequest transactionRequest) {
        BankAccount bankAccount = this.bankAccountRepository
                .findByAccountNumber(transactionRequest.getAccountNumber())
                .orElseThrow(EntityNotFoundException::new);

        this.validateBalance(bankAccount, BigDecimal.valueOf(transactionRequest.getAmount()));

        bankAccount.setBalance(subtractFromCurrentBalance(transactionRequest.getAmount()
                , bankAccount.getBalance()).negate());

        this.bankAccountRepository.save(bankAccount);

        this.createTransactionHistoryEntry(TransactionType.CREDIT,
                BigDecimal.valueOf(transactionRequest.getAmount()),
                null, transactionRequest.getAccountNumber());

        AccountTransactionResponse accountTransactionResponse = createAccountTransactionResponse(
                bankAccount.getAccountNumber(),
                bankAccount.getAccountType(),
                bankAccount.getBalance()
        );

        return accountTransactionResponse;
    }

    private static BigDecimal addToCurrentBalance(Double amount, final BigDecimal currentBalance) {
        return currentBalance.add(BigDecimal.valueOf(amount));
    }

    private static BigDecimal subtractFromCurrentBalance(Double amount, final BigDecimal currentBalance) {
        return currentBalance.subtract(BigDecimal.valueOf(amount));
    }

    public BankAccount createBankAccount(AccountType accountType,
                                         Double openingBalance,
                                         boolean paymentEnabled) {

        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountType(accountType);
        bankAccount.setAccountNumber(this.generateRandomNumber());
        bankAccount.setBalance(new BigDecimal(openingBalance));
        bankAccount.setPaymentEnabled(paymentEnabled);
        this.bankAccountRepository.save(bankAccount);
        return bankAccount;
    }

    private String generateRandomNumber() {
        return UUID.randomUUID().toString().replaceAll("[\\D.]", "");
    }

    private void validateBalance(BankAccount bankAccount, BigDecimal amount) {
        if (bankAccount.getBalance().compareTo(BigDecimal.ZERO) < 0 || bankAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in the account " + bankAccount.getAccountNumber(), GlobalErrorCode.INSUFFICIENT_FUNDS);
        }
    }
}
