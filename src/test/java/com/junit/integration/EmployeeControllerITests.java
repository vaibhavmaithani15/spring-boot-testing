package com.junit.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junit.exception.ResourceNotFoundException;
import com.junit.model.Employee;
import com.junit.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.NestedServletException;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(){
        employeeRepository.deleteAll();
    }


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
        employeeRepository.saveAll(listOfEmployees);


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

//        Long employeeId = 1L;
        //given- precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Vaibhav")
                .lastName("Maithani")
                .email("vaibhav.maithani1@gmail.com")
                .build();

        Employee savedEmployee = employeeRepository.save(employee1);


        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", savedEmployee.getId()));

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

        employeeRepository.save(employee1);

        //when - action or the behaviour that we are going to test
        Assertions.assertThrows(Exception.class, () -> {
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
        response.andExpect(status().isInternalServerError())
                .andDo(print());
         });

        //then- verify the output
    }

    //    Positive Scenario
    //    Junit test for update employee REST API
    @Test
    @DisplayName(" Junit test for update employee REST API Postive Scenario")
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {

             //given- precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Vaibhav")
                .lastName("Maithani")
                .email("vaibhav.maithani1@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Rahul")
                .lastName("Maithani")
                .email("rahul.maithani1@gmail.com")
                .build();




        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then- verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));

    }

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

        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Rahul")
                .lastName("Maithani")
                .email("rahul.maithani1@gmail.com")
                .build();



        //when - action or the behaviour that we are going to test
        Assertions.assertThrows(Exception.class, () -> {

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        });
        //then- verify the output


    }

    //    Junit test for delete employee REST API
    @Test
    @DisplayName(" Junit test for")
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {


        //given- precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Vaibhav")
                .lastName("Maithani")
                .email("vaibhav.maithani1@gmail.com")
                .build();
        employeeRepository.save(employee1);

        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employee1.getId()));

        //then- verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }

}
