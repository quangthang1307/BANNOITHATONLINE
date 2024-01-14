package poly.edu.Responsitory;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import poly.edu.entity.ProductImage;

public interface ProductImageRepository extends Repository<ProductImage, Integer>{


    @Query(value="Select * from ProductImage where ProductID = ?", nativeQuery = true)
    List<ProductImage> getProductImageById(Integer productID);

}
