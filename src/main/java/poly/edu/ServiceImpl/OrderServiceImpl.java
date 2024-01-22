package poly.edu.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Service.OrderService;
import poly.edu.entity.Order;
import poly.edu.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired OrderRepository orderRepository;

    @Override
    public Order createdOrder(Order order) {
        return orderRepository.save(order);
    }

    

    @Override
    public List<Order> getOrderListByCustomerId(Integer customerId) {
        // TODO Auto-generated method stub
       return orderRepository.orderList(customerId);
    }

}
