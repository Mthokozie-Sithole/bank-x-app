package com.banking.bankingapp.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import com.banking.bankingapp.model.Employee;
import com.banking.bankingapp.model.EmployeePage;
import com.banking.bankingapp.model.EmployeeSearchCriteria;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class EmployeeCriteriaRepository  {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public EmployeeCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Employee> findAllWithFilters(EmployeePage employeePage,
                                             EmployeeSearchCriteria employeeSearchCriteria){

        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        Predicate predicate = this.getPredicate(employeeSearchCriteria, employeeRoot);
        criteriaQuery.where(predicate);
        orderResults(employeePage, criteriaQuery, employeeRoot);

        TypedQuery<Employee> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(employeePage.getPageNumber() * employeePage.getPageSize());
        typedQuery.setMaxResults(employeePage.getPageSize());

        Pageable pageable = this.getPageable(employeePage);

        long employeesCount = this.getEmployeeCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, employeesCount);

    }

    private long getEmployeeCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Employee> countRoot = countQuery.from(Employee.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private Pageable getPageable(EmployeePage employeePage) {
        Sort sort = Sort.by(employeePage.getSortDirection(), employeePage.getSortBy());
        return PageRequest.of(employeePage.getPageNumber(), employeePage.getPageSize(), sort);
    }

    private void orderResults(EmployeePage employeePage,
                              CriteriaQuery<Employee> criteriaQuery,
                              Root<Employee> employeeRoot) {

        if (employeePage.getSortDirection().isAscending()) {
            criteriaQuery.orderBy(criteriaBuilder.asc(employeeRoot.get(employeePage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(employeeRoot.get(employeePage.getSortBy())));
        }
    }

    private Predicate getPredicate(EmployeeSearchCriteria employeeSearchCriteria,
                                   Root<Employee> employeeRoot) {

        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(employeeSearchCriteria.getFirstName())) {
            predicates.add(criteriaBuilder.like(employeeRoot
                    .get("firstName"),
                    "%" + employeeSearchCriteria.getFirstName() + "%"));
        }

        if (Objects.nonNull(employeeSearchCriteria.getLastName())) {
            predicates.add(criteriaBuilder.like(employeeRoot
                            .get("lastName"),
                    "%" + employeeSearchCriteria.getLastName() + "%"));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
