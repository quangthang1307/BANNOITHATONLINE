package poly.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.edu.entity.Discount;

public interface DiscountRepository  extends JpaRepository<Discount,Integer>{
    @Query(value="Select * from Discount where Code = ?1",nativeQuery = true)
	Discount findName(String code);

    @Query(value="SELECT TOP 4 * FROM Discount WHERE GETDATE() BETWEEN Startdate AND Enddate",nativeQuery = true)
	List<Discount> findTop4Discount();


}
