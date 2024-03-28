package poly.edu.dto;

import java.util.List;

import poly.edu.entity.Account;
import poly.edu.entity.AccountRole;
import poly.edu.entity.Customer;
import poly.edu.entity.Employee;

public class AccountInfo {
    private Account account;
    private Employee employee;
    private Customer customer;
    private List<AccountRole> roles;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<AccountRole> getRoles() {
        return roles;
    }

    public void setRoles(List<AccountRole> roles) {
        this.roles = roles;
    }
}
