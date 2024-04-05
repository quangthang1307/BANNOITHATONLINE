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

    @Override
	public Order findById(Integer id) {
		// TODO Auto-generated method stub
		return orderRepository.findById(id).orElse(null);
	}

	@Override
	public Order save(Order order) {
		// TODO Auto-generated method stub
		return orderRepository.save(order);
	}
	
	@Override
	public List<Order> findByCustomerId(int id) {
		return orderRepository.findByCustomerid(id);
	}
	
	@Override
	public void deleteOrder(Integer orderId) {
		Order order = orderRepository.findById(orderId).orElse(null);
		if (order != null) {
			// Xóa đơn hàng
			orderRepository.delete(order);
		}
	}
}
