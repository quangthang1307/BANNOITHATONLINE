package poly.edu.RestController;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import poly.edu.Service.FlashSaleHourService;
import poly.edu.Service.FlashSaleScheduler;
import poly.edu.Service.FlashSaleService;
import poly.edu.entity.FlashSaleDelay;
import poly.edu.entity.FlashSaleEndDelay;
import poly.edu.entity.FlashSaleHour;
import poly.edu.entity.Flashsale;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("*")
@RestController
public class FlashSaleRestController {
    @Autowired
    FlashSaleScheduler flashSaleScheduler;

    @Autowired
    FlashSaleService flashSaleService;

    @Autowired
    FlashSaleHourService flashSaleHourService;

    @GetMapping("/rest/flashsaledelay/start")
    public ResponseEntity<List<FlashSaleDelay>> getFlashSaleDelayStart() {
        List<FlashSaleHour> flashsalesHours = flashSaleHourService.findFlashSaleHours();
        List<FlashSaleDelay> delays = new ArrayList<>();
       


        for(FlashSaleHour flashsSaleHour : flashsalesHours){

           long delay = flashSaleScheduler.getStartTimeToNextOccurrence(flashsSaleHour);
          
           List<Flashsale> list = flashSaleService.getFlashsales(flashsSaleHour.getHourStart());

           FlashSaleDelay fsd = new FlashSaleDelay();
           fsd.setDelay(delay);
           fsd.setFlashsale(list);

           delays.add(fsd);
          
        }

        return ResponseEntity.ok(delays); //);
    }

    @GetMapping("/rest/flashsaledelay/end")
    public ResponseEntity<FlashSaleEndDelay> getFlashSaleDelayEnd() {
        FlashSaleHour flashsalesHours = flashSaleHourService.findFlashSaleHourOnStart();
        long delay = flashSaleScheduler.getEndTimeToNextOccurrence(flashsalesHours.getHourEnd());

        FlashSaleEndDelay fse = new FlashSaleEndDelay();
        fse.setDelay(delay);
        fse.setFlashsalehour(flashsalesHours);
        

        return ResponseEntity.ok(fse); //);
    }

    @GetMapping("/rest/flashsale")
    public ResponseEntity<Page<Flashsale>> getFlashSale( 
                    @RequestParam(defaultValue = "0") int page,
                    @RequestParam(defaultValue = "10") int size) {
    PageRequest pageRequest = PageRequest.of(page, size);
    Page<Flashsale> flashPage = flashSaleService.getFlashsalesNow(pageRequest);
        return ResponseEntity.ok(flashPage); //);
    }

    @GetMapping("/rest/flashsale/category")
    public ResponseEntity<Page<Flashsale>> getFlashSaleByCategory( 
                    @RequestParam(defaultValue = "0") int page,
                    @RequestParam(defaultValue = "10") int size,
                    @RequestParam(name = "categoryId") List<Integer> categoryId) {
    PageRequest pageRequest = PageRequest.of(page, size);
    Page<Flashsale> flashPage = flashSaleService.getFlashsalesNowByCategory(pageRequest, categoryId);
        return ResponseEntity.ok(flashPage); //);
    }

    @PutMapping("/rest/flashsale/update")
    public void UpdateFlashSale(@RequestBody FlashSaleHour flashSaleHour) {
        LocalDateTime targetDateTime = LocalDateTime.now();

            if (flashSaleHour.getFrequencyFor().trim().equals("DAY")) {
                targetDateTime = LocalDateTime.of(flashSaleHour.getDateStart().plusDays(flashSaleHour.getFrequency()),
                        flashSaleHour.getHourStart());
            }

            if (flashSaleHour.getFrequencyFor().trim().equals("WEEK")) {
                targetDateTime = LocalDateTime.of(flashSaleHour.getDateStart().plusWeeks(flashSaleHour.getFrequency()),
                        flashSaleHour.getHourStart());
            }

            if (flashSaleHour.getFrequencyFor().trim().equals("MONTH")) {
                targetDateTime = LocalDateTime.of(flashSaleHour.getDateStart().plusMonths(flashSaleHour.getFrequency()),
                        flashSaleHour.getHourStart());
            }

            flashSaleHour.setDateStart(targetDateTime.toLocalDate());
            flashSaleHourService.update(flashSaleHour);
        
    }

}
