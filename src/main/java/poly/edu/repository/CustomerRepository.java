package poly.edu.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import poly.edu.entity.Customer;


public interface CustomerRepository extends Repository<Customer,Integer> {
    
    
    @Query(value="select * from Customer where AccountID = ?", nativeQuery = true)
    Customer getCustomerID(Integer customerId);

}
