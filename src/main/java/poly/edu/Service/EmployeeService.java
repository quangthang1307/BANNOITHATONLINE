package poly.edu.Service;

import java.util.List;

import poly.edu.entity.Employee;

public interface EmployeeService {
    List<Employee> findAll();

    Employee findByUsername(String username);
}
