package poly.edu.repository;

import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import poly.edu.entity.Evaluate;

@Repository
public interface EvaluateRepository extends JpaRepository<Evaluate, Integer> {

    Optional<List<Evaluate>> findByProductId(Integer productId);
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
}
