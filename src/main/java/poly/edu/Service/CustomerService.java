package poly.edu.Service;

import poly.edu.entity.Customer;

public interface CustomerService {

    Customer findByName(String name);

    Customer findByPhone(String phone);

}
