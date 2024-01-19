package poly.edu.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Service.CustomerService;
import poly.edu.entity.Customer;
import poly.edu.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public Customer findByName(String name) {
        // TODO Auto-generated method stub
        return customerRepository.findByName(name);
    }

    @Override
    public Customer findByPhone(String phone) {
        // TODO Auto-generated method stub
        return customerRepository.findByPhone(phone);
    }

}
