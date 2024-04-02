package poly.edu.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import poly.edu.Service.AccountService;
import poly.edu.dto.AccountInfo;
import poly.edu.entity.Account;
import poly.edu.entity.AccountRole;
import poly.edu.entity.Brands;
import poly.edu.entity.Customer;
import poly.edu.entity.Employee;
import poly.edu.entity.Role;
import poly.edu.repository.AccountRepository;
import poly.edu.repository.AccountRoleRepository;
import poly.edu.repository.CustomerRepository;
import poly.edu.repository.EmployeeRepository;
import poly.edu.repository.RoleRepository;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountRoleRepository accountRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Account findByUserName(String userName) {
        // TODO Auto-generated method stub
        return accountRepository.findByUsername(userName);
    }

    @Override
    public Account saveAccount(Account account) {
        // TODO Auto-generated method stub
        return accountRepository.save(account);
    }

    @Override
    public Account findByEmail(String email) {
        // TODO Auto-generated method stub
        return accountRepository.findByEmail(email);
    }

    @Override
    public List<Account> findAll() {
        // TODO Auto-generated method stub
        return accountRepository.findAll();
    }

    @Override
    public Account create(Account account) {
        // TODO Auto-generated method stub
        return accountRepository.save(account);
    }

    @Override
    public Account update(Account account) {
        // TODO Auto-generated method stub
        return accountRepository.save(account);
    }

    @Override
    public Account findById(Integer accountId) {
        // TODO Auto-generated method stub
        return accountRepository.findById(accountId).get();
    }

    @Override
    public void delete(Integer accountId) {
        // TODO Auto-generated method stub
        accountRepository.deleteById(accountId);

    }

    @Override
    public List<AccountInfo> getAccountInfo() {
        // TODO Auto-generated method stub
        List<AccountInfo> accountInfos = new ArrayList<>();
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            AccountInfo accountInfo = new AccountInfo();
            accountInfo.setAccount(account);

            Employee employee = employeeRepository.findByAccount(account);
            accountInfo.setEmployee(employee);

            Customer customer = customerRepository.findByAccount(account);
            accountInfo.setCustomer(customer);

            List<AccountRole> roles = accountRoleRepository.findByAccount(account);
            accountInfo.setRoles(roles);

            accountInfos.add(accountInfo);
        }

        return accountInfos;
    }

    /*
     * @Override
     * 
     * @Transactional public ResponseEntity<String> updateOrderStatus(Integer
     * accountId, AccountStatus newStatus) { Optional<Account> optionalOrder =
     * accountRepository.findById(accountId); System.out.println(accountId);
     * 
     * if (optionalOrder.isPresent()) { Account account = optionalOrder.get();
     * account.setActive(newStatus == AccountStatus.ACTIVE);
     * accountRepository.save(account); return
     * ResponseEntity.ok("Đổi trạng thái thành công!"); } else { return
     * ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy đơn hàng");
     * } }
     */
}
