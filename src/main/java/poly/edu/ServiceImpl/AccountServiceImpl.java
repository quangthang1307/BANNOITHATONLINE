package poly.edu.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Service.AccountService;
import poly.edu.entity.Account;
import poly.edu.entity.Brands;
import poly.edu.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account findByUserName(String userName) {
        // TODO Auto-generated method stub
        return accountRepository.findByUsername(userName);
    }

    @Override
    public Account saveAccount(Account account) {
        // TODO Auto-generated method stub
        return accountRepository.save(account);
    }

    @Override
    public Account findByEmail(String email) {
        // TODO Auto-generated method stub
        return accountRepository.findByEmail(email);
    }

    @Override
    public List<Account> findAll() {
        // TODO Auto-generated method stub
        return accountRepository.findAll();
    }

    @Override
    public Account create(Account account) {
        // TODO Auto-generated method stub
        return accountRepository.save(account);
    }

    @Override
    public Account update(Account account) {
        // TODO Auto-generated method stub
        return accountRepository.save(account);
    }

    @Override
    public Account findById(Integer accountId) {
        // TODO Auto-generated method stub
        return accountRepository.findById(accountId).get();
    }

	@Override
	public void delete(Integer accountId) {
		// TODO Auto-generated method stub
		accountRepository.deleteById(accountId);
		
	}
	  
}
