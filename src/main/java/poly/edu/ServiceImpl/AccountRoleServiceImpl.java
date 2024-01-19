package poly.edu.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Service.AccountRoleService;
import poly.edu.repository.AccountRoleRepository;

@Service
public class AccountRoleServiceImpl implements AccountRoleService{

    @Autowired
    AccountRoleRepository accountRoleRepository;
    
}
