package poly.edu.RestController;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import jakarta.servlet.http.HttpServletRequest;
import poly.edu.entity.Order;
import poly.edu.entity.Orderdetails;
import poly.edu.entity.Orderstatus;
import poly.edu.repository.OrderDetailRepository;
import poly.edu.repository.OrderRepository;
import poly.edu.repository.OrderStatusRepository;

@RestController
@CrossOrigin("*")
public class TelegramRestController {

    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderStatusRepository orderStatusRepository;

    @Autowired
    HttpServletRequest request;

    @GetMapping("/api/telegram/approve")
    public String approve(@RequestParam Integer orderID) {
        Optional<Order> order = orderRepository.findById(orderID);
        if (order.isPresent()) {
            if (order.get().getOrderstatus().getOrderstatusname().equals("Chờ xác nhận")) {
                Orderstatus orderstatus = orderStatusRepository.findByOrderStatusName("Đã xác nhận");
                order.get().setOrderstatus(orderstatus);
                orderRepository.save(order.get());
                return "Duyệt thành công!";
            }else{
                return "Trạng thái đơn hàng: " + order.get().getOrderstatus().getOrderstatusname() ;
            }

        } else {
            return "Duyệt thất bại";
        }
    }

    @PostMapping("/rest/telegram/notification")
    public boolean notification(@RequestBody Map<String, Object> data) throws JsonProcessingException {
        String username = null;
        String sanpham = null;
        String phone = null;
        String tongtien = null;
        String time = null;
        String name = null;
        String link = null;
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

        if (order.isPresent()) {
            if (order.get().getCustomer().getAccount().getUsername() != null
                    || !order.get().getCustomer().getAccount().getUsername().isEmpty()) {
                username = order.get().getCustomer().getAccount().getUsername();
            }
            if (order.get().getCustomer().getPhone() != null || !order.get().getCustomer().getPhone().isEmpty()) {
                phone = order.get().getCustomer().getPhone();
            }
            if (order.get().getTime() != null) {
                time = order.get().getTime().toString().substring(0, 10);
            }
            if (order.get().getCustomer().getName() != null || !order.get().getCustomer().getName().isEmpty()) {
                name = order.get().getCustomer().getName();
            }

            DecimalFormat decimalFormat = new DecimalFormat("#,### VND");
            tongtien = decimalFormat.format(Double.parseDouble(order.get().getSumpayment().toString()));
            // String baseUrl =
            // request.getRequestURL().toString().replace(request.getRequestURI(),
            // request.getContextPath());
            // link = baseUrl + "/rest/telegram/approve?orderID=" + String.valueOf(orderID);
        }

        String message = "Đơn hàng mới!\n"
                + "*Mã đơn hàng:* " + orderID + "\n"
                + "*Họ và tên:* " + name + "\n"
                + "*Username:* " + username + "\n"
                + "*Số điện thoại:* " + phone + "\n"
                + "*Ngày đặt hàng:* " + time + "\n"
                + "*Sản phẩm:* " + sanpham + "\n"
                + "*Tổng tiền:* " + tongtien + "\n"
                + "Link duyệt đơn hàng: " + "[Ấn vào đây](http://www.localhost/api/telegram/approve?orderID="
                + String.valueOf(orderID) + ")";

        try {
            TelegramBot bot = new TelegramBot("6853009990:AAHzyQYieOgmEUBr6cAI8-OXGQD_t_GHVW4");
            SendMessage send = new SendMessage("5884779776", message).parseMode(ParseMode.Markdown);
            SendResponse response = bot.execute(send);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }
}
