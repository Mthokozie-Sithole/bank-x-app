package com.banking.bankingapp.controller;

import com.banking.bankingapp.model.*;
import com.banking.bankingapp.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@Api(value="banking-application", description="Operations pertaining to accounts in the banking application")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/transfer")
    @ApiOperation(value = "Transfer funds to an account",response = Customer.class)
    public ResponseEntity<Customer> transferFromSavings(@RequestBody TransferRequest transferRequest){
       return new ResponseEntity<>(this.accountService.doTransfer(transferRequest), HttpStatus.OK);
    }

    @PostMapping("/pay")
    @ApiOperation(value = "Receive payment from third party",response = Customer.class)
    public ResponseEntity<BankAccount> receivePayment(@RequestBody PaymentRequest paymentRequest) {
        return new ResponseEntity<>(this.accountService.receivePayment(paymentRequest), HttpStatus.OK);
    }

    @PostMapping("/debit")
    @ApiOperation(value = "Debit account",response = BankAccount.class)
    public ResponseEntity<BankAccount> debitAccount(@RequestBody AccountRequest accountRequest) {
        return new ResponseEntity<>(this.accountService.debitAccount(accountRequest), HttpStatus.OK);
    }

    @PostMapping("/credit")
    @ApiOperation(value = "Credit account",response = BankAccount.class)
    public ResponseEntity<BankAccount> creditAccount(@RequestBody AccountRequest accountRequest) {
        return new ResponseEntity<>(this.accountService.creditAccount(accountRequest), HttpStatus.OK);
    }
}
