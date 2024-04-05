package poly.edu.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Service.OrderStatusService;
import poly.edu.entity.Orderstatus;
import poly.edu.repository.OrderStatusRepository;

@Service
public class OrderStatusServiceImpl implements OrderStatusService {

    @Autowired
	private OrderStatusRepository orderStatusRepository;

    @Override
    public Orderstatus findByStatusName(String statusName) {
        // TODO Auto-generated method stub
		return orderStatusRepository.findByStatusName(statusName);
    }

}
