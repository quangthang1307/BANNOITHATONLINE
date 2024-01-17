package poly.edu.Responsitory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.edu.entity.Product;

public interface ProductResponsitory extends JpaRepository<Product, Integer>{
    @Query(value="SELECT * FROM Product WHERE Productactivate = 1", nativeQuery = true)
    Page<Product> findAllProducts(Pageable pageable);

}
