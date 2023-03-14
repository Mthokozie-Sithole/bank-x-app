package com.banking.bankingapp.service;

import com.banking.bankingapp.exception.EntityNotFoundException;
import com.banking.bankingapp.model.constants.TransactionType;
import com.banking.bankingapp.model.dto.request.PaymentRequest;
import com.banking.bankingapp.model.dto.request.TransactionRequest;
import com.banking.bankingapp.model.dto.response.AccountTransactionResponse;
import com.banking.bankingapp.model.entities.BankAccount;
import com.banking.bankingapp.model.entities.TransactionHistory;
import com.banking.bankingapp.repository.BankAccountRepository;
import com.banking.bankingapp.repository.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionHistoryRepository transactionHistoryRepository;

    private final BankAccountRepository bankAccountRepository;

    private final AccountService accountService;

    @Autowired
    public TransactionService(TransactionHistoryRepository transactionHistoryRepository,
                              BankAccountRepository bankAccountRepository,
                              AccountService accountService) {
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.accountService = accountService;
    }

    public TransactionHistory saveTransaction(TransactionHistory transactionHistory) {
        return this.transactionHistoryRepository.save(transactionHistory);
    }

    public List<TransactionHistory> getAllTransactions() {
        return (List<TransactionHistory>) this.transactionHistoryRepository.findAll();
    }

    public void reconcileTransactions(List<TransactionRequest> transactionRequests) {
        for (TransactionRequest transactionRequest : transactionRequests) {
            this.saveTransaction(createTransactionHistory(transactionRequest));
        }
    }

    private TransactionHistory createTransactionHistory(TransactionRequest transactionRequest) {
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setTransactionAmount(BigDecimal.valueOf(transactionRequest.getAmount()));
        transactionHistory.setTransactionId(transactionRequest.getTransactionId());
        transactionHistory.setTransactionType(transactionRequest.getTransactionType());

        if (transactionRequest.getTransactionType().equals(TransactionType.CREDIT)) {
            transactionHistory.setFromAccount(transactionRequest.getAccountNumber());
        }

        if (transactionRequest.getTransactionType().equals(TransactionType.DEBIT) ||
                transactionRequest.getTransactionType().equals(TransactionType.INCOMING_PAYMENT)) {
            transactionHistory.setToAccount(transactionRequest.getAccountNumber());
        }
        return transactionHistory;
    }

    public AccountTransactionResponse processSingleTransaction(TransactionRequest transactionRequest) {
        AccountTransactionResponse accountTransactionResponse = new AccountTransactionResponse();
        BankAccount bankAccount;

        if (transactionRequest.getTransactionType().equals(TransactionType.DEBIT)) {
            bankAccount = getBankAccount(transactionRequest);
            accountTransactionResponse = accountService.debitAccount(transactionRequest);
            bankAccount.setBalance(accountTransactionResponse.getNewBalance());
        }

        if (transactionRequest.getTransactionType().equals(TransactionType.CREDIT)) {
            bankAccount = getBankAccount(transactionRequest);
            accountTransactionResponse = accountService.creditAccount(transactionRequest);
            bankAccount.setBalance(accountTransactionResponse.getNewBalance());

        }

        if (transactionRequest.getTransactionType().equals(TransactionType.INCOMING_PAYMENT)) {
            bankAccount = getBankAccount(transactionRequest);
            PaymentRequest paymentRequest = new PaymentRequest(transactionRequest.getAmount()
                    , transactionRequest.getAccountNumber(), bankAccount.getAccountType());
            accountTransactionResponse = accountService.receivePayment(paymentRequest);
            bankAccount.setBalance(accountTransactionResponse.getNewBalance());

        }

        return accountTransactionResponse;
    }

    public List<AccountTransactionResponse> processMultipleTransactions(List<TransactionRequest> transactionRequests) {
        if (!transactionRequests.isEmpty()) {
            List<AccountTransactionResponse> accountTransactionResponses = new ArrayList<>();

            for (TransactionRequest transactionRequest: transactionRequests) {
                AccountTransactionResponse accountTransactionResponse = this.processSingleTransaction(transactionRequest);
                accountTransactionResponses.add(accountTransactionResponse);
            }

            return accountTransactionResponses;
        }
        return new ArrayList<>();
    }

    private BankAccount getBankAccount(TransactionRequest transactionRequest) {
        return this.bankAccountRepository
                .findByAccountNumber(transactionRequest.getAccountNumber())
                .orElseThrow(EntityNotFoundException::new);
    }
}
