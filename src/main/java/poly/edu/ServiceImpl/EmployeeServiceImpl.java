package poly.edu.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Service.AccountRoleService;
import poly.edu.Service.EmployeeService;
import poly.edu.entity.Employee;
import poly.edu.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }

    @Override

    public Employee findById(Integer employeeID) {

        // TODO Auto-generated method stub

        return employeeRepository.findById(employeeID).get();

    }

    @Override

    public Employee create(Employee employee) {

        // TODO Auto-generated method stub

        return employeeRepository.save(employee);

    }

    @Override

    public Employee update(Employee employee) {
        // TODO Auto-generated method stub

        return employeeRepository.save(employee);

    }

    @Override

    public void delete(Integer employeeID) {

        // TODO Auto-generated method stub

        employeeRepository.deleteById(employeeID);

    }
}
