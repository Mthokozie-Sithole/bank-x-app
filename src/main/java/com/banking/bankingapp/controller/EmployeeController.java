package com.banking.bankingapp.controller;

import com.banking.bankingapp.model.Employee;
import com.banking.bankingapp.model.EmployeePage;
import com.banking.bankingapp.model.EmployeeSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.banking.bankingapp.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/getEmployees")
    public ResponseEntity<Page<Employee>> getEmployees(EmployeePage employeePage,
                                             EmployeeSearchCriteria employeeSearchCriteria) {
        return new ResponseEntity<>(this.employeeService
                .getEmployees(employeePage, employeeSearchCriteria), HttpStatus.OK);
    }

    @PostMapping("/addEmployee")
    public ResponseEntity<Employee> addEmployee (@RequestBody Employee employee) {
        return new ResponseEntity<>(this.employeeService.addEmployee(employee), HttpStatus.OK);
    }
}
