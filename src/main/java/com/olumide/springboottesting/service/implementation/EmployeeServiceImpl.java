package com.olumide.springboottesting.service.implementation;

import com.olumide.springboottesting.exception.ResourceNotFoundException;
import com.olumide.springboottesting.model.Employee;
import com.olumide.springboottesting.repository.EmployeeRepository;
import com.olumide.springboottesting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {


     EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> employeeOptional = employeeRepository.findByEmail(employee.getEmail());
        if(employeeOptional.isPresent()){
            throw new ResourceNotFoundException(String.format("Employee already exist with given email %s",employeeOptional.get().getEmail()));
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getById(Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
//        if(employeeOptional.isEmpty()){
//            throw new ResourceNotFoundException("Employee does not exist");
//        }
        return employeeOptional;
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
