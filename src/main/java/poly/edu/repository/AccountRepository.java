package poly.edu.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import poly.edu.entity.Account;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findByUsername(String username);

    Account findByAccountId(Integer accountID);

    Account findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT ar.account FROM AccountRole ar WHERE ar.role.id <> 1")
    List<Account> findAll();

    @Query(value = "select * from Accounts where AccountID = ?", nativeQuery = true)
    Account findByCustomerId(Integer customerId);

    @Query(value = "SELECT * FROM Accounts where Email=?", nativeQuery = true)
    List<Account> findByEmailAccount(String email);

    @Query(

            value = "SELECT a.AccountID, a.Username, a.Email, a.Active, a.Password " +

                    "FROM Accounts a " +

                    "WHERE NOT EXISTS ( " +

                    " SELECT 1 " +

                    " FROM AccountRole ar " +

                    " WHERE a.AccountID = ar.AccountID " +

                    ")",

            nativeQuery = true

    )

    List<Account> findAccountsWithoutRoles();

}
