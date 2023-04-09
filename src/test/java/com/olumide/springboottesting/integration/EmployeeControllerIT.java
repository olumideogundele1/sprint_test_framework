package com.olumide.springboottesting.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olumide.springboottesting.model.Employee;
import com.olumide.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIT extends AbstractionBaseTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        employeeRepository.deleteAll();
    }

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("olumide")
                .lastName("dele-johnson")
                .email("olumide@gmail.com")
                .build();

        //when
        ResultActions response =  mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //then
        response.andDo(print()).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", is(employee.getLastName())));
    }

    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeeList() throws Exception{
        //given
        List<Employee> list = new ArrayList<>();
        list.add(Employee.builder()
                .firstName("olumide")
                .lastName("dele-johnson")
                .email("olumide@gmail.com")
                .build());
        list.add(Employee.builder()
                .firstName("omotara")
                .lastName("dele-johnson")
                .email("omotara@gmail.com")
                .build());
        employeeRepository.saveAll(list);
        //when
        ResultActions response = mockMvc.perform(get("/api/employees"));
        //then
        response.andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", is(list.size())));
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeId_thenReturnEmployeeObject() throws Exception{
        //given
        Employee employee = Employee.builder()
                .firstName("olumide")
                .lastName("dele-johnson")
                .email("olumide@gmail.com")
                .build();
        Employee savedEmployee = employeeRepository.save(employee);
        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", savedEmployee.getId()));
        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())));
    }

    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeId_thenReturnEmployeeObject() throws Exception{
        //given
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("olumide")
                .lastName("dele-johnson")
                .email("olumide@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenEmployeeId_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception{
        //given
        Employee employee = Employee.builder()
                .firstName("olumide")
                .lastName("dele-johnson")
                .email("olumide@gmail.com")
                .build();
        employeeRepository.save(employee);
        Employee updatedEmployee = Employee.builder()
                .firstName("omotara")
                .lastName("dele-johnson")
                .email("omotara@gmail.com")
                .build();

        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        //then
        response.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())));

    }

    @Test
    public void givenInvalidId_whenUpdateEmployee_thenThrowException() throws Exception{
        //given
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("olumide")
                .lastName("dele-johnson")
                .email("olumide@gmail.com")
                .build();
        employeeRepository.save(employee);
        Employee updatedEmployee = Employee.builder()
                .firstName("omotara")
                .lastName("dele-johnson")
                .email("omotara@gmail.com")
                .build();

        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        //then
        response.andExpect(status().isNotFound()).andDo(print());

    }

    @Test
    public void givenEmployeeIId_whenDeleteEmployee_thenReturnSuccessfulMessage() throws Exception {
        //given
        Employee employee = Employee.builder()
                .firstName("olumide")
                .lastName("dele-johnson")
                .email("olumide@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/employees/{id}",employee.getId()));
        //then
        resultActions.andExpect(status().isOk()).andDo(print());
    }
}
