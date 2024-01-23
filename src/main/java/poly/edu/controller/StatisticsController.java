package poly.edu.controller;

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

        model.addAttribute("message", orderRepository.findSumpaymentOrder(1, 2024));
        return "admin/charts";
    }

}
