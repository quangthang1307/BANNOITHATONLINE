package poly.edu.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Service.AccountService;
import poly.edu.entity.Account;
import poly.edu.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService{
    
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account findByUserName(String userName) {
        // TODO Auto-generated method stub
        return accountRepository.findByUsername(userName);
    }
}