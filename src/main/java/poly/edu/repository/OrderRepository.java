package poly.edu.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import poly.edu.entity.Order;
import poly.edu.entity.Orderstatus;
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "select * from [Order] where CustomerID = ?", nativeQuery = true)
    List<Order> orderList(Integer customerId);

    @Query(value = "select * from [Order] where CustomerID = ?1 and OrderID = ?2", nativeQuery = true)
    Order getOrderByCustomerIdAndOrderId(Integer customerId, Integer orderId);

    @Query(value = "SELECT SUM(o.[Sumpayment])FROM [Order] o JOIN [Orderstatus] os ON o.[OrderstatusID] = os.[OrderstatusID] WHERE os.[Orderstatusname] = ?1 AND MONTH(o.[Time]) = ?2 AND YEAR(o.[Time]) = ?3", nativeQuery = true)
    Integer findSumpaymentOrder(String orderStatus, Integer month, Integer year);

    @Query(value = "SELECT DISTINCT YEAR(o.[Time]) FROM [Order] o ORDER BY YEAR(o.[Time]) DESC", nativeQuery = true)
    List<Integer> findDistinctYears();

    @Query(value = "SELECT cp.Categoryname, COUNT(od.ProductID) AS SoLuong FROM Categoryproduct cp JOIN Product p ON cp.ID = p.CategoryID JOIN OrderDetails od ON p.ProductID = od.ProductID JOIN [Order] o ON od.OrderID = o.OrderID JOIN Orderstatus os ON o.OrderstatusID = os.OrderstatusID WHERE os.Orderstatusname = N'Giao hàng thành công' AND YEAR(o.[Time]) = :selectedYear GROUP BY cp.Categoryname", nativeQuery = true)
    List<Object[]> countProductByCategoryAndStatus(@Param("selectedYear") Integer selectedYear);

    @Query(value = "Select * from [Order] where OrderstatusID = ?1 and CustomerID = ?2", nativeQuery = true)
    List<Order> findByOrderstatus_OrderstatusID_CustomerID(Integer orderstatusId, Integer customerId);

    @Query(value = "SELECT * FROM dbo.[Order] WHERE CustomerID = ?", nativeQuery = true)
	List<Order> findByCustomerid(int id);

    @Query(value = "SELECT * FROM dbo.[Order] order by [Time] desc", nativeQuery = true)
	List<Order> findOrderAll();

    @Query(value = "SELECT SUM(o.[Sumpayment]) FROM [Order] o JOIN [Orderstatus] os ON o.[OrderstatusID] = os.[OrderstatusID] WHERE os.[Orderstatusname] = ?1 AND o.[Time] BETWEEN ?2 AND ?3", nativeQuery = true)
    Integer findSumpaymentOrderForLast7Days(String orderStatus, LocalDateTime sevenDaysAgo, LocalDateTime now);

    @Query(value = "SELECT cp.Categoryname, COUNT(od.ProductID) AS SoLuong FROM Categoryproduct cp JOIN Product p ON cp.ID = p.CategoryID JOIN OrderDetails od ON p.ProductID = od.ProductID JOIN [Order] o ON od.OrderID = o.OrderID JOIN Orderstatus os ON o.OrderstatusID = os.OrderstatusID WHERE os.Orderstatusname = N'Giao hàng thành công' AND o.[Time] BETWEEN :startDate AND :endDate GROUP BY cp.Categoryname", nativeQuery = true)
    List<Object[]> countProductByCategoryAndStatusForLast7Days(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

}
