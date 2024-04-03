package poly.edu.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Service.EmployeeService;
import poly.edu.entity.Employee;
import poly.edu.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Employee findByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }
    
}
