package poly.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.edu.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{
    @Query(value="SELECT * FROM Product WHERE Productactivate = 1", nativeQuery = true)
    List<Product> findAllProducts();
}
