package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.edu.entity.Account;

public interface TaiKhoanRepository  extends JpaRepository<Account, Integer>{

}
