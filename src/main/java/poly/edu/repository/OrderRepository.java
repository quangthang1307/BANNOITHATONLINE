package poly.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.edu.entity.Order;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    @Query(value = "select * from [Order] where CustomerID = ?", nativeQuery = true)
    List<Order> orderList(Integer customerId);
}
