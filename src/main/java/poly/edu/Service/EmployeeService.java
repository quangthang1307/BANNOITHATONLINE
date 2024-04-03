package poly.edu.Service;

import org.springframework.stereotype.Service;

import poly.edu.entity.Employee;

@Service
public interface EmployeeService {
    
    Employee findByUsername(String username);
}
