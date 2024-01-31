package poly.edu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.edu.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1", nativeQuery = true)
    Page<Product> findAllProducts(Pageable pageable);

    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 and CategoryID = ?", nativeQuery = true)
    Page<Product> findProductByCategory(Pageable pageable, Integer categoryID);

    @Query(value = "SELECT TOP 5 p.ProductID FROM Product p JOIN Orderdetails od ON p.ProductID = od.ProductID WHERE p.Productactivate = 1 GROUP BY p.ProductID ORDER BY SUM(od.Productquantity) DESC;", nativeQuery = true)
    Integer[] findProductBestSeller();      
           
            
           
          
    

}
