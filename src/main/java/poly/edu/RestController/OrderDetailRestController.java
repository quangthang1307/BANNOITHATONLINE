package poly.edu.RestController;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import poly.edu.entity.Orderdetails;
import poly.edu.repository.OrderDetailRepository;

@CrossOrigin("*")
@RestController
public class OrderDetailRestController {
    @Autowired OrderDetailRepository orderDetailRepository;

    @GetMapping("/rest/orderDetailsByOrderID")
    public ResponseEntity<?> getOrderDetailsByOrderID(@RequestParam Integer orderId){
        List<Orderdetails> orderdetails = orderDetailRepository.getOrderdetailsByOrderID(orderId);
        if(orderdetails != null){            
            return ResponseEntity.ok(orderdetails);
        }else{
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/rest/createOrderDetail")
    public ResponseEntity<?> createOrderDetail(@RequestBody Orderdetails orderdetails){
        orderDetailRepository.save(orderdetails);
        return ResponseEntity.ok(orderdetails);
    }


}
