package poly.edu.Responsitory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import poly.edu.entity.Cart;

public interface CartResponsitory extends JpaRepository<Cart, Integer> {
    @Query(value="SELECT * FROM Cart WHERE CustomerID = ?", nativeQuery = true)
    List<Cart> findByCustomerId(Integer customerId);

    
    @Query(value="DELETE FROM Cart WHERE CustomerID = ?", nativeQuery = true)
    public void deleteAllByCustomerId2(Integer customerId);

    @Query(value="select * from Cart where CustomerID = ?1 and ProductID = ?2", nativeQuery = true)
	Optional<Cart> findByCustomer_CustomerIdAndProduct_ProductID(Integer customerId, Integer productId);
}
