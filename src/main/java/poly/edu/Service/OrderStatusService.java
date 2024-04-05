package poly.edu.Service;

import poly.edu.entity.Orderstatus;

public interface OrderStatusService {
    Orderstatus findByStatusName(String statusName);
}
