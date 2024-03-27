package poly.edu.repository;

import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import poly.edu.entity.Account;
import poly.edu.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Page<Customer> findByCustomerId(Integer customerId, Pageable pageable);
	
	@Query(value = "select * from Customer where CustomerID = ?1", nativeQuery = true)	
	Optional<Customer> findByIdCustomer(Integer customerId);

    Customer findByName(String name);

    boolean existsByName(String username);

    boolean existsByPhone(String userphonename);

    Customer findByPhone(String phone);

    @Query(value = "select * from Customer where AccountID = ?", nativeQuery = true)
    Customer getCustomerID(Integer customerId);

    Customer findByAccountUsername(String username);

    Customer findByAccount(Account account);
}
