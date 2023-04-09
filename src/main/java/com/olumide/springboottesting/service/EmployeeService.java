package com.olumide.springboottesting.service;

import com.olumide.springboottesting.exception.ResourceNotFoundException;
import com.olumide.springboottesting.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    public Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Optional<Employee> getById(Long id);
    Employee updateEmployee(Employee employee);
    void deleteEmployee(Long id);
}
