package poly.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import poly.edu.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {

   @Query("SELECT a FROM Address a WHERE a.customer.customerId = :customerId")
    List<Address> findAddressesByCustomerId(@Param("customerId") Integer customerId);
}
