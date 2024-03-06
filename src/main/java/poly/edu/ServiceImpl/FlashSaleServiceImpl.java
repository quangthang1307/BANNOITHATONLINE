package poly.edu.ServiceImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Service.FlashSaleService;
import poly.edu.entity.Flashsale;
import poly.edu.repository.FlashSaleRepository;

@Service
public class FlashSaleServiceImpl implements FlashSaleService{

    @Autowired
    FlashSaleRepository flashSaleRepository;


    @Override
    public List<Flashsale> getFlashsales(LocalTime localTime) {
       return flashSaleRepository.getFlashSales(localTime);
    }


    @Override
    public List<Flashsale> getFlashsalesNow() {
        return flashSaleRepository.getFlashSalesNow();
    }
    
}
