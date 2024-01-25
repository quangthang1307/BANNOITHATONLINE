package poly.edu.RestController;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.GetMapping;
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
import poly.edu.entity.DiscountUsage;
import poly.edu.entity.Order;
import poly.edu.entity.Orderdetails;
import poly.edu.entity.Transaction;
import poly.edu.repository.DiscountUsageRepository;
import poly.edu.repository.OrderDetailRepository;
import poly.edu.repository.OrderRepository;
import poly.edu.repository.TransactionRepository;

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

    @Autowired
    DiscountUsageRepository discountUsageRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @PostMapping("/rest/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        order.setTime(LocalDateTime.now());
        orderService.createdOrder(order);
        try {
            if (order.getDiscount().getDiscountId() != null) {
                Optional<Discount> discount = discountService.findById(order.getDiscount().getDiscountId());
                List<DiscountUsage> usageList = discountUsageRepository
                        .findAllByCustomerId(order.getCustomer().getCustomerId());
                if (usageList.size() < discount.get().getMaxUsage()) {
                    if (discount.isPresent()) {
                        discount.get().setQuantityUsed(discount.get().getQuantityUsed() + 1);
                        discountService.update(discount.get());
                        DiscountUsage usage = new DiscountUsage();
                        usage.setCustomer(order.getCustomer());
                        usage.setDiscount(order.getDiscount());
                        usage.setUseddate(LocalDateTime.now());
                        discountUsageRepository.save(usage);
                    }
                }
            }

        } catch (Exception e) {
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/rest/deleteProductInCartByCustomerId")
    public ResponseEntity<?> deleteProductInCartByCustomerId(@RequestParam Integer customerId,
            @RequestParam Integer productId) {
        Optional<Cart> cart = cartService.findByCustomerAndProduct(customerId, productId);
        if (cart.isPresent()) {
            cartService.delete(cart.get().getCartId());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rest/orderByCustomer")
    public ResponseEntity<?> orderByCustomer(@RequestParam Integer customerId) {
        List<Order> order = orderService.getOrderListByCustomerId(customerId);
        if (order.size() > 0) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.noContent().build();
        }

    }

    @DeleteMapping("/rest/deleteOrder")
    public void deleteOrder(@RequestParam Integer orderId) {
        List<Orderdetails> orderDetails = orderDetailRepository.getOrderdetailsByOrderID(orderId);
        Optional<Order> order = orderRepository.findById(orderId);

        Transaction transaction = transactionRepository.findByOrder(order.get());
        if (transaction != null) {
            transactionRepository.delete(transaction);
            orderDetailRepository.deleteAll(orderDetails);
            orderRepository.deleteById(orderId);
        } else {
            orderDetailRepository.deleteAll(orderDetails);
            orderRepository.deleteById(orderId);
        }

    }

}
