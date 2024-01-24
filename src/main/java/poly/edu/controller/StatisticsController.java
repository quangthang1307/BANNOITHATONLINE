package poly.edu.controller;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import poly.edu.repository.OrderRepository;

@Controller
public class StatisticsController {

    @Autowired
    OrderRepository orderRepository;

    @RequestMapping("/admin/statistics")
    public String showstatistics(Model model, @RequestParam(name = "selectedYear", required = false) Integer selectedYear) {
        // List<Integer> years = orderRepository.findDistinctYears();

        // Integer year = 2024;

        // List<Integer> sumpayments = new ArrayList<>();
        // List<Integer> sumpaymentWithVNPays = new ArrayList<>();

        // for (int month = 1; month <= 12; month++) {
        // Integer sumpayment = orderRepository.findSumpaymentOrder("Thanh Toán", month,
        // year);
        // Integer sumpaymentWithVNPay = orderRepository.findTransactionsOrder("Thanh
        // toán VNPay", month, year);

        // sumpayments.add(sumpayment);
        // sumpaymentWithVNPays.add(sumpaymentWithVNPay);

        // }

        // model.addAttribute("message", sumpayments);
        // model.addAttribute("message2", sumpaymentWithVNPays);
        // int yearHientai = LocalDate.now().getYear();    

        // List<Integer> yearArrays = new ArrayList<>();
        // yearArrays.add(2023);
        // System.out.println(yearArrays);

        // if(yearHientai != yearArrays.get(yearArrays.size() - 1)){

        //     yearArrays.add(yearHientai);
        // }

        // System.out.println("Đây là kết quả cuối cùng: " + yearArrays.get(yearArrays.size() - 1));

        if (selectedYear == null) {
            // Nếu không có năm được chọn, sử dụng năm hiện tại
            selectedYear = Year.now().getValue();
        }

        List<Integer> years = orderRepository.findDistinctYears();
        model.addAttribute("years", years);
        model.addAttribute("selectedYear", selectedYear);

        List<Integer> totalSumpayments = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            Integer sumpayment = orderRepository.findSumpaymentOrder("Thanh Toán", month, selectedYear);
            totalSumpayments.add(sumpayment);
        }
        model.addAttribute("message", totalSumpayments);

        return "admin/charts";
    }

}
