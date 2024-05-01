package poly.edu.RestController;

import java.text.DecimalFormat;
import java.time.LocalDate;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import jakarta.servlet.http.HttpServletRequest;
import poly.edu.entity.Order;
import poly.edu.entity.Orderdetails;
import poly.edu.entity.Orderstatus;
import poly.edu.entity.Telegram;
import poly.edu.repository.OrderDetailRepository;
import poly.edu.repository.OrderRepository;
import poly.edu.repository.OrderStatusRepository;
import poly.edu.repository.TelegramRepository;

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
    TelegramRepository telegramRepository;

    @Autowired
    HttpServletRequest request;

    @GetMapping("/api/telegram/approve")
    public RedirectView approve(@RequestParam Integer orderID, @RequestParam String action,
            RedirectAttributes attributes) {
        Optional<Order> order = orderRepository.findById(orderID);
        if (order.isPresent()) {
            if (action.equals("approve")) {
                if (order.get().getOrderstatus().getOrderstatusname().equals("Chờ xác nhận")) {
                    Orderstatus orderstatus = orderStatusRepository.findByOrderStatusName("Đã xác nhận");
                    order.get().setOrderstatus(orderstatus);
                    orderRepository.save(order.get());
                } else {
                    attributes.addFlashAttribute("errorMessage", "Trạng thái đơn hàng không hợp lệ");
                }
            } else if (action.equals("cancel")) {
                if (order.get().getOrderstatus().getOrderstatusname().equals("Chờ xác nhận")) {
                    String madonhang = String.valueOf(order.get().getOrderID());
                    String name = order.get().getCustomer().getName();
                    String username = order.get().getCustomer().getAccount().getUsername();
                    String phone = order.get().getCustomer().getPhone();
                    String time = order.get().getTime().toString().substring(0, 10);
                    String sanpham = null;
                    List<Orderdetails> orderDetail = orderDetailRepository.getOrderdetailsByOrderID(orderID);
                    if (orderDetail.size() == 1) {
                        sanpham = orderDetail.get(0).getProduct().getProductname();
                    } else {
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
                    }

                    DecimalFormat decimalFormat = new DecimalFormat("#,### VND");
                    String tongtien = decimalFormat.format(Double.parseDouble(order.get().getSumpayment().toString()));

                    String message = "Đơn hủy!\n"
                            + "*Mã đơn hàng:* " + orderID + "\n"
                            + "*Họ và tên:* " + name + "\n"
                            + "*Username:* " + username + "\n"
                            + "*Số điện thoại:* " + phone + "\n"
                            + "*Ngày hủy:* " + time + "\n"
                            + "*Sản phẩm:* " + sanpham + "\n"
                            + "*Tổng tiền:* " + tongtien + "\n";
                    Telegram telegram = telegramRepository.findByMission(Telegram.MissionType.HUYHANG);
                    if (telegram != null) {
                        try {
                            TelegramBot bot = new TelegramBot(telegram.getBotToken());
                            SendMessage send = new SendMessage(telegram.getChatId(), message)
                                    .parseMode(ParseMode.Markdown);
                            SendResponse response = bot.execute(send);
                        } catch (Exception e) {
                        }
                    }
                    Orderstatus orderstatus = orderStatusRepository.findByOrderStatusName("Đã hủy");
                    order.get().setOrderstatus(orderstatus);
                    orderRepository.save(order.get());
                } else {
                    attributes.addFlashAttribute("errorMessage", "Trạng thái đơn hàng không hợp lệ");
                }
            }
        } else {
            attributes.addFlashAttribute("errorMessage", "Đơn hàng không tồn tại");
        }
        return new RedirectView("/admin/showinvoicedetails/" + orderID);
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
        
        if (orderDetail.size() == 1) {
            sanpham = orderDetail.get(0).getProduct().getProductname();
        } else {
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
        }

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
        String host = request.getServerName();
        String message = "Đơn hàng mới !\n"
                + "*Mã đơn hàng:* " + orderID + "\n"
                + "*Họ và tên:* " + name + "\n"
                + "*Username:* " + username + "\n"
                + "*Số điện thoại:* " + phone + "\n"
                + "*Ngày đặt hàng:* " + time + "\n"
                + "*Sản phẩm:* " + sanpham + "\n"
                + "*Tổng tiền:* " + tongtien + "\n"
                + "Link duyệt đơn hàng: " + "[Ấn vào đây](http://www." + host + "/admin/showinvoicedetails/"
                + String.valueOf(orderID) + ")";

        Telegram telegram = telegramRepository.findByMission(Telegram.MissionType.DATHANG);
        if (telegram != null) {
            try {
                TelegramBot bot = new TelegramBot(telegram.getBotToken());
                SendMessage send = new SendMessage(telegram.getChatId(), message).parseMode(ParseMode.Markdown);
                SendResponse response = bot.execute(send);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
