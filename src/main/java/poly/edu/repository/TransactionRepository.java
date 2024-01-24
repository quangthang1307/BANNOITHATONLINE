package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.edu.entity.Order;
import poly.edu.entity.Transaction;
import java.util.List;
import java.util.Optional;


public interface TransactionRepository extends JpaRepository<Transaction, Integer>{

    Transaction findByOrder(Order order);
    
    
}
