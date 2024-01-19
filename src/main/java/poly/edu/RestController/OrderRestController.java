package poly.edu.RestController;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import poly.edu.Service.OrderService;
import poly.edu.entity.Order;

@CrossOrigin("*")
@RestController
public class OrderRestController {
    @Autowired OrderService orderService;
    
    @PostMapping("/rest/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody Order order){
        Order newOrder = orderService.createdOrder(order);
        return new ResponseEntity<>(newOrder, HttpStatus.OK);
    }

}
