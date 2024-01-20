package poly.edu.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import poly.edu.Service.VNPayService;

@Controller
public class PaymentController {

    @Autowired
    private VNPayService vnPayService;


    @RequestMapping("/vnpay-payment")
	public String GetMapping(HttpServletRequest request, Model model){
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        String vnp_TransactionStatus = request.getParameter("vnp_TransactionStatus");
        System.out.println(vnp_TransactionStatus);
        

        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
    }
    
}
