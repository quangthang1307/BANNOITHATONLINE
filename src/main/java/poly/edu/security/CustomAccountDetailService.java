package poly.edu.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import poly.edu.Service.AccountService;
import poly.edu.Service.CustomerService;
import poly.edu.entity.Account;
import poly.edu.entity.AccountRole;
import poly.edu.entity.Customer;

@Service
public class CustomAccountDetailService implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub

        Account account = accountService.findByUserName(username);
        if (account == null) {
            throw new UsernameNotFoundException("Không tồn tại");
        }

        Optional<Customer> optionalCustomer = customerService.findById(account.getAccountId());
        
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            
            Collection<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
            Set<AccountRole> roles = account.getAccountroles();

            for (AccountRole accountRole : roles) {
                grantedAuthoritySet.add(new SimpleGrantedAuthority(accountRole.getRole().getRoleName()));
            }

            return new CustomAccountDetails(account, customer, grantedAuthoritySet);
        } else {
            throw new UsernameNotFoundException("Không tìm thấy thông tin Customer");
        }
    }

}
