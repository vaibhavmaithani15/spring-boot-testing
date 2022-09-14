package com.junit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junit.model.Employee;
import com.junit.servcie.EmployeeService;

import static org.hamcrest.CoreMatchers.is;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

@WebMvcTest
public class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private EmployeeService employeeService;


    //    Junit test for create employee method REST API
    @Test
    @DisplayName(" Junit test for create employee method REST API")
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        //given- precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Vaibhav")
                .lastName("Maithani")
                .email("vaibhav.maithani1@gmail.com")
                .build();

        given(employeeService
                .saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee1)));

        //then- verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee1.getLastName())))
                .andExpect(jsonPath("$.email", is(employee1.getEmail())));
    }

    //    Junit test for get all employees REST Api
    @Test
    @DisplayName(" Junit test for get all employees REST Api")
    public void givenListOfEmployee_whenGetEmployeeList_thenReturnEmployeeList() throws Exception {

        //given- precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Vaibhav")
                .lastName("Maithani")
                .email("vaibhav.maithani1@gmail.com")
                .build();
        Employee employee2 = Employee.builder()
                .firstName("Rahul")
                .lastName("Maithani")
                .email("rahul.maithani1@gmail.com")
                .build();

        List<Employee> listOfEmployees = List.of(employee1, employee2);
        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);


        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then- verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
    }


    //Positive Scenerio
    //    Junit test for GET employee by id REST API
    @Test
    @DisplayName(" Junit test for GET employee by id REST API Positive Scenario")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        Long employeeId = 1L;
        //given- precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Vaibhav")
                .lastName("Maithani")
                .email("vaibhav.maithani1@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee1));

        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then- verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee1.getLastName())))
                .andExpect(jsonPath("$.email", is(employee1.getEmail())));
    }

    //Negative Scenerio
    //    Junit test for GET employee by id REST API
    @Test
    @DisplayName(" Junit test for GET employee by id REST API negative scenario")
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {

        Long employeeId = 1L;
        //given- precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Vaibhav")
                .lastName("Maithani")
                .email("vaibhav.maithani1@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then- verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    //    Positive Scenario
    //    Junit test for update employee REST API
    @Test
    @DisplayName(" Junit test for update employee REST API Postive Scenario")
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {

        Long employeeId = 1L;
        //given- precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Vaibhav")
                .lastName("Maithani")
                .email("vaibhav.maithani1@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Rahul")
                .lastName("Maithani")
                .email("rahul.maithani1@gmail.com")
                .build();


        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));

        given(employeeService
                .updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then- verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));

    }


    //    Negative Scenario
    //    Junit test for update employee REST API
    @Test
    @DisplayName(" Junit test for update employee REST API Negative Scenario")
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {

        Long employeeId = 1L;
        //given- precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Vaibhav")
                .lastName("Maithani")
                .email("vaibhav.maithani1@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Rahul")
                .lastName("Maithani")
                .email("rahul.maithani1@gmail.com")
                .build();


        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        given(employeeService
                .updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then- verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    //    Junit test for delete employee REST API
        @Test
        @DisplayName(" Junit test for")
        public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {

        Long employeeId=1L;
            //given- precondition or setup
            willDoNothing().given(employeeService).deleteEmployee(employeeId);

            //when - action or the behaviour that we are going to test
            ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

            //then- verify the output
            response.andExpect(status().isOk())
                    .andDo(print());
        }


}
