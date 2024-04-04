package poly.edu.ServiceImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<Flashsale> getFlashsalesNow(Pageable pageable) {
        return flashSaleRepository.getFlashSalesNow(pageable);
    }


    @Override
    public Page<Flashsale> getFlashsalesNowByCategory(Pageable pageable, List<Integer> categoryIDs) {
        return flashSaleRepository.getFlashSalesNowByCategory(pageable, categoryIDs);
    }


    @Override
    public void SaveAndUpdate(Flashsale flash) {
        flashSaleRepository.save(flash);
    }


    @Override
    public List<Flashsale> findByFlashsaleHour(Integer flashsalehourID) {
        return flashSaleRepository.findByFlashSaleHourID(flashsalehourID);
    }


    @Override
    public List<Flashsale> getFlashsalesNowAll() {
        return flashSaleRepository.getFlashSalesNowAll();
    }
    
}
