package poly.edu.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import poly.edu.entity.Customer;


public interface CustomerService {

    Customer findByName(String name);

    Customer findByPhone(String phone);

    List<Customer> findAll();

    Optional<Customer> findById(Integer customerId) ;
    
    Customer create(Customer customer) ;

	Customer update(Customer customer) ;

    void delete(Integer id) ;

    Page<Customer> findAll(Pageable pageable) ;
}
