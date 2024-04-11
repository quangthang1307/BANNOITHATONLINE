package poly.edu.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
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

import poly.edu.entity.Inventory;
import poly.edu.entity.Order;
import poly.edu.entity.Orderdetails;
import poly.edu.entity.StoclMovement;
import poly.edu.repository.InventoryRepository;
import poly.edu.repository.OrderDetailRepository;
import poly.edu.repository.OrderRepository;
import poly.edu.repository.StoclMovementRepository;

@CrossOrigin("*")
@RestController
public class OrderDetailRestController {
    @Autowired OrderDetailRepository orderDetailRepository;
    @Autowired InventoryRepository inventoryRepository;
    @Autowired OrderRepository orderRepository;
    @Autowired StoclMovementRepository stoclMovementRepository;
    
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
        Inventory inventory = inventoryRepository.findByProduct(orderdetails.getProduct());
        if(inventory != null && inventory.getQuantityonhand() > 0){
            inventory.setQuantityonhand(inventory.getQuantityonhand() - orderdetails.getProductquantity());
            inventory.setLastupdatedate(new Date());
            inventoryRepository.save(inventory);
            orderDetailRepository.save(orderdetails);

            StoclMovement stoclMovement = new StoclMovement();
            stoclMovement.setMovementdate(LocalDateTime.now());
            stoclMovement.setProduct(orderdetails.getProduct());
            stoclMovement.setQuantity(orderdetails.getProductquantity());
            stoclMovement.setWarehouses(inventory.getWarehouse());
            stoclMovement.setMovementtype(true);
            stoclMovementRepository.save(stoclMovement);
            return ResponseEntity.ok(orderdetails);
        } else{
            Optional<Order> order = orderRepository.findById(orderdetails.getOrder().getOrderID());
            if(order.isPresent()){
                orderRepository.delete(order.get());
            }
            return ResponseEntity.badRequest().build();
        }

        
    }


}
