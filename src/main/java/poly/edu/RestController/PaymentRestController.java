package poly.edu.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import net.minidev.json.JSONObject;
import poly.edu.Service.PaymentService;
import poly.edu.Service.VNPayService;
import poly.edu.entity.Payment;

@CrossOrigin("*")
@RestController
public class PaymentRestController {
    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/rest/vnpay")
    public ResponseEntity<PaymentResponse> submitOrder(@RequestBody Map<String, Object> requestBody,
            HttpServletRequest request) {

        int orderTotal = (int) requestBody.get("amount");
        String orderInfo = (String) requestBody.get("orderInfo");
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", vnpayUrl);

        PaymentResponse response = new PaymentResponse(vnpayUrl, "Order created successfully");
        return ResponseEntity.ok(response);

    }

    

    @Data
    public class PaymentResponse {
        private String vnpayUrl;
        private String message; // Có thể thêm các thông tin khác bạn muốn trả về

        // Constructors, getters, setters

        public PaymentResponse(String vnpayUrl, String message) {
            this.vnpayUrl = vnpayUrl;
            this.message = message;
        }

        // Getters và setters
    }



    @GetMapping("/rest/checkAllOptionPayment")
    public ResponseEntity<?> getAllOptionPayment(){
        List<Payment> payment = paymentService.getPayments();
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }

    @GetMapping("/rest/checkOptionPayment/{paymentId}")
    public ResponseEntity<?> getOptionPayment(@PathVariable Integer paymentId){
        Optional<Payment> payment = paymentService.getPayment(paymentId);
        return new ResponseEntity<>(payment.get(), HttpStatus.OK);
    }


}












// @GetMapping("/vnpay-payment")
    // public ResponseEntity<?> getPayment(HttpServletRequest request) {
    //     int paymentStatus = vnPayService.orderReturn(request);

    //     String orderInfo = request.getParameter("vnp_OrderInfo");
    //     String paymentTime = request.getParameter("vnp_PayDate");
    //     String transactionId = request.getParameter("vnp_TransactionNo");
    //     String totalPrice = request.getParameter("vnp_Amount");

    //     // Tạo một JSON response thay vì sử dụng Model
    //     JSONObject responseJson = new JSONObject();
    //     responseJson.put("orderId", orderInfo);
    //     responseJson.put("totalPrice", totalPrice);
    //     responseJson.put("paymentTime", paymentTime);
    //     responseJson.put("transactionId", transactionId);
    //     responseJson.put("paymentStatus", paymentStatus);

    //     HttpStatus httpStatus = (paymentStatus == 1) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

    //     System.out.println(responseJson);

    //     if (paymentStatus == 1) {
    //         PaymentResponse response = new PaymentResponse(responseJson.toString(), "Order created successfully");
    //         return ResponseEntity.ok(responseJson);
    //     } else {
    //         PaymentResponse response = new PaymentResponse(responseJson.toString(), "Order created failed");
    //         return new ResponseEntity<>(responseJson, HttpStatus.BAD_REQUEST);
    //     }
    // }

    // @GetMapping("/vnpay-payment1")
    // public ResponseEntity<?> getPayment1(HttpServletRequest request,
    //         @RequestParam Integer vnp_Amount,
    //         @RequestParam String vnp_BankCode,
    //         @RequestParam String vnp_CardType,
    //         @RequestParam String vnp_OrderInfo,
    //         @RequestParam String vnp_PayDate,
    //         @RequestParam Integer vnp_ResponseCode,
    //         @RequestParam String vnp_TmnCode,
    //         @RequestParam Integer vnp_TransactionNo,
    //         @RequestParam String vnp_TransactionStatus,
    //         @RequestParam String vnp_TxnRef,
    //         @RequestParam String vnp_SecureHash) {

    //     int paymentStatus = vnPayService.orderReturn(request);

    //     String orderInfo = request.getParameter("vnp_OrderInfo");
    //     String paymentTime = request.getParameter("vnp_PayDate");
    //     String transactionId = request.getParameter("vnp_TransactionNo");
    //     String totalPrice = request.getParameter("vnp_Amount");
    //     String TransactionStatus = request.getParameter("vnp_TransactionStatus");
    //     Map<String, String> response = new HashMap<>();
    //     response.put("orderId", orderInfo);
    //     response.put("totalPrice", totalPrice);
    //     response.put("paymentTime", paymentTime);
    //     response.put("transactionId", transactionId);
    //     System.out.println(TransactionStatus);
        
    //     response.put("paymentStatus", String.valueOf(paymentStatus));
    //     // response.put("data", "quadinh");

    //     return new ResponseEntity<>(response, HttpStatus.OK);
    // }

    // @GetMapping("/rest/test")
    // public ResponseEntity<Map<String, String>> getString(@RequestParam String customerid) {
    //     Map<String, String> response = new HashMap<>();
    //     response.put("data", "quadinh");
    //     return new ResponseEntity<>(response, HttpStatus.OK);
    // }