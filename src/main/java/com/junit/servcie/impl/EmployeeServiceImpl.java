package com.junit.servcie.impl;

import com.junit.exception.ResourceAlreadyExistsException;
import com.junit.exception.ResourceNotFoundException;
import com.junit.model.Employee;
import com.junit.repository.EmployeeRepository;
import com.junit.servcie.EmployeeService;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository=employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if(savedEmployee.isPresent()){
            throw new ResourceAlreadyExistsException("Employee already exit with given email: "+employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        Optional<Employee> savedEmployee = employeeRepository.findById(id);
        if(savedEmployee.isEmpty()){
            throw new ResourceNotFoundException("Employee not found with given id: "+id);
        }
        return savedEmployee;
    }

    @Override
    public Employee updateEmployee(Employee updatedEmployee) {
        return employeeRepository.save(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
