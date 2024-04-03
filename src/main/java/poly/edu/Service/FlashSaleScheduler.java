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

    // public void scheduleFlashSaleAtFixedHours(List<FlashSaleHour> flashSaleHour)
    // {

    // for (int i = 0; i < flashSaleHour.size(); i++) {
    // scheduleFlashSaleTask(flashSaleHour.get(i));

    // }
    // }

    // private void scheduleFlashSaleTask(FlashSaleHour flashSaleHour) {
    // long delay = getStartTimeToNextOccurrence(flashSaleHour);
    // scheduler.schedule(() -> {
    // System.out.println("Flash sale is starting now at " + LocalDateTime.now());
    // // Thực hiện công việc khi bắt đầu flash sale

    // // Lập lịch cho công việc khi kết thúc flash sale
    // scheduleSaleOffTask(flashSaleHour);

    // }, delay, TimeUnit.MILLISECONDS);

    // }

    // private void scheduleSaleOffTask(FlashSaleHour flashSaleHour) {
    // long delay = getEndTimeToNextOccurrence(flashSaleHour.getHourEnd()); //
    // Truyền endTime vào cả hai tham số để
    // // tính toán chính xác
    // scheduler.schedule(() -> {
    // System.out.println("Sale off at " + LocalDateTime.now());
    // // Thực hiện công việc khi kết thúc flash sale
    // LocalDateTime targetDateTime = LocalDateTime.now();

    // if (flashSaleHour.getFrequencyFor().trim().equals("DAY")) {
    // targetDateTime =
    // LocalDateTime.of(flashSaleHour.getDateStart().plusDays(flashSaleHour.getFrequency()),
    // flashSaleHour.getHourStart());
    // }

    // if (flashSaleHour.getFrequencyFor().trim().equals("WEEK")) {
    // targetDateTime =
    // LocalDateTime.of(flashSaleHour.getDateStart().plusWeeks(flashSaleHour.getFrequency()),
    // flashSaleHour.getHourStart());
    // }

    // if (flashSaleHour.getFrequencyFor().trim().equals("MONTH")) {
    // targetDateTime =
    // LocalDateTime.of(flashSaleHour.getDateStart().plusMonths(flashSaleHour.getFrequency()),
    // flashSaleHour.getHourStart());
    // }

    // flashSaleHour.setDateStart(targetDateTime.toLocalDate());
    // flashSaleHourService.update(flashSaleHour);
    // }, delay, TimeUnit.MILLISECONDS);
    // }

    public long getStartTimeToNextOccurrence(FlashSaleHour flashSaleHour) {
        LocalTime now = LocalTime.now();

        LocalDateTime currentDateTime = LocalDateTime.now();

        // Tạo LocalDateTime từ ngày hiện tại và thời gian mốc
        LocalDateTime targetDateTime = LocalDateTime.of(currentDateTime.toLocalDate(), flashSaleHour.getHourStart());

        // Ngày bắt đầu là ngày hiện tại
        if (LocalDate.now().isEqual(flashSaleHour.getDateStart())) {
            // Nếu thời gian hiện tại đã qua thời gian bắt đầu và thời gian kết thúc
            if (now.compareTo(flashSaleHour.getHourStart()) > 0 && now.compareTo(flashSaleHour.getHourEnd()) > 0) {
                targetDateTime = updateDate(flashSaleHour, targetDateTime);
            }
            // Nếu thời gian hiện tại đã qua thời gian bắt đầu nhưng chưa đến thời gian kết
            // thúc
            if (now.compareTo(flashSaleHour.getHourStart()) > 0 && now.compareTo(flashSaleHour.getHourEnd()) < 0) {
                return 0;
            }
        }
        // Ngày bắt đầu chưa đến
        if (LocalDate.now().isBefore(flashSaleHour.getDateStart())) {
            targetDateTime = LocalDateTime.of(flashSaleHour.getDateStart(), flashSaleHour.getHourStart());
        }
        // Đã qua ngày bắt đầu
        if (LocalDate.now().isAfter(flashSaleHour.getDateStart())) {
            targetDateTime = updateDate(flashSaleHour, targetDateTime);
        }

        // Tính khoảng thời gian giữa thời gian hiện tại và mốc thời gian bắt đầu
        Duration duration = Duration.between(currentDateTime, targetDateTime);

        // Trả về thời gian cần chờ đợi dưới dạng mili giây
        return duration.toMillis();

    }

    public long getEndTimeToNextOccurrence(LocalTime endTime) {

        LocalDateTime currentDateTime = LocalDateTime.now();

        // Tạo LocalDateTime kết thúc
        LocalDateTime targetDateTime = LocalDateTime.of(currentDateTime.toLocalDate(), endTime);

        // Tính toán khoảng thời gian giữa thời gian hiện tại và mốc thời gian kết thúc
        Duration duration = Duration.between(currentDateTime, targetDateTime);

        // Trả về thời gian cần chờ đợi dưới dạng mili giây
        return duration.toMillis();

    }

    public LocalDateTime updateDate(FlashSaleHour flashSaleHour, LocalDateTime targetDateTime) {
        boolean check = false;
        // Nếu Lặp theo ngày
        if (flashSaleHour.getFrequencyFor().trim().equals("DAY")) {
            while (!check) {
                targetDateTime = targetDateTime.plusDays(flashSaleHour.getFrequency());
                targetDateTime = LocalDateTime.of(targetDateTime.toLocalDate(), flashSaleHour.getHourStart());
                System.out.println(targetDateTime);
                if (LocalDateTime.now().isBefore(targetDateTime)) {
                    check = true;
                }
            }

        }
        // Nếu Lặp theo tuần
        if (flashSaleHour.getFrequencyFor().trim().equals("WEEK")) {
            while (!check) {
                targetDateTime = targetDateTime.plusWeeks(flashSaleHour.getFrequency());
                targetDateTime = LocalDateTime.of(targetDateTime.toLocalDate(), flashSaleHour.getHourStart());
                System.out.println(targetDateTime);
                if (LocalDateTime.now().isBefore(targetDateTime)) {
                    check = true;
                }
            }

        }
        // Nếu Lặp theo tháng
        if (flashSaleHour.getFrequencyFor().trim().equals("MONTH")) {
            while (!check) { // Vòng lặp vô hạn
                targetDateTime = targetDateTime.plusMonths(flashSaleHour.getFrequency());
                targetDateTime = LocalDateTime.of(targetDateTime.toLocalDate(), flashSaleHour.getHourStart());
                System.out.println(targetDateTime);
                if (LocalDateTime.now().isBefore(targetDateTime)) {
                    check = true;
                }
            }

        }
        
        flashSaleHour.setDateStart(targetDateTime.toLocalDate());
        flashSaleHourService.update(flashSaleHour);

        return targetDateTime;
    }

}