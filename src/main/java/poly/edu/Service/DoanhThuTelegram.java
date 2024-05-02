package poly.edu.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import poly.edu.entity.Order;
import poly.edu.entity.Orderdetails;
import poly.edu.entity.Telegram;
import poly.edu.repository.OrderDetailRepository;
import poly.edu.repository.OrderRepository;
import poly.edu.repository.TelegramRepository;

@Service
public class DoanhThuTelegram {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    TelegramRepository telegramRepository;

    public void doanhThu() {
        Double doanhThuDatHang = 0.0;
        Double doanhThuThanhCong = 0.0;

        List<Order> order = orderRepository.findAll();
        List<Order> ordernow = new ArrayList<>();

        if(order.isEmpty()) {
            // Thực hiện các hành động tương ứng khi danh sách order trống
            System.out.println("Không có đơn hàng nào.");
            return; // Trả về ngay sau khi kiểm tra danh sách order trống
        }

        for (Order order2 : order) {
            if (order2.getTime().toLocalDate().isEqual(LocalDate.now())) {
                ordernow.add(order2);
            }
        }

        for (Order order2 : ordernow) {
            doanhThuDatHang += order2.getSumpayment();
        }

        for (Order order2 : ordernow) {
            List<Orderdetails> orderdetails = orderDetailRepository.getOrderdetailsByOrderID(order2.getOrderID());
            for (Orderdetails orderdetail : orderdetails) {
                if (orderdetail.getOrder().getOrderstatus().getOrderstatusname().equals("Thanh toán thành công")) {
                    doanhThuThanhCong += orderdetail.getTotalpayment();
                }
            }
        }

        DecimalFormat df = new DecimalFormat("#,##0");

        String message = "Doanh thu hôm nay - " + LocalDate.now() + "\n"
                + "Doanh thu các đơn đặt hàng: " + df.format(doanhThuDatHang) + "\n"
                + "Doanh thu các đơn thành công: " + df.format(doanhThuThanhCong) + "\n";
        Telegram telegram = telegramRepository.findByMission(Telegram.MissionType.THONGKE);
        if (telegram != null) {
            TelegramBot bot = new TelegramBot(telegram.getBotToken());
            SendMessage send = new SendMessage(telegram.getChatId(), message);
            SendResponse response = bot.execute(send);
        }
    }
}
