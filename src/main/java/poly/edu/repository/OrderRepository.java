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

    @Query(value = "SELECT SUM(o.Sumpayment) FROM [Order] o JOIN Orderstatus os ON o.OrderstatusID = os.OrderstatusID WHERE os.Orderstatusname = ?1 and MONTH(o.[Time]) = ?2  AND YEAR(o.[Time]) = ?3", nativeQuery=true)
     Integer findSumpaymentOrder (String payment, Integer month, Integer year);

}
