package poly.edu.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import poly.edu.entity.Order;

public interface OrderService {
    Order createdOrder(Order order);

    List<Order> getOrderListByCustomerId(Integer customerId);

    Order findById(Integer id);

    List<Order> findByCustomerId(int id);

    Order save(Order order);

    void deleteOrder(Integer orderId);
}
