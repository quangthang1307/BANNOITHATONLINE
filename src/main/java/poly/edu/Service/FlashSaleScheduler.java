package poly.edu.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.entity.FlashSaleHour;
import poly.edu.entity.Flashsale;

@Service
public class FlashSaleScheduler {
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    private FlashSaleHourService flashSaleHourService;

    @Autowired
    private FlashSaleService flashSaleService;



    // public void scheduleFlashSaleAtFixedHours(List<FlashSaleHour> flashSaleHour) {

     
    //     for (int i = 0; i < flashSaleHour.size(); i++) {
    //         scheduleFlashSaleTask(flashSaleHour.get(i));

    //     }
    // }

    // private void scheduleFlashSaleTask(FlashSaleHour flashSaleHour) {
    //     long delay = getStartTimeToNextOccurrence(flashSaleHour);
    //     scheduler.schedule(() -> {
    //         System.out.println("Flash sale is starting now at " + LocalDateTime.now());
    //         // Thực hiện công việc khi bắt đầu flash sale

    //         // Lập lịch cho công việc khi kết thúc flash sale
    //         scheduleSaleOffTask(flashSaleHour);

            
    //     }, delay, TimeUnit.MILLISECONDS);


        
    // }

    // private void scheduleSaleOffTask(FlashSaleHour flashSaleHour) {
    //     long delay = getEndTimeToNextOccurrence(flashSaleHour.getHourEnd()); // Truyền endTime vào cả hai tham số để
    //                                                                          // tính toán chính xác
    //     scheduler.schedule(() -> {
    //         System.out.println("Sale off at " + LocalDateTime.now());
    //         // Thực hiện công việc khi kết thúc flash sale
    //         LocalDateTime targetDateTime = LocalDateTime.now();

    //         if (flashSaleHour.getFrequencyFor().trim().equals("DAY")) {
    //             targetDateTime = LocalDateTime.of(flashSaleHour.getDateStart().plusDays(flashSaleHour.getFrequency()),
    //                     flashSaleHour.getHourStart());
    //         }

    //         if (flashSaleHour.getFrequencyFor().trim().equals("WEEK")) {
    //             targetDateTime = LocalDateTime.of(flashSaleHour.getDateStart().plusWeeks(flashSaleHour.getFrequency()),
    //                     flashSaleHour.getHourStart());
    //         }

    //         if (flashSaleHour.getFrequencyFor().trim().equals("MONTH")) {
    //             targetDateTime = LocalDateTime.of(flashSaleHour.getDateStart().plusMonths(flashSaleHour.getFrequency()),
    //                     flashSaleHour.getHourStart());
    //         }

    //         flashSaleHour.setDateStart(targetDateTime.toLocalDate());
    //         flashSaleHourService.update(flashSaleHour);
    //     }, delay, TimeUnit.MILLISECONDS);
    // }

    public long getStartTimeToNextOccurrence(FlashSaleHour flashSaleHour) {
        LocalTime now = LocalTime.now();

        LocalDateTime currentDateTime = LocalDateTime.now();

        // Tạo LocalDateTime từ ngày hiện tại và thời gian mốc
        LocalDateTime targetDateTime = LocalDateTime.of(currentDateTime.toLocalDate(), flashSaleHour.getHourStart());

        if (LocalDate.now().isEqual(flashSaleHour.getDateStart())) {
            System.out.println("GIONG NGAY HIEN TAI");
            // Nếu thời gian hiện tại đã qua thời gian bắt đầu và thời gian kết thúc
            if (now.compareTo(flashSaleHour.getHourStart()) > 0 && now.compareTo(flashSaleHour.getHourEnd()) > 0) {
                System.out.println("GIONG NGAY HIEN TAI VA DA QUA THOI GIAN BAT DAU VÀ KET THUC");
                if (flashSaleHour.getFrequencyFor().trim().equals("DAY")) {
                    System.out.println("GIONG DAY");
                    targetDateTime = LocalDateTime.of(
                            flashSaleHour.getDateStart().plusDays(flashSaleHour.getFrequency()),
                            flashSaleHour.getHourStart());
                }

                if (flashSaleHour.getFrequencyFor().trim().equals("WEEK")) {
                    System.out.println("GIONG WEEK");
                    targetDateTime = LocalDateTime.of(
                            flashSaleHour.getDateStart().plusWeeks(flashSaleHour.getFrequency()),
                            flashSaleHour.getHourStart());
                }

                if (flashSaleHour.getFrequencyFor().trim().equals("MONTH")) {
                    System.out.println("GIONG MONTH");
                    targetDateTime = LocalDateTime.of(
                            flashSaleHour.getDateStart().plusMonths(flashSaleHour.getFrequency()),
                            flashSaleHour.getHourStart());
                }

                flashSaleHour.setDateStart(targetDateTime.toLocalDate());
                flashSaleHourService.update(flashSaleHour);

            }

            if (now.compareTo(flashSaleHour.getHourStart()) > 0 && now.compareTo(flashSaleHour.getHourEnd()) < 0) {
                return 0;
            }
        }
        if (LocalDate.now().isBefore(flashSaleHour.getDateStart())) {
            targetDateTime = LocalDateTime.of(flashSaleHour.getDateStart(), flashSaleHour.getHourStart());

        }
        if (LocalDate.now().isAfter(flashSaleHour.getDateStart())) {
            if (flashSaleHour.getFrequencyFor().trim().equals("DAY")) {
                System.out.println("GIONG NHAU");
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

        System.out.println("wait for day" + targetDateTime);
        // Tính toán khoảng thời gian giữa thời gian hiện tại và mốc thời gian
        Duration duration = Duration.between(currentDateTime, targetDateTime);

        // Trả về thời gian cần chờ đợi dưới dạng mili giây
        return duration.toMillis();

    }

    public long getEndTimeToNextOccurrence(LocalTime endTime) {

        LocalDateTime currentDateTime = LocalDateTime.now();

        // Tạo LocalDateTime từ ngày hiện tại và thời gian mốc
        LocalDateTime targetDateTime = LocalDateTime.of(currentDateTime.toLocalDate(), endTime);

        // Tính toán khoảng thời gian giữa thời gian hiện tại và mốc thời gian
        Duration duration = Duration.between(currentDateTime, targetDateTime);

        // Trả về thời gian cần chờ đợi dưới dạng mili giây
        return duration.toMillis();

    }

}