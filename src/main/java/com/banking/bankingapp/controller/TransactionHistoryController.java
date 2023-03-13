package com.banking.bankingapp.controller;

import com.banking.bankingapp.model.TransactionHistory;
import com.banking.bankingapp.service.TransactionHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transaction")
@Api(value="banking-application", description="Operations pertaining to transaction history in the banking application")
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;

    @Autowired
    public TransactionHistoryController(TransactionHistoryService transactionHistoryService) {
        this.transactionHistoryService = transactionHistoryService;
    }

    @GetMapping("/history")
    @ApiOperation(value = "Retrieve a transaction history ",response = TransactionHistory.class)
    public ResponseEntity<List<TransactionHistory>> getTransactionHistory() {
        return new ResponseEntity<>(this.transactionHistoryService.getAllTransactions(), HttpStatus.OK);
    }
}
