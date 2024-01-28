package poly.edu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.edu.entity.Product;
import poly.edu.entity.Sale;

public interface SaleRepository extends JpaRepository<Sale, Integer>{
    @Query(value="SELECT * FROM Sale WHERE Statussale = 1 and GETDATE() BETWEEN Daystartsale AND Dayendsale", nativeQuery = true)
    List<Sale> findProductOnSale();

    Sale findByProductID(Integer productID);
}
