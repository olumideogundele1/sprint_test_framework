package com.olumide.springboottesting.service.implementation;

import com.olumide.springboottesting.exception.ResourceNotFoundException;
import com.olumide.springboottesting.model.Employee;
import com.olumide.springboottesting.repository.EmployeeRepository;
import com.olumide.springboottesting.service.EmployeeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.ResourceAccessException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup(){
//        employeeRepository = Mockito.mock(EmployeeRepository.class);
//        employeeService = new EmployeeServiceImpl(employeeRepository);
         employee = Employee.builder()
                .id(1L)
                .firstName("olumide")
                .lastName("dele-johnson")
                .email("olumide@gmail.com")
                .build();
    }

    @Test
    public void givenEmployeeObject_whenSaveEmployeeisCalled_thenReturnEmployeeObject(){
            //given
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);
            //when
        Employee savedEmployee = employeeService.saveEmployee(employee);

            //then
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException(){
            //given
       given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
            //when
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,() ->  {
            employeeService.saveEmployee(employee);
        });
            //then
        verify(employeeRepository,never()).save(any(Employee.class));

    }

    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnAllEmployees(){
        //given
       Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("omotara")
                .lastName("dele-johnson")
                .email("omotara@gmail.com")
                .build();
      given(employeeRepository.findAll()).willReturn(List.of(employee,employee2));
        //when
       List<Employee> employeeList = employeeService.getAllEmployees();
        //then
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.size()).isGreaterThan(0);
    }

    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeesList(){
        //given
      given(employeeRepository.findAll()).willReturn(Collections.emptyList());
        //when
      List<Employee> employeeList = employeeService.getAllEmployees();
      //then
        assertThat(employeeList).isEmpty();
    }

    @Test
    public void givenEmployeeById_whenGetEmployee_thenReturnEmployeeObject(){
        //given
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
        //when
         Employee savedEmployee =   employeeService.getById(employee.getId()).get();
        //then
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    public void givenEmployeeIdThatDoesExist_whenFindById_thenThrowException(){
        //given
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.empty());
        //when
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getById(employee.getId());
        });
        //then
        verify(employeeRepository, never()).getReferenceById(any(Long.class));
    }

    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        //given
            given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("omotara@gmail.com");
        employee.setFirstName("omotara");
        //when
           Employee updatedEmployee = employeeService.updateEmployee(employee);
        //then
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getFirstName()).isEqualTo("omotara");
    }

    @Test
    public void givenEmployeeId_whenDeleteIsCalled_thenReturnVoid(){
        //given
           willDoNothing().given(employeeRepository).deleteById(employee.getId());
        //when
          employeeService.deleteEmployee(employee.getId());
        //then
        verify(employeeRepository,times(1)).deleteById(employee.getId());
    }

}