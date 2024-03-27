package poly.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.edu.entity.Account;
import poly.edu.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee findByAccount(Account account);
}
