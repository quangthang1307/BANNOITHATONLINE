package poly.edu.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import poly.edu.entity.FlashSaleHour;

public interface FlashSaleHourService {
    public List<FlashSaleHour> findFlashSaleHours();

    public FlashSaleHour findFlashSaleHoursByTimeNow(LocalTime timenow);

    public FlashSaleHour findFlashSaleHourOnStart();

    public FlashSaleHour update(FlashSaleHour flashSaleHour);

    public Optional<FlashSaleHour> findbyId(Integer id);
}
