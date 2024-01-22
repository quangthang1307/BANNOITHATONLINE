package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.edu.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}
