package poly.edu.RestController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import net.minidev.json.JSONObject;
import poly.edu.Service.VNPayService;

@CrossOrigin("*")
@RestController
public class PaymentRestController {
    @Autowired
    private VNPayService vnPayService;

    @PostMapping("/rest/vnpay")
    public ResponseEntity<PaymentResponse> submitOrder(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) {
        
        int orderTotal = (int) requestBody.get("amount");
        String orderInfo = (String) requestBody.get("orderInfo");
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", vnpayUrl);

        PaymentResponse response = new PaymentResponse(vnpayUrl, "Order created successfully");
        return ResponseEntity.ok(response);
        
    }

    @GetMapping("/vnpay-payment")
    public ResponseEntity<PaymentResponse> getPayment(HttpServletRequest request) {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        // Tạo một JSON response thay vì sử dụng Model
        JSONObject responseJson = new JSONObject();
        responseJson.put("orderId", orderInfo);
        responseJson.put("totalPrice", totalPrice);
        responseJson.put("paymentTime", paymentTime);
        responseJson.put("transactionId", transactionId);
        responseJson.put("paymentStatus", paymentStatus);

        HttpStatus httpStatus = (paymentStatus == 1) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        System.out.println(responseJson);

        if(paymentStatus == 1) {
            PaymentResponse response = new PaymentResponse(responseJson.toString(), "Order created successfully");
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.badRequest().build();
        }

        
    }


    @Data
    public class PaymentResponse {
        private String vnpayUrl;
        private String message;  // Có thể thêm các thông tin khác bạn muốn trả về
    
        // Constructors, getters, setters
    
        public PaymentResponse(String vnpayUrl, String message) {
            this.vnpayUrl = vnpayUrl;
            this.message = message;
        }
    
        // Getters và setters
    }
    

}

