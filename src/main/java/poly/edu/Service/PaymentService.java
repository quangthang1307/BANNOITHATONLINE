package poly.edu.Service;

import java.util.List;
import java.util.Optional;

import poly.edu.entity.Payment;

public interface PaymentService {
    List<Payment> getPayments();
    Optional<Payment> getPayment(Integer paymentId);

}
