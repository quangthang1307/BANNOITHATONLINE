package poly.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.edu.entity.Account;
import poly.edu.entity.AccountRole;
import poly.edu.entity.Role;

public interface AccountRoleRepository extends JpaRepository<AccountRole, Integer>{
    List<AccountRole> findByAccount(Account account);

    AccountRole findByAccountAndRole(Account account, Role role);
}
