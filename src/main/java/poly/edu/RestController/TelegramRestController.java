package poly.edu.RestController;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import poly.edu.entity.Order;
import poly.edu.entity.Orderdetails;
import poly.edu.repository.OrderDetailRepository;
import poly.edu.repository.OrderRepository;

@RestController
@CrossOrigin("*")
public class TelegramRestController {

    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    OrderRepository orderRepository;

    @PostMapping("/rest/telegram/notification")
    public boolean notification(@RequestBody Map<String, Object> data) throws JsonProcessingException {
        String username = null;        
        String sanpham = null;
        String phone = null;
        String tongtien = null;
        String time = null;
        String name = null;
        int orderID = (int) data.get("data");
        Optional<Order> order = orderRepository.findById(orderID);
        List<Orderdetails> orderDetail = orderDetailRepository.getOrderdetailsByOrderID(orderID);
        StringBuilder concatenatedNames = new StringBuilder();

        // Duyệt qua từng order detail để lấy tên sản phẩm và nối vào chuỗi
        for (Orderdetails orderdetails : orderDetail) {
            // Lấy tên sản phẩm và nối vào chuỗi
            concatenatedNames.append(orderdetails.getProduct().getProductname()).append(" x ");

            // Lấy số lượng sản phẩm và nối vào chuỗi
            concatenatedNames.append(orderdetails.getProductquantity()).append(", ");
        }
        // Xóa dấu phẩy cuối cùng và khoảng trắng thừa
        String finalNames = concatenatedNames.toString().replaceAll(", $", "");
        sanpham = finalNames;
        

        if(order.isPresent()){
            if(order.get().getCustomer().getAccount().getUsername() != null || !order.get().getCustomer().getAccount().getUsername().isEmpty()){
                username = order.get().getCustomer().getAccount().getUsername();
            }
            if(order.get().getCustomer().getPhone() != null || !order.get().getCustomer().getPhone().isEmpty()){
                phone = order.get().getCustomer().getPhone();
            }
            if(order.get().getTime() != null){
                time = order.get().getTime().toString().substring(0, 10);
            }
            if(order.get().getCustomer().getName() != null || !order.get().getCustomer().getName().isEmpty()){
                name = order.get().getCustomer().getName();
            }
                DecimalFormat decimalFormat = new DecimalFormat("#,### VND");                
                tongtien = decimalFormat.format(Double.parseDouble(order.get().getSumpayment().toString()));
        }

         


        try {
            TelegramBot bot = new TelegramBot("6853009990:AAHzyQYieOgmEUBr6cAI8-OXGQD_t_GHVW4");
            SendMessage send = new SendMessage("5884779776", String.valueOf("Đơn hàng mới!\n" + "Họ và tên: " + name + "\n" + 
            "Username: " + username + "\n" + "Số điện thoại: " + phone + "\n" + "Ngày đặt hàng: " + time + "\n" + "Sản phẩm: " + sanpham + "\n" + 
            "Tổng tiền: " + tongtien));
            SendResponse response = bot.execute(send);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }
}
