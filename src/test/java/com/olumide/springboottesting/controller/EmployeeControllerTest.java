package com.olumide.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olumide.springboottesting.model.Employee;
import com.olumide.springboottesting.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
        public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
            //given
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("olumide")
                .lastName("dele-johnson")
                .email("olumide@gmail.com")
                .build();
        given(employeeService.saveEmployee(any(Employee.class))).willAnswer((i)-> i.getArgument(0));

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
                  .id(1L)
                  .firstName("olumide")
                  .lastName("dele-johnson")
                  .email("olumide@gmail.com")
                  .build());
        list.add(Employee.builder()
                .id(2L)
                .firstName("omotara")
                .lastName("dele-johnson")
                .email("omotara@gmail.com")
                .build());
        given(employeeService.getAllEmployees()).willReturn(list);
            //when
          ResultActions response = mockMvc.perform(get("/api/employees"));
            //then
        response.andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", is(list.size())));
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeId_thenReturnEmployeeObject() throws Exception{
            //given
        long employeeId = 1L;
          Employee employee = Employee.builder()
                  .id(1L)
                  .firstName("olumide")
                  .lastName("dele-johnson")
                  .email("olumide@gmail.com")
                  .build();
          given(employeeService.getById(employeeId)).willReturn(Optional.of(employee));
            //when
           ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
            //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())));
    }

    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeId_thenReturnEmployeeObject() throws Exception{
        //given
        long employeeId = 2L;
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("olumide")
                .lastName("dele-johnson")
                .email("olumide@gmail.com")
                .build();
        given(employeeService.getById(employeeId)).willReturn(Optional.empty());
        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenEmployeeId_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception{
            //given
      long employeeId = 1L;
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("olumide")
                .lastName("dele-johnson")
                .email("olumide@gmail.com")
                .build();
        Employee updatedEmployee = Employee.builder()
                .id(1L)
                .firstName("omotara")
                .lastName("dele-johnson")
                .email("omotara@gmail.com")
                .build();
        given(employeeService.getById(employeeId)).willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer(i -> i.getArgument(0));
            //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",employeeId)
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
                .id(1L)
                .firstName("olumide")
                .lastName("dele-johnson")
                .email("olumide@gmail.com")
                .build();
        Employee updatedEmployee = Employee.builder()
                .id(1L)
                .firstName("omotara")
                .lastName("dele-johnson")
                .email("omotara@gmail.com")
                .build();
        given(employeeService.getById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer(i -> i.getArgument(0));
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
       long employeeId = 1L;
       willDoNothing().given(employeeService).deleteEmployee(employeeId);
            //when
        ResultActions resultActions = mockMvc.perform(delete("/api/employees/{id}",employeeId));
            //then
        resultActions.andExpect(status().isOk()).andDo(print());
    }
}