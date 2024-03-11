package poly.edu.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import poly.edu.entity.Account;
import poly.edu.entity.Customer;

public class CustomAccountDetails implements UserDetails {
    private Account account;
    private Customer customer;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomAccountDetails(Account account, Customer customer, Collection<? extends GrantedAuthority> authorities) {
        this.account = account;
        this.customer = customer;
        this.authorities = authorities;
    }

    // Getter methods for account, customer, and authorities

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Customer getCustomer() {
        return customer;
    }
}

