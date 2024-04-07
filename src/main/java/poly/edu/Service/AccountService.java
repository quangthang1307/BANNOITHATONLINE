package poly.edu.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import poly.edu.dto.AccountInfo;
import poly.edu.entity.Account;

import poly.edu.entity.Brands;

public interface AccountService {
    Account findByUserName(String userName);

    Account saveAccount(Account account);

    Account findByEmail(String email);

    public List<Account> findAll();

    public Account create(Account account);

    public void delete(Integer accountId);

    /**
     * Cập nhật thông tin của một nhà sản xuất.
     */
    public Account update(Account account);

    public Account findById(Integer accountId);
    // tài khoản
    // ResponseEntity<String> updateOrderStatus(Integer accountId, AccountStatus
    // newStatus);

    public List<AccountInfo> getAccountInfo();

}
