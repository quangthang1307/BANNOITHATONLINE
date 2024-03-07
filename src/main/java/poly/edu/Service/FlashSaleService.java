package poly.edu.Service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import poly.edu.entity.Flashsale;

public interface FlashSaleService {
   public List<Flashsale> getFlashsales(LocalTime time);

   public Page<Flashsale> getFlashsalesNow(Pageable pageable);

   public Page<Flashsale> getFlashsalesNowByCategory(Pageable pageable, List<Integer> categoryIDs);
   
}
