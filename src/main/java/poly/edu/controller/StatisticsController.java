package poly.edu.controller;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

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
    public String showstatistics(Model model, @RequestParam(name = "selectedYear", required = false) Integer selectedYear) {

        if (selectedYear == null) {
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
