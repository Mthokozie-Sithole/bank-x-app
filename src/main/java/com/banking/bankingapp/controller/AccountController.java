package com.banking.bankingapp.controller;

import com.banking.bankingapp.exception.EntityNotFoundException;
import com.banking.bankingapp.exception.InsufficientFundsException;
import com.banking.bankingapp.model.dto.request.PaymentRequest;
import com.banking.bankingapp.model.dto.request.TransactionRequest;
import com.banking.bankingapp.model.dto.request.TransferRequest;
import com.banking.bankingapp.model.dto.response.AccountTransactionResponse;
import com.banking.bankingapp.service.AccountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<AccountTransactionResponse> transferFromSavings(@RequestBody TransferRequest transferRequest) {
        try {
            return new ResponseEntity<>(this.accountService.doTransfer(transferRequest), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InsufficientFundsException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/pay")
    public ResponseEntity<AccountTransactionResponse> receivePayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            return new ResponseEntity<>(this.accountService.receivePayment(paymentRequest), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/debit")
    @ApiOperation(value = "Debit account", response = AccountTransactionResponse.class)
    public ResponseEntity<AccountTransactionResponse> debitAccount(@RequestBody TransactionRequest transactionRequest) {
        try {
            return new ResponseEntity<>(this.accountService.debitAccount(transactionRequest), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/credit")
    public ResponseEntity<AccountTransactionResponse> creditAccount(@RequestBody TransactionRequest transactionRequest) {
        try {
            return new ResponseEntity<>(this.accountService.creditAccount(transactionRequest), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InsufficientFundsException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
