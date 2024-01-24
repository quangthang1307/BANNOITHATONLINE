package poly.edu.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	@Override
	public List<Customer> findAll() {
		// TODO Auto-generated method stub
		return customerRepository.findAll();
	}

	@Override
	public Optional<Customer> findById(Integer customerId) {
		// TODO Auto-generated method stub
		return customerRepository.findByIdCustomer(customerId);
	}

	@Override
	public Page<Customer> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return customerRepository.findAll(pageable);
	}

	@Override
	public Customer create(Customer customer) {
		// TODO Auto-generated method stub
		return customerRepository.save(customer);
	}

	@Override
	public Customer update(Customer customer) {
		// TODO Auto-generated method stub
		return customerRepository.save(customer);
	}

	@Override
	public void delete(Integer id) {
		customerRepository.deleteById(id);
		
	}
}
