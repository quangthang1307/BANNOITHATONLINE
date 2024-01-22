package poly.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.edu.entity.Orderdetails;

public interface OrderDetailRepository extends JpaRepository<Orderdetails,Integer> {

    @Query(value="select * from [Orderdetails] where OrderID = ?1", nativeQuery = true)
    List<Orderdetails> getOrderdetailsByOrderID(Integer orderID);
}
