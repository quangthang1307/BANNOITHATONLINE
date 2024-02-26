package poly.edu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import poly.edu.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1", nativeQuery = true)
    Page<Product> findAllProducts(Pageable pageable);

    // @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 and
    // CategoryID = ?", nativeQuery = true)
    // Page<Product> findProductByCategory(Pageable pageable, Integer categoryID);

    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 and CategoryID IN :categoryIDs", nativeQuery = true)
    Page<Product> findProductByCategory(Pageable pageable, @Param("categoryIDs") List<Integer> categoryIDs);

    @Query(value = "SELECT TOP 5 p.ProductID FROM Product p JOIN Orderdetails od ON p.ProductID = od.ProductID WHERE p.Productactivate = 1 GROUP BY p.ProductID ORDER BY SUM(od.Productquantity) DESC;", nativeQuery = true)
    Integer[] findProductBestSeller();

    @Query(value = "SELECT Product.* FROM Product JOIN Sale ON Product.ProductID = Sale.ProductID WHERE Sale.Statussale = 1 AND GETDATE() BETWEEN Sale.Daystartsale AND Sale.Dayendsale", nativeQuery = true)
    Page<Product> findProductOnSale(Pageable pageable);





    

}
