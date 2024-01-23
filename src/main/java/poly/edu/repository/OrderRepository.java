package poly.edu.repository;

import java.util.List;

import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.edu.entity.Order;


public interface OrderRepository extends JpaRepository<Order,Integer> {

    @Query(value = "select * from [Order] where CustomerID = ?", nativeQuery = true)
    List<Order> orderList(Integer customerId);

    // Order findBySumpayment(Double sumpayment);

    @Query(value = "SELECT SUM(o.Sumpayment) FROM [Order] o WHERE MONTH(o.[Time]) = ?1  AND YEAR(o.[Time]) = ?2", nativeQuery=true)
     Integer findSumpaymentOrder (Integer month, Integer year);

}
