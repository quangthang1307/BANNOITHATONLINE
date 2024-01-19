package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import poly.edu.entity.Order;

public interface OrderRepository extends JpaRepository<Order,Integer> {

  
}
