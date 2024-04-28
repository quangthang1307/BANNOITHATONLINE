package poly.edu.repository;

import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import poly.edu.entity.Account;
import poly.edu.entity.Evaluate;
import poly.edu.entity.Order;

@Repository
public interface EvaluateRepository extends JpaRepository<Evaluate, Integer> {

    @Query("SELECT e FROM Evaluate e WHERE e.productId = :productId")
    List<Evaluate> findByProductId1(@Param("productId") Integer productId);
    // EvaluateRepository.java
    // @Query("SELECT e FROM Evaluate e WHERE e.product.productId = :productId")
    // List<Evaluate> findByProductId1(@Param("productId") Integer productId);

    Optional<List<Evaluate>> findByProductId(Integer productId);

    @Query(value = "SELECT * FROM dbo.[Order] WHERE CustomerID = ?", nativeQuery = true)
    List<Order> findByCustomerid(int id);
    // @Query("SELECT COUNT(r) FROM Evaluate r WHERE r.product.id = :productId")
    // Integer countReviewProduct(@Param("productId") Integer productId);
    //
    // @Query("select AVG(r.Rating) from Evaluate r where r.product.id =
    // :productId")
    // Integer getAvgReviewByProductId(@Param("productId") Integer productId);
    //
    // @Query("select COUNT(r) from Evaluate r where r.product.id = :productId and
    // r.evaluateStar >= 4")
    // Integer getCountReviewThan4SById(@Param("productId") Integer productId);
    //
    // @Query("select r from Evaluate r where r.product.id = :productId and
    // r.evaluateStar = :start")
    // Evaluate getChooseStartByProductId(@Param("productId") Integer productId,
    // @Param("start") Integer start);

    @Query(value="SELECT dg.EvaluateID FROM Evaluate dg INNER JOIN Product p ON dg.ProductID = p.ProductID INNER JOIN Customer c ON dg.CustomerID = c.CustomerID INNER JOIN OrderDetails od ON p.ProductID = od.ProductID INNER JOIN [Order] o ON od.OrderID = o.OrderID WHERE dg.CustomerID = ?1 AND dg.ProductID = ?2 GROUP BY dg.EvaluateID", nativeQuery = true)
    List<?> disableBTN(Integer CustomerID, Integer ProductID);
}
