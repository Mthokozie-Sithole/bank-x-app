package com.banking.bankingapp.service;

import com.banking.bankingapp.model.Employee;
import com.banking.bankingapp.model.EmployeePage;
import com.banking.bankingapp.model.EmployeeSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.banking.bankingapp.repository.EmployeeCriteriaRepository;
import com.banking.bankingapp.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeCriteriaRepository employeeCriteriaRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeCriteriaRepository employeeCriteriaRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeCriteriaRepository = employeeCriteriaRepository;
    }

    public Page<Employee> getEmployees (EmployeePage employeePage,
                                        EmployeeSearchCriteria employeeSearchCriteria){
        return employeeCriteriaRepository.findAllWithFilters(employeePage, employeeSearchCriteria);
    }

    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
}
