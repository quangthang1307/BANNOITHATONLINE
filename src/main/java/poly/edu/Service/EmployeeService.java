package poly.edu.Service;

import java.util.List;

import poly.edu.entity.Employee;

public interface EmployeeService {
    List<Employee> findAll();

    Employee findByUsername(String username);

    public Employee findById(Integer employeeID);

    public Employee create(Employee employee);

    public Employee update(Employee employee);

    public void delete(Integer employeeID);
}
