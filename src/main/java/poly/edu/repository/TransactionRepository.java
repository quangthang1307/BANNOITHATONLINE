package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.edu.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer>{
    
}
