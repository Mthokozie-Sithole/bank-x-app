package com.banking.bankingapp.controller;

import com.banking.bankingapp.exception.EntityNotFoundException;
import com.banking.bankingapp.model.dto.request.ThirdPartyTransaction;
import com.banking.bankingapp.model.dto.request.TransactionRequest;
import com.banking.bankingapp.model.dto.response.AccountTransactionResponse;
import com.banking.bankingapp.model.entities.TransactionHistory;
import com.banking.bankingapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/history")
    public ResponseEntity<List<TransactionHistory>> getTransactionHistory() {
        return new ResponseEntity<>(this.transactionService.getAllTransactions(), HttpStatus.OK);
    }

    /*Bank Z should be able to send Bank X a list of transactions that they processed on behalf of Bank X at the end of the business day for reconciliation*/
    @PostMapping("/reconcile-transactions")
    public void reconcileTransactions(@RequestBody ThirdPartyTransaction thirdPartyTransaction) {
        this.transactionService.reconcileTransactions(thirdPartyTransaction.getTransactionRequests());
    }

    @PostMapping("/process-transaction")
    public ResponseEntity<AccountTransactionResponse> processSingleTransaction(@RequestBody TransactionRequest transactionRequest) {
        try {
            return new ResponseEntity<>(this.transactionService.processSingleTransaction(transactionRequest), HttpStatus.OK);
        }  catch (EntityNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/process-multi-transaction")
    public ResponseEntity<List<AccountTransactionResponse>> processMultiTransaction(@RequestBody List<TransactionRequest> transactionRequests) {
        try {
            return new ResponseEntity<>(this.transactionService.processMultipleTransactions(transactionRequests), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
