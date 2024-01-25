package poly.edu.Service;

import java.util.List;

import poly.edu.entity.Order;

public interface OrderService {
    Order createdOrder(Order order);
    List<Order> getOrderListByCustomerId(Integer customerId);
}
