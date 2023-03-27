package com.banking.bankingapp.controller;

import com.banking.bankingapp.model.entities.Customer;
import com.banking.bankingapp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/onboardCustomer")
    public ResponseEntity<Customer> onboardCustomer(@RequestBody Customer customer) {
        return new ResponseEntity<>(this.customerService.onboardCustomer(customer), HttpStatus.CREATED);
    }

    @GetMapping("/retrieve/{id}")
    public ResponseEntity<Customer> retrieveCustomerById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(this.customerService.findCustomerById(id).orElse(null), HttpStatus.OK);
    }

    @GetMapping("/retrieveAllCustomers")
    public ResponseEntity<List<Customer>> retrieveAllCustomers() {
        return new ResponseEntity<>(this.customerService.findAllCustomers(), HttpStatus.OK);
    }
}
