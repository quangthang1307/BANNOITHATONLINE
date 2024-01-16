package poly.edu.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import poly.edu.entity.Account;
import poly.edu.entity.Customer;
import poly.edu.repository.AccountRepository;
import poly.edu.repository.CustomerRepository;

import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin("*")
@RestController
public class CustomerRestController {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/rest/customer")
    public ResponseEntity<Customer> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(username != null){
            Account account = accountRepository.findByUsername(username);
            if(account != null){
                Customer customer = customerRepository.getCustomerID(account.getAccountId());
                return ResponseEntity.ok(customer);
            }
        }
        System.out.println("Logged in user: " + username);
        return ResponseEntity.notFound().build();
    }
}
