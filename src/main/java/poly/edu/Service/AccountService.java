package poly.edu.Service;

import java.util.Optional;

import poly.edu.entity.Account;

public interface AccountService {
    Account findByUserName(String userName);

    Account saveAccount(Account account);
}
