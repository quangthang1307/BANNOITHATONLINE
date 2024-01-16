package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.edu.entity.Account;



public interface AccountRepository extends JpaRepository<Account, Integer>{

    Account findByUsername(String username);

    Account findByAccountId(Integer accountID);

   
} 
