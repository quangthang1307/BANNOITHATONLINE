package poly.edu.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import poly.edu.entity.Account;
import poly.edu.entity.Customer;
import poly.edu.oauth2.CustomerOauth2User;
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
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            if (oauthToken.getPrincipal() instanceof OidcUser) {
                OidcUser oidcUser = (OidcUser) oauthToken.getPrincipal();
                String email = oidcUser.getAttribute("email");
                Account Oauth2GoogleAccount = accountRepository.findByUsername(email);
                if (Oauth2GoogleAccount != null) {
                    System.out.println(authentication);
                    Customer customer = customerRepository.getCustomerID(Oauth2GoogleAccount.getAccountId());
                    return ResponseEntity.ok(customer);
                } else {
                    Account newAccount = new Account();
                    newAccount.setUsername(email);
                    newAccount.setEmail(email);
                    newAccount.setActive(true);
                    accountRepository.save(newAccount);

                    Customer newCustomer = new Customer();
                    newCustomer.setAccount(newAccount);
                    customerRepository.save(newCustomer);
                    return ResponseEntity.ok(newCustomer);
                }
            }
        }
        try {
            Account account = accountRepository.findByUsername(username);
            if (account != null) {
                System.out.println(authentication);
                Customer customer = customerRepository.getCustomerID(account.getAccountId());
                return ResponseEntity.ok(customer);
            } else {

                Object principal = authentication.getPrincipal();
                if (principal instanceof CustomerOauth2User) {
                    CustomerOauth2User customerOauth2User = (CustomerOauth2User) principal;

                    String email = customerOauth2User.getAttribute("email");
                    String id = customerOauth2User.getAttribute("id");
                    Account Oauth2Account = accountRepository.findByUsername(email);
                    if (Oauth2Account != null) {
                        System.out.println(authentication);
                        Customer customer = customerRepository.getCustomerID(Oauth2Account.getAccountId());
                        return ResponseEntity.ok(customer);
                    } else {
                        Account newAccount = new Account();
                        newAccount.setUsername(email);
                        newAccount.setEmail(email);
                        newAccount.setActive(true);
                        accountRepository.save(newAccount);

                        Customer newCustomer = new Customer();
                        newCustomer.setAccount(newAccount);
                        customerRepository.save(newCustomer);
                        return ResponseEntity.ok(newCustomer);
                    }
                }
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        // System.out.println("Logged in user: " + username);
        return ResponseEntity.notFound().build();
    }

}
