package poly.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.edu.entity.Productimage;

public interface ProductImageRepository extends JpaRepository<Productimage, Integer>{


    @Query(value="Select * from ProductImage where ProductID = ?", nativeQuery = true)
    List<Productimage> getProductImageById(Integer productID);

}
