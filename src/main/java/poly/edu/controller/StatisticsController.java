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
    public String showstatistics(Model model){

        Integer year = 2024;
        List<Integer> sumpayments = new ArrayList<>();
        for(int month = 1; month <= 12; month ++){
            Integer sumpayment  = orderRepository.findSumpaymentOrder("Thanh Toán", month, year);
            sumpayments.add(sumpayment);
        }
        model.addAttribute("message", sumpayments);
        return "admin/charts";
    }

}
