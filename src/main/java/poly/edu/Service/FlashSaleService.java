package poly.edu.Service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import poly.edu.entity.Flashsale;

public interface FlashSaleService {

   public void SaveAndUpdate(Flashsale flash);

   public List<Flashsale> getFlashsales(LocalTime time);

   public Page<Flashsale> getFlashsalesNow(Pageable pageable);

   public List<Flashsale> getFlashsalesNowAll();

   public Page<Flashsale> getFlashsalesNowByCategory(Pageable pageable, List<Integer> categoryIDs);

   public List<Flashsale> findByFlashsaleHour(Integer flashsalehourID);

   
}
