package poly.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import poly.edu.entity.Orderdetails;

@Repository
public interface OrderDetailRepository extends JpaRepository<Orderdetails, Integer> {

    @Query(value = "select * from [Orderdetails] where OrderID = ?1", nativeQuery = true)
    List<Orderdetails> getOrderdetailsByOrderID(Integer orderID);

    // đánh giá
    @Query(value = "select * from Orderdetails where OrderID = ? and ProductID = ?", nativeQuery = true)
    Orderdetails getOrderdetailsByOrderIDandProductid(Integer orderID, Integer productid);
}
