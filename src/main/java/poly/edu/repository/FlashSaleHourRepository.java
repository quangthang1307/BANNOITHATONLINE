package poly.edu.repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.edu.entity.FlashSaleHour;

public interface FlashSaleHourRepository extends JpaRepository<FlashSaleHour, Integer>{
    
    @Query(value =  "SELECT * FROM Flashsalehour WHERE [Status] = 1", nativeQuery = true)
    List<FlashSaleHour> getFlashSalesHour();

    @Query(value =  "SELECT * FROM Flashsalehour WHERE Starthour = CAST(? AS time)", nativeQuery = true)
    FlashSaleHour getFlashSalesHourByTimeNow(LocalTime timenow);

    @Query(value =  "SELECT * FROM Flashsalehour WHERE Starthour <= CONVERT(TIME, GETDATE()) AND Endhour > CONVERT(TIME, GETDATE())", nativeQuery = true)
    FlashSaleHour getFlashSalesHourOnStart();
}
