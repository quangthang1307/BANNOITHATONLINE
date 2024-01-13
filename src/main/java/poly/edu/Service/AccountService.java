package poly.edu.Service;

import poly.edu.entity.Account;

public interface AccountService {
    Account findByUserName(String userName);

}
