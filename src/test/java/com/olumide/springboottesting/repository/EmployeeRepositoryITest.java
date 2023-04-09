package com.olumide.springboottesting.repository;

import com.olumide.springboottesting.integration.AbstractionBaseTest;
import com.olumide.springboottesting.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryITest extends AbstractionBaseTest {

    @Autowired
    EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp(){
        employee = Employee.builder()
                .firstName("olumide")
                .lastName("dele-johnson")
                .email("olumide@gmail.com")
                .build();
    }

    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){
        //given
//        Employee employee = Employee.builder()
//                .firstName("olumide")
//                .lastName("dele-johnson")
//                .email("olumide@gmail.com")
//                .build();
        //when
        Employee savedEmployee = employeeRepository.save(employee);
        //then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @DisplayName("return employee list")
    @Test
    public void givenEmployees_whenFindAllIsCalled_thenReturnEmployeeList(){
        //given
//        Employee employee = Employee.builder()
//                .firstName("olumide")
//                .lastName("dele-johnson")
//                .email("olumide@gmail.com")
//                .build();
        Employee employee2 = Employee.builder()
                .firstName("bbankioluwa")
                .lastName("dele-johnson")
                .email("banki@gmail.com")
                .build();
        Employee employee3 = Employee.builder()
                .firstName("omotara")
                .lastName("dele-johnson")
                .email("omotarae@gmail.com")
                .build();
        employeeRepository.save(employee);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);
        //when
        List<Employee> list = employeeRepository.findAll();
        //then
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(3);
    }

    @Test
        public void givenEmployee_whenFindyIDIsCalled_thenReturnEmployeeOject(){
            //given
//        Employee employee = Employee.builder()
//                .firstName("olumide")
//                .lastName("dele-johnson")
//                .email("olumide@gmail.com")
//                .build();
        employeeRepository.save(employee);
        //when
         Optional<Employee> savedEmployee = employeeRepository.findById(employee.getId());
            //then
        assertThat(savedEmployee.get()).isNotNull();
    }

    @Test
        public void givenEmployeeEmail_whenFindyEmailIsCalled_thenReturnEmployee(){
            //given
//        Employee employee = Employee.builder()
//                .firstName("olumide")
//                .lastName("dele-johnson")
//                .email("olumide@gmail.com")
//                .build();
        employeeRepository.save(employee);
            //when
        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        //then
        assertThat(savedEmployee.get()).isNotNull();
        assertThat(savedEmployee.get().getEmail()).isEqualTo("olumide@gmail.com");
    }

    @Test
    public void givenEmployeeId_whenUpdateEmployeeIsCalled_thenReturnUpdatedEmployee(){
        //given
//        Employee employee = Employee.builder()
//                .firstName("olumide")
//                .lastName("dele-johnson")
//                .email("olumide@gmail.com")
//                .build();
        employeeRepository.save(employee);

        //when
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setFirstName("omotara");
        savedEmployee.setEmail("omtara@gmail.com");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //then
        assertThat(updatedEmployee.getEmail()).isEqualTo("omtara@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("omotara");
    }

    @Test
        public void givenEmployeeId_whenDeleteIsCalled_thenRemoveEmployeeObject(){
        //given
//        Employee employee = Employee.builder()
//                .firstName("olumide")
//                .lastName("dele-johnson")
//                .email("olumide@gmail.com")
//                .build();
        employeeRepository.save(employee);
        //when
        employeeRepository.delete(employee);
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());
        //then
        assertThat(employeeOptional).isEmpty();
        }
}