package poly.edu.repository;


import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import poly.edu.entity.Flashsale;
import poly.edu.entity.Product;

public interface FlashSaleRepository extends  JpaRepository<Flashsale, Integer> {
    @Query(value =  "SELECT * FROM Flashsale WHERE [Status] = 1 AND FlashsalehourID IN (SELECT ID FROM Flashsalehour WHERE Starthour = CAST(? AS time))", nativeQuery = true)
    List<Flashsale> getFlashSales(LocalTime starthour);

    @Query(value =  "SELECT * FROM Flashsale WHERE FlashsalehourID IN (SELECT Id FROM Flashsalehour WHERE Starthour <= CONVERT(TIME, GETDATE()) AND Endhour > CONVERT(TIME, GETDATE()) AND CONVERT(DATE, Startdate) = CONVERT(DATE, GETDATE())) ", nativeQuery = true)
    Page<Flashsale> getFlashSalesNow(Pageable pageable);

    @Query(value =  "SELECT * FROM Flashsale WHERE FlashsalehourID IN (SELECT Id FROM Flashsalehour WHERE Starthour <= CONVERT(TIME, GETDATE()) AND Endhour > CONVERT(TIME, GETDATE()) AND CONVERT(DATE, Startdate) = CONVERT(DATE, GETDATE())) ", nativeQuery = true)
    List<Flashsale> getFlashSalesNowAll();

    @Query(value =  "SELECT * FROM Flashsale WHERE ProductID IN(SELECT ProductID FROM Product WHERE CategoryID IN :categoryIDs AND FlashsalehourID IN (SELECT Id FROM Flashsalehour WHERE Starthour <= CONVERT(TIME, GETDATE()) AND Endhour > CONVERT(TIME, GETDATE())))", nativeQuery = true)
    Page<Flashsale> getFlashSalesNowByCategory(Pageable pageable, @Param("categoryIDs") List<Integer> categoryIDs);

    @Query(value =  "SELECT * FROM Flashsale WHERE FlashsalehourID = ?", nativeQuery = true)
    List<Flashsale> findByFlashSaleHourID(Integer flashSaleHourID);

    List<Flashsale> findByProduct(Product product);
}
