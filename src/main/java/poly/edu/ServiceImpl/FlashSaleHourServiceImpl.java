package poly.edu.ServiceImpl;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Service.FlashSaleHourService;
import poly.edu.entity.FlashSaleHour;
import poly.edu.repository.FlashSaleHourRepository;

@Service
public class FlashSaleHourServiceImpl implements FlashSaleHourService{

    @Autowired
    FlashSaleHourRepository flashSaleHourRepository;

    @Override
    public List<FlashSaleHour> findFlashSaleHours() {
        return flashSaleHourRepository.getFlashSalesHour();
    }

    @Override
    public FlashSaleHour findFlashSaleHoursByTimeNow(LocalTime timenow) {
        return flashSaleHourRepository.getFlashSalesHourByTimeNow(timenow);
    }

    @Override
    public FlashSaleHour update(FlashSaleHour flashSaleHour) {
        return flashSaleHourRepository.save(flashSaleHour);
    }

    @Override
    public FlashSaleHour findFlashSaleHourOnStart() {
        return flashSaleHourRepository.getFlashSalesHourOnStart();
    }

   
    
}
