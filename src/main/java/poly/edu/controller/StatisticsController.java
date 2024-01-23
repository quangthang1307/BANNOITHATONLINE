package poly.edu.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.edu.repository.OrderRepository;

@Controller
public class StatisticsController {

    @Autowired
    OrderRepository orderRepository;

    @RequestMapping("/admin/statistics")
    public String showstatistics(Model model) {

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

        Integer year = 2024;
        List<Integer> totalSumpayments = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            
            Integer sumpayment = orderRepository.findSumpaymentOrder("Thanh Toán", month,
             year, 2);
            Integer sumpaymentWithVNPay = orderRepository.findTransactionsOrder("Thanh toán VNPay", month, year);

            if(sumpaymentWithVNPay == null){
                sumpaymentWithVNPay = 0;
            }

            if(sumpayment == null){
                sumpayment = 0;
            }

            totalSumpayments.add(sumpayment + sumpaymentWithVNPay);
        }
        model.addAttribute("message", totalSumpayments);

        return "admin/charts";
    }

}
