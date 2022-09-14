package com.junit.service;


import com.junit.exception.ResourceAlreadyExistsException;
import com.junit.exception.ResourceNotFoundException;
import com.junit.model.Employee;
import com.junit.repository.EmployeeRepository;
import com.junit.servcie.impl.EmployeeServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    Employee employee1;

    @BeforeEach
    public void setup() {
        employee1 = Employee.builder()
                .id(1L)
                .firstName("Vaibhav")
                .lastName("Maithani")
                .email("vaibhav.maithani1@gmail.com")
                .build();
    }

    //    Junit test for savedEmployee method
    @Test
    @DisplayName(" Junit test for saveEmployeeMethod")
    public void givenEmployeeObject_whenSaveEmployee_thenEmployeeObject() {

        //given- precondition or setup

        given(employeeRepository.findByEmail(employee1.getEmail()))//with this email nothing found in database
                .willReturn(Optional.empty());

        given(employeeRepository.save(employee1)).willReturn(employee1);


        //when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeService.saveEmployee(employee1);

        //then- verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //    Junit test for savedEmployee method which throws Exception
    @Test
    @DisplayName(" Junit test for saveEmployeeMethod which throws exception")
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {

        //given- precondition or setup

        given(employeeRepository.findByEmail(employee1.getEmail())) //this email already found in the database
                .willReturn(Optional.of(employee1));


        //when - action or the behaviour that we are going to test
        Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> {
            Employee savedEmployee = employeeService.saveEmployee(employee1);
        });


        //then- verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }


    //    Junit test for getAllEmployees method
        @Test
        @DisplayName(" Junit test for getAllEmployees method")
        public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeesList(){

            Employee employee2 = Employee.builder()
                    .id(2L)
                    .firstName("Rahul")
                    .lastName("Maithani")
                    .email("rahul.maithani1@gmail.com")
                    .build();
            //given- precondition or setup
            given(employeeRepository.findAll()).willReturn(List.of(employee1,employee2));

            //when - action or the behaviour that we are going to test
            List<Employee> employeeList=employeeService.getAllEmployees();

            //then- verify the output
            assertThat(employeeList).isNotNull();
            assertThat(employeeList.size()).isEqualTo(2);
        }

    //    Junit test for getAllEmployees method
    @Test
    @DisplayName(" Junit test for getAllEmployees method (negative scenario)")
    public void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeesList(){

        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("Rahul")
                .lastName("Maithani")
                .email("rahul.maithani1@gmail.com")
                .build();
        //given- precondition or setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when - action or the behaviour that we are going to test
        List<Employee> employeeList=employeeService.getAllEmployees();

        //then- verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    //    Junit test for getEmployeeById method
        @Test
        @DisplayName(" Junit test for getEmployeeById method")
        public void givenEmployeeId_whenGetEmployeeById_thenEmployeeObject(){

            //given- precondition or setup
            given(employeeRepository.findById(employee1.getId())).willReturn(Optional.of(employee1));

            //when - action or the behaviour that we are going to test
            Employee savedEmployee = employeeService.getEmployeeById(employee1.getId()).get();

            //then- verify the output
            assertThat(savedEmployee).isNotNull();
        }

    //    Junit test for findById method which throws Exception

        @Test
        @DisplayName(" Junit test for findById method which throws Exception")
        public void givenInvalidEmployeeId_whenFindByEmployeeId_thenThrowsException(){

            //given- precondition or setup
            given(employeeRepository.findById(4L)).willReturn(Optional.empty());

            //when - action or the behaviour that we are going to test
            Assertions.assertThrows(ResourceNotFoundException.class, () -> {
//                Employee savedEmployee = employeeService.saveEmployee(employee1);
                Employee savedEmployee = employeeService.getEmployeeById(4L).get();
            });


            //then- verify the output
            verify(employeeRepository, never()).findById(employee1.getId());

        }

        //    Junit test for updateEmployeeMethod
            @Test
            @DisplayName(" Junit test for updateEmployee Method")
            public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){

                //given- precondition or setup
                given(employeeRepository.save(employee1)).willReturn(employee1);
                employee1.setFirstName("Rahul");
                employee1.setEmail("rahul.maithani1@gmail.com");

                //when - action or the behaviour that we are going to test
                Employee updatedEmployee = employeeService.updateEmployee(employee1);


                //then- verify the output
                assertThat(updatedEmployee.getFirstName()).isEqualTo("Rahul");
                assertThat(updatedEmployee.getEmail()).isEqualTo("rahul.maithani1@gmail.com");
            }

            //    Junit test for deleteEmployee method
                @Test
                @DisplayName(" Junit test for deleteEmployee method")
                public void givenEmployeeId_whenDeleteEmployee_thenReturnNothing(){

                    //given- precondition or setup
                    willDoNothing().given(employeeRepository).deleteById(1L);

                    //when - action or the behaviour that we are going to test
                    employeeService.deleteEmployee(1L);

                    //then- verify the output
                    verify(employeeRepository,times(1)).deleteById(1L);
                }

}
