package poly.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.edu.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer>{


    @Query(value="Select * from ProductImage where ProductID = ?", nativeQuery = true)
    List<ProductImage> getProductImageById(Integer productID);

}
