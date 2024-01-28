package poly.edu.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import poly.edu.Service.VNPayService;
import poly.edu.repository.TransactionRepository;

@Controller
public class PaymentController {

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    TransactionRepository transactionRepository;


    @RequestMapping("/vnpay-payment")
	public String GetMapping(HttpServletRequest request, Model model){
        int paymentStatus = vnPayService.orderReturn(request);
        

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        int tongTien = Integer.valueOf(totalPrice);
        String vnp_Amount = request.getParameter("vnp_Amount");
        String vnp_BankCode = request.getParameter("vnp_BankCode");


        model.addAttribute("vnp_Amount", vnp_Amount);
        model.addAttribute("vnp_BankCode", vnp_BankCode);
        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", tongTien/100);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        String vnp_TransactionStatus = request.getParameter("vnp_TransactionStatus");
        model.addAttribute("vnp_TransactionStatus", vnp_TransactionStatus);
        

        return "ordersuccess";
    }
    
}
