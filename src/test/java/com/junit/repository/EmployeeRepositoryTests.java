package com.junit.repository;

import com.junit.model.Employee;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee1;

    @BeforeEach
    public void setup(){
         employee1 = Employee.builder()
                .firstName("Vaibhav")
                .lastName("Maithani")
                .email("vaibhav.maithani1@gmail.com")
                .build();
         employeeRepository.save(employee1);

    }

    // Junit test for save employee operation
    @Test
    @DisplayName("Junit test for save employee operation")
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        //given- precondition or setup

        //when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee1);

        //then- verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    //    Junit test  to get all employees List operation
    @Test
    @DisplayName("Junit test  to get all employees List operation")
    public void givenEmployeeList_whenFindAll_thenEmployeeList() {

        //given- precondition or setup

        employeeRepository.save(employee1);

        //when - action or the behaviour that we are going to test
        List<Employee> employeeList = employeeRepository.findAll();

        //then- verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).size().isEqualTo(  1);
    }

    //    Junit test for get employee by id operation
    @Test
    @DisplayName("Junit test for get employee by id operation")
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {

        //given- precondition or setup

        //when - action or the behaviour that we are going to test
        Employee employeeDB = employeeRepository.findById(employee1.getId()).get();

        //then- verify the output
        assertThat(employeeDB).isNotNull();
    }

    //    Junit test for get employee by email operation
    @Test
    @DisplayName("Junit test for get employee by email operation")
    public void givenEmployeeEmail_whenFindByEmial_thenReturnEmployeeObject() {

        //given- precondition or setup


        //when - action or the behaviour that we are going to test
        Employee employeeDB = employeeRepository.findByEmail(employee1.getEmail()).get();

        //then- verify the output
        assertThat(employeeDB).isNotNull();
    }

    //    Junit test for update employee operation
    @Test
    @DisplayName(" Junit test for update employee operation")
    public void givenEmployeeObject_whenUpdateEmployee_thenUpdatedEmployee() {

        //given- precondition or setup

        //when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findById(employee1.getId()).get();
        savedEmployee.setEmail("vaibh.maithani1@gmial.com");
        savedEmployee.setFirstName("vaibh");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //then- verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("vaibh.maithani1@gmial.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("vaibh");
    }

    //    Junit test for delete employee operation
    @Test
    @DisplayName(" Junit test for delete operation")
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {

        //given- precondition or setup

        //when - action or the behaviour that we are going to test
        employeeRepository.deleteById(employee1.getId());
        Optional<Employee> deletedEmployee = employeeRepository.findById(employee1.getId());

        //then- verify the output
        assertThat(deletedEmployee).isEmpty();

    }

    //    Junit test for custom query using JPQL with index
    @Test
    @DisplayName("Junit test for custom query using JPQL with index")
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {

        //given- precondition or setup

        String firstName="Vaibhav";
        String lastName="Maithani";


        //when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByJPQL(firstName, lastName);

        //then- verify the output
        assertThat(savedEmployee);

    }


    //    Junit test for custom query using JPQL with named params
    @Test
    @DisplayName("Junit test for custom query using JPQL with named params")
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {

        //given- precondition or setup

        String firstName="Vaibhav";
        String lastName="Maithani";


        //when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        //then- verify the output
        assertThat(savedEmployee);

    }


    //    Junit test for custom query using Native sql  with index
    @Test
    @DisplayName("Junit test for custom query using Native SQL with index")
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {

        //given- precondition or setup

        String firstName="Vaibhav";
        String lastName="Maithani";


        //when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByNativeSQL(firstName, lastName);

        //then- verify the output
        assertThat(savedEmployee);

    }

    //    Junit test for custom query using Native sql  with param
    @Test
    @DisplayName("Junit test for custom query using Native SQL with params")
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {

        //given- precondition or setup

        String firstName="Vaibhav";
        String lastName="Maithani";


        //when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByNativeSQLNamedParams(firstName, lastName);

        //then- verify the output
        assertThat(savedEmployee);

    }




}
