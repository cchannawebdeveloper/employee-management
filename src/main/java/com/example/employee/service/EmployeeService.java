package com.example.employee.service;

import com.example.employee.dto.EmployeeDTO;
import com.example.employee.entity.Employee;
import com.example.employee.exception.DuplicateEmailException;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.util.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        log.debug("Fetching all employees");
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(Long id) {
        log.debug("Fetching employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return employeeMapper.toDTO(employee);
    }

    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        log.debug("Creating new employee with email: {}", employeeDTO.getEmail());

        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + employeeDTO.getEmail());
        }

        Employee employee = employeeMapper.toEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with id: {}", savedEmployee.getId());

        return employeeMapper.toDTO(savedEmployee);
    }

    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        log.debug("Updating employee with id: {}", id);

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        // Check if email is being changed and if new email already exists
        if (!existingEmployee.getEmail().equals(employeeDTO.getEmail())
                && employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + employeeDTO.getEmail());
        }

        existingEmployee.setName(employeeDTO.getName());
        existingEmployee.setEmail(employeeDTO.getEmail());
        existingEmployee.setRole(employeeDTO.getRole());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        log.info("Employee updated successfully with id: {}", id);

        return employeeMapper.toDTO(updatedEmployee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        log.debug("Deleting employee with id: {}", id);

        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }

        employeeRepository.deleteById(id);
        log.info("Employee deleted successfully with id: {}", id);
    }
}