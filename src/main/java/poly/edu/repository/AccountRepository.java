package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.edu.entity.Account;
import java.util.List;


public interface AccountRepository extends JpaRepository<Account, Integer>{

    Account findByUsername(String username);
} 
