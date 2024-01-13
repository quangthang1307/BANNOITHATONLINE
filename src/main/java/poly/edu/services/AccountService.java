package poly.edu.services;

import poly.edu.entity.Account;

public interface AccountService {
    Account findByUserName(String userName);

}
