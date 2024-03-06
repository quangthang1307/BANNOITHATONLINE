package poly.edu.repository;


import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.edu.entity.Flashsale;

public interface FlashSaleRepository extends  JpaRepository<Flashsale, Integer> {
    @Query(value =  "SELECT * FROM Flashsale WHERE [Status] = 1 AND FlashsalehourID IN (SELECT ID FROM Flashsalehour WHERE Starthour = CAST(? AS time))", nativeQuery = true)
    List<Flashsale> getFlashSales(LocalTime starthour);

    @Query(value =  "SELECT * FROM Flashsale WHERE FlashsalehourID IN (SELECT Id FROM Flashsalehour WHERE Starthour <= CONVERT(TIME, GETDATE()) AND Endhour > CONVERT(TIME, GETDATE()))", nativeQuery = true)
    List<Flashsale> getFlashSalesNow();
}
