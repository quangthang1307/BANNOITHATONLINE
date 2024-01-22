package poly.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.edu.entity.DiscountUsage;

public interface DiscountUsageRepository extends JpaRepository<DiscountUsage, Integer> {

    @Query( value = "Select * from Discountusage where CustomerID = ?1", nativeQuery = true)
    List<DiscountUsage> findAllByCustomerId(Integer customerId);

}
