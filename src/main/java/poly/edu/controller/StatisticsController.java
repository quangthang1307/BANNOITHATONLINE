package poly.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StatisticsController {
    
    @RequestMapping("/admin/statistics")
    public String showstatistics(){
        return "admin/charts";
    }
}
