package poly.edu.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import poly.edu.entity.Categoryproduct;

public interface CategoryProductRepository extends JpaRepository<Categoryproduct, Integer>{
    
    @Query(value="SELECT * FROM Categoryproduct WHERE IDcapcha IS NOT NULL", nativeQuery = true)
    List<Categoryproduct> getAllCategoryProductCapCon();

}