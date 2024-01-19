package poly.edu.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import poly.edu.entity.Account;
import java.util.List;


@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    
    Account findByUsername(String username);

    Account findByAccountId(Integer accountID);

    Account  findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
