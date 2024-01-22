package poly.edu.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import poly.edu.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findByName(String name);

    boolean existsByName(String username);

    boolean existsByPhone(String userphonename);

    Customer findByPhone(String phone);

    @Query(value = "select * from Customer where AccountID = ?", nativeQuery = true)
    Customer getCustomerID(Integer customerId);
}
