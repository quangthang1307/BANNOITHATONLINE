package poly.edu.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Service.PaymentService;
import poly.edu.entity.Payment;
import poly.edu.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired PaymentRepository paymentRepository;

    @Override
    public List<Payment> getPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Optional<Payment> getPayment(Integer paymentId) {
       return paymentRepository.findById(paymentId);
    }

}
