package poly.edu.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import poly.edu.Service.AccountService;
import poly.edu.entity.Account;
import poly.edu.entity.AccountRole;

@Service
public class CustomAccountDetailService implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub

        Account account = accountService.findByUserName(username);
        if (account == null) {
            throw new UsernameNotFoundException("Không tồn tại");
        }
        // Thêm kiểm tra trạng thái hoạt động
        if (!account.getActive()) {
            throw new UsernameNotFoundException("Tài khoản đã ngưng hoạt động");
        }

        Collection<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        Set<AccountRole> roles = account.getAccountroles();

        for (AccountRole accountRole : roles) {
            grantedAuthoritySet.add(new SimpleGrantedAuthority(accountRole.getRole().getRoleName()));
        }

        return new CustomAccountDetails(account, grantedAuthoritySet);
    }

}