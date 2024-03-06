package poly.edu.Service;

import java.time.LocalTime;
import java.util.List;

import poly.edu.entity.Flashsale;

public interface FlashSaleService {
   public List<Flashsale> getFlashsales(LocalTime time);

   public List<Flashsale> getFlashsalesNow();
   
}
