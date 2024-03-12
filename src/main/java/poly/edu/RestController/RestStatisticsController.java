package poly.edu.RestController;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import poly.edu.repository.OrderRepository;

@RestController
@CrossOrigin("*")
public class RestStatisticsController {

    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/admin/rest/statics/data")
    public ResponseEntity<List<Integer>> getStatisticData(
            @RequestParam(name = "selectedYear", required = false) Integer selectedYear) {
        if (selectedYear == null) {
            selectedYear = Year.now().getValue();
        }

        List<Integer> totatlSumpayments = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            Integer sumpayment = orderRepository.findSumpaymentOrder("Thanh toán", month, selectedYear);

            totatlSumpayments.add(sumpayment);
        }

        return ResponseEntity.ok(totatlSumpayments);
    }

    @GetMapping("/admin/rest/statics/categoryData")
    public ResponseEntity<List<Map<String, Object>>> getCategoryData(
            @RequestParam(name = "selectedYear", required = false) Integer selectedYear) {
        if (selectedYear == null) {
            selectedYear = Year.now().getValue();
        }

        List<Object[]> categoryData = orderRepository.countProductByCategoryAndStatus(selectedYear);
        List<Map<String, Object>> responseData = new ArrayList<>();
        for (Object[] object : categoryData) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", object[0]);
            map.put("y", object[1]);
            responseData.add(map);
        }

        return ResponseEntity.ok(responseData);
    }

}
