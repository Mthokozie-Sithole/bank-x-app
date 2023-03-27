package com.banking.bankingapp.service;

import com.banking.bankingapp.exception.EntityNotFoundException;
import com.banking.bankingapp.exception.GlobalErrorCode;
import com.banking.bankingapp.exception.InsufficientFundsException;
import com.banking.bankingapp.model.constants.AccountType;
import com.banking.bankingapp.model.constants.TransactionType;
import com.banking.bankingapp.model.dto.request.*;
import com.banking.bankingapp.model.dto.response.AccountTransactionResponse;
import com.banking.bankingapp.model.entities.BankAccount;
import com.banking.bankingapp.model.entities.TransactionHistory;
import com.banking.bankingapp.repository.BankAccountRepository;
import com.banking.bankingapp.repository.CustomerRepository;
import com.banking.bankingapp.repository.TransactionHistoryRepository;
import com.banking.bankingapp.util.NotificationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountService {
    @Value("${spring.kafka.topics.payments-topic}")
    private String paymentsTopic;

    @Value("${spring.kafka.topics.transfers-topic}")
    private String transfersTopic;

    @Value("${spring.kafka.topics.debit-topic}")
    private String debitTopic;

    @Value("${spring.kafka.topics.credit-topic}")
    private String creditTopic;

    @Value("${spring.notifications.subjects.payment}")
    private String paymentSubject;

    @Value("${spring.notifications.subjects.transfer}")
    private String transferSubject;

    @Value("${spring.notifications.subjects.debit}")
    private String debitSubject;

    @Value("${spring.notifications.subjects.credit}")
    private String creditSubject;

    @Value("${spring.notifications.contents.payment-message}")
    private String paymentNotification;

    @Value("${spring.notifications.contents.transfer-message}")
    private String fundTransferNotification;

    @Value("${spring.notifications.contents.debit-message}")
    private String debitNotification;

    @Value("${spring.notifications.contents.credit-message}")
    private String creditNotification;
    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final NotificationService notificationService;
    @Autowired
    public AccountService(BankAccountRepository bankAccountRepository,
                          CustomerRepository customerRepository,
                          TransactionHistoryRepository transactionHistoryRepository,
                          NotificationService notificationService) {

        this.bankAccountRepository = bankAccountRepository;
        this.customerRepository = customerRepository;
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.notificationService = notificationService;
    }

    public AccountTransactionResponse receivePayment(PaymentRequest paymentRequest) {
        AccountTransactionResponse accountTransactionResponse = null;

        try {
            BankAccount bankAccount = this.bankAccountRepository
                    .findByAccountNumber(paymentRequest.getAccountNumber())
                    .orElseThrow(EntityNotFoundException::new);

            /*All payments made into the Savings Account will be credited with a 0.5% interest of the current balance.*/
            final double creditAmount = 0.5 * bankAccount.getBalance().doubleValue() / 100;

            final BigDecimal newBalance = subtractFromCurrentBalance(creditAmount
                    , BigDecimal.valueOf(paymentRequest.getPaymentAmount()));

            bankAccount.setBalance(newBalance);

            this.bankAccountRepository.save(bankAccount);

            accountTransactionResponse = this.createAccountTransactionResponse(bankAccount.getAccountNumber(),
                    bankAccount.getAccountType(), bankAccount.getBalance());

            this.createTransactionHistoryEntry(TransactionType.INCOMING_PAYMENT,
                    BigDecimal.valueOf(paymentRequest.getPaymentAmount()),
                    paymentRequest.getAccountNumber(), null);

            Notification notification = NotificationHelper.buildPaymentNotification(paymentRequest,
                    this.paymentNotification,
                    this.paymentSubject);

            System.out.println("Notification: " + notification);

            this.notificationService.sendNotification(this.paymentsTopic, notification);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return accountTransactionResponse;
    }
    private AccountTransactionResponse createAccountTransactionResponse(String accountNumber,
                                                                        AccountType accountType,
                                                                        BigDecimal balance) {
        return new AccountTransactionResponse(accountNumber, accountType, balance);
    }
    public AccountTransactionResponse doTransfer(TransferRequest transferRequest) {
        BankAccount fromAccount;
        BankAccount toAccount = null;

        try {
            fromAccount = this.bankAccountRepository
                    .findByAccountNumber(transferRequest.getFromAccount())
                    .orElseThrow(EntityNotFoundException::new);

            validateBalance(fromAccount, BigDecimal.valueOf(transferRequest.getTransferAmount()));

            toAccount = this.bankAccountRepository
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

            Notification notification = NotificationHelper.buildFundTransferNotification(transferRequest,
                    this.fundTransferNotification,
                    this.transferSubject);

            System.out.println("Notification: " + notification);

            this.notificationService.sendNotification(this.transfersTopic, notification);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        AccountTransactionResponse accountTransactionResponse = null;

        try {
            BankAccount bankAccount = this.bankAccountRepository
                    .findByAccountNumber(transactionRequest.getAccountNumber())
                    .orElseThrow(EntityNotFoundException::new);

            bankAccount.setBalance(addToCurrentBalance(transactionRequest.getAmount(), bankAccount.getBalance()));
            this.bankAccountRepository.save(bankAccount);

            accountTransactionResponse = this
                    .createAccountTransactionResponse(bankAccount.getAccountNumber(),
                            bankAccount.getAccountType(),
                            bankAccount.getBalance());

            this.createTransactionHistoryEntry(TransactionType.DEBIT,
                    BigDecimal.valueOf(transactionRequest.getAmount()),
                    null, bankAccount.getAccountNumber());

            Notification notification = NotificationHelper.buildDebitNotification(transactionRequest,
                    this.debitNotification,
                    this.debitSubject);

            System.out.println("Notification: " + notification);

            this.notificationService.sendNotification(this.debitTopic, notification);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return accountTransactionResponse;
    }

    public AccountTransactionResponse creditAccount(TransactionRequest transactionRequest) {
        AccountTransactionResponse accountTransactionResponse = null;

        try {
            BankAccount bankAccount = this.bankAccountRepository
                    .findByAccountNumber(transactionRequest.getAccountNumber())
                    .orElseThrow(EntityNotFoundException::new);

            this.validateBalance(bankAccount, BigDecimal.valueOf(transactionRequest.getAmount()));

            bankAccount.setBalance(subtractFromCurrentBalance(transactionRequest.getAmount()
                    , bankAccount.getBalance()));

            this.bankAccountRepository.save(bankAccount);

            this.createTransactionHistoryEntry(TransactionType.CREDIT,
                    BigDecimal.valueOf(transactionRequest.getAmount()),
                    null, transactionRequest.getAccountNumber());

            accountTransactionResponse = createAccountTransactionResponse(
                    bankAccount.getAccountNumber(),
                    bankAccount.getAccountType(),
                    bankAccount.getBalance());

            Notification notification = NotificationHelper.buildCreditNotification(transactionRequest,
                    this.creditNotification, this.creditSubject);

            this.notificationService.sendNotification(this.creditTopic, notification);

        } catch (Exception e) {
            e.printStackTrace();
        }

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
