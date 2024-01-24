package poly.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.edu.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "select * from [Order] where CustomerID = ?", nativeQuery = true)
    List<Order> orderList(Integer customerId);

    // Order findBySumpayment(Double sumpayment);

    // @Query(value = "SELECT SUM(o.Sumpayment) FROM [Order] o JOIN Orderstatus os ON o.OrderstatusID = os.OrderstatusID  WHERE os.Orderstatusname = ?1 and MONTH(o.[Time]) = ?2  AND YEAR(o.[Time]) = ?3 and  o.PaymentID = ?4", nativeQuery = true)
    // Integer findSumpaymentOrder(String payment, Integer month, Integer year, Integer paymentid);

    // @Query(value = "SELECT SUM(o.[Sumpayment])FROM [Order] o JOIN [Orderstatus] os ON o.[OrderstatusID] = os.[OrderstatusID] JOIN [Payment] p ON o.[PaymentID] = p.[PaymentID] WHERE os.[Orderstatusname] = ?1 AND p.[Paymentname] = ?2 AND MONTH(o.[Time]) = ?3 AND YEAR(o.[Time]) = ?4", nativeQuery = true)
    // Integer findSumpaymentOrder(String payment, String paymentname, Integer month, Integer year);

    // @Query(value = "SELECT SUM(o.Sumpayment) FROM [Order] o JOIN Payment p ON o.PaymentID = p.PaymentID JOIN Transactions t ON o.OrderID = t.OrderID WHERE p.Paymentname = ?1 AND t.[Status] = 0 AND MONTH(o.[Time]) = ?2  AND YEAR(o.[Time]) = ?3", nativeQuery = true)
    // Integer findTransactionsOrder(String paymentName, Integer month, Integer year);

    @Query(value = "SELECT SUM(o.[Sumpayment])FROM [Order] o JOIN [Orderstatus] os ON o.[OrderstatusID] = os.[OrderstatusID] WHERE os.[Orderstatusname] = ?1 AND MONTH(o.[Time]) = ?2 AND YEAR(o.[Time]) = ?3", nativeQuery = true)
    Integer findSumpaymentOrder(String orderStatus, Integer month, Integer year);

}
