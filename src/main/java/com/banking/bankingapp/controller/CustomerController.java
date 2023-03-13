package com.banking.bankingapp.controller;

import com.banking.bankingapp.model.BankAccount;
import com.banking.bankingapp.model.Customer;
import com.banking.bankingapp.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@Api(value="banking-application", description="Operations pertaining to customers in the banking application")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/onboardCustomer")
    @ApiOperation(value = "Onboard new customer",response = Customer.class)
    public ResponseEntity<Customer> onboardCustomer(@RequestBody Customer customer) {
        return new ResponseEntity<>(this.customerService.onboardCustomer(customer), HttpStatus.OK);
    }

    @GetMapping("/retrieve/{id}")
    @ApiOperation(value = "Retrieve customer by ID",response = Customer.class)
    public ResponseEntity<Customer> retrieveCustomerById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(this.customerService.findCustomerById(id).orElse(null), HttpStatus.OK);
    }

    @GetMapping("/retrieveAllCustomers")
    @ApiOperation(value = "Retrieve all customers",response = Customer.class)
    public ResponseEntity<List<Customer>> retrieveAllCustomers() {
        return new ResponseEntity<>(this.customerService.findAllCustomers(), HttpStatus.OK);
    }
}
