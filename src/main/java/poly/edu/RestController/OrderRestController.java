package poly.edu.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import poly.edu.Service.CartService;
import poly.edu.Service.DiscountService;
import poly.edu.Service.OrderService;
import poly.edu.entity.Cart;
import poly.edu.entity.Discount;
import poly.edu.entity.Order;
import poly.edu.repository.OrderRepository;

@CrossOrigin("*")
@RestController
public class OrderRestController {
    @Autowired
    OrderService orderService;
    @Autowired
    DiscountService discountService;
    @Autowired
    CartService cartService;

    @Autowired
    OrderRepository orderRepository;
    
    

    @PostMapping("/rest/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        Order newOrder = orderService.createdOrder(order);
        
        try {
            if (order.getDiscount().getDiscountId() != null) {
                Optional<Discount> discount = discountService.findById(order.getDiscount().getDiscountId());
                if(discount.isPresent()) {
                    discount.get().setQuantityUsed(discount.get().getQuantityUsed()+1);
                    discountService.update(discount.get());
                }
            }

        } catch (Exception e) {}
        

        return new ResponseEntity<>(newOrder, HttpStatus.OK);
    }

    @DeleteMapping("/rest/deleteProductInCartByCustomerId")
    public ResponseEntity<?> deleteProductInCartByCustomerId(@RequestParam Integer customerId, @RequestParam Integer productId){
        Optional<Cart> cart = cartService.findByCustomerAndProduct(customerId, productId);
        if(cart.isPresent()){
            cartService.delete(cart.get().getCartId());
        }
        
        return ResponseEntity.noContent().build();
    }

}
