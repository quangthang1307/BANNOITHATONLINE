package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.edu.entity.AccountRole;

public interface AccountRoleRepository extends JpaRepository<AccountRole, Integer>{
    
}
