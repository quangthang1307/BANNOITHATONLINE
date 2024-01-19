package poly.edu.RestController;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import poly.edu.Service.AccountService;
import poly.edu.Service.CustomerService;
import poly.edu.dto.AccountDTO;
import poly.edu.entity.Account;
import poly.edu.entity.AccountRole;
import poly.edu.entity.Customer;
import poly.edu.entity.Role;
import poly.edu.repository.AccountRepository;
import poly.edu.repository.AccountRoleRepository;
import poly.edu.repository.CustomerRepository;
import poly.edu.repository.RoleRepository;

@CrossOrigin("*")
@RestController
public class RestAccountController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerService customerService;

    @Autowired
    AccountRoleRepository accountRoleRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AccountService accountService;
    
    @PostMapping("/rest/user/forgotpassword")
    public ResponseEntity<?> forgotPassWord(@RequestBody AccountDTO accountDTO){
        Account checkEmail = accountService.findByEmail(accountDTO.getEmail());

        return new ResponseEntity<>(Collections.singletonMap("message", "Mật khẩu đã được gửi vào email của bạn"), HttpStatus.OK);
    }

    @PostMapping("/rest/user/sigup")
    public ResponseEntity<?> registerUser(@RequestBody AccountDTO accountDTO) {

        // Kiểm tra tài khoản đã tồn tại hay chưa
        if (accountRepository.existsByUsername(accountDTO.getUsername())) {
            // return new ResponseEntity<>("Tên đăng nhập đã tồn tại", HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>(Collections.singletonMap("message", "Tên đăng nhập đã tồn tại"), HttpStatus.OK);
        }

        // Kiểm tra Email đã tồn tại hay chưa
        if (accountRepository.existsByEmail(accountDTO.getEmail())) {
            // return new ResponseEntity<>("Email này đã tồn tại", HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>(Collections.singletonMap("message", "Email này đã được sử dụng"), HttpStatus.OK);
        }

        if (customerRepository.existsByName(accountDTO.getName()
        )){
            // return new ResponseEntity<>("Tên người dùng đã tồn tại", HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>(Collections.singletonMap("message", "Tên người dùng đã tồn tại"), HttpStatus.OK);
        }

        if (customerRepository.existsByPhone(accountDTO.getPhone()
        )){
            // return new ResponseEntity<>("Số điện thoại đã được sử dụng", HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>(Collections.singletonMap("message", "Số điện thoại đã được sử dụng"), HttpStatus.OK);
        }

        Account account = new Account();
        account.setUsername(accountDTO.getUsername());
        account.setEmail(accountDTO.getEmail());
        account.setPassword(accountDTO.getPassword());
        account.setActive(true);

        Role role = roleRepository.findByRoleName("USER").get();
        // account.accountroles(Collections.singleton(role));
        AccountRole accountRole = new AccountRole();
        accountRole.setRole(role);
        accountRole.setAccount(account);

        Customer customer = new Customer();
        customer.setName(accountDTO.getName());
        customer.setPhone(accountDTO.getPhone());
        customer.setAccount(account);

        accountRepository.save(account);
        customerRepository.save(customer);
        accountRoleRepository.save(accountRole);

        return new ResponseEntity<>(Collections.singletonMap("message", "Đăng ký thành công"), HttpStatus.OK);

    }
}
