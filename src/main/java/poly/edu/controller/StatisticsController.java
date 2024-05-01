package poly.edu.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import poly.edu.repository.OrderRepository;

@Controller
public class StatisticsController {

    @Autowired
    OrderRepository orderRepository;

    @RequestMapping("/admin/statistics")
    public String showstatistics(Model model,
            @RequestParam(name = "selectedYear", required = false) Integer selectedYear) {

        if (selectedYear == null) {
            selectedYear = Year.now().getValue();
        }

        List<Integer> years = orderRepository.findDistinctYears();
        model.addAttribute("years", years);
        model.addAttribute("selectedYear", selectedYear);

        List<Integer> totalSumpayments = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            Integer sumpayment = orderRepository.findSumpaymentOrder("Thanh toán thành công", month, selectedYear);
            totalSumpayments.add(sumpayment);
        }

        List<Object[]> categoryCounts = orderRepository.countProductByCategoryAndStatus(selectedYear);
        List<Map<String, Object>> chartData = new ArrayList<>();
        for (Object[] categoryCount : categoryCounts) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", categoryCount[0]);
            map.put("y", categoryCount[1]);
            chartData.add(map);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysAgo = now.minusDays(7);
        List<Integer> totalSumpaymentsLast7Days = new ArrayList<>();
        for (int day = 0; day < 7; day++) {
            LocalDateTime dayStart = sevenDaysAgo.plusDays(day);
            LocalDateTime dayEnd = dayStart.plusDays(1);
            Integer sumpayment = orderRepository.findSumpaymentOrderForLast7Days("Thanh toán thành công", dayStart, dayEnd);
            totalSumpaymentsLast7Days.add(sumpayment);
        }

        List<Object[]> categoryCountsLast7Days = orderRepository
                .countProductByCategoryAndStatusForLast7Days(sevenDaysAgo, now);
        List<Map<String, Object>> chartDataLast7Days = new ArrayList<>();
        for (Object[] categoryCount : categoryCountsLast7Days) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", categoryCount[0]);
            map.put("y", categoryCount[1]);
            chartDataLast7Days.add(map);
        }

        model.addAttribute("chartDataLast7Days", chartDataLast7Days);

        model.addAttribute("totalSumpaymentsLast7Days", totalSumpaymentsLast7Days);

        model.addAttribute("chartData", chartData);
        model.addAttribute("message", totalSumpayments);

        return "admin/charts";
    }

}
