package poly.edu.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import poly.edu.entity.Order;
import poly.edu.entity.Orderdetails;
import poly.edu.repository.OrderDetailRepository;
import poly.edu.repository.OrderRepository;

@Service
public class DoanhThuTelegram {
    @Autowired OrderRepository orderRepository;
    @Autowired OrderDetailRepository orderDetailRepository;

    public void doanhThu(){
        List<Order> order = orderRepository.findAll();
        List<Order> ordernow = new ArrayList<>();

        for (Order order2 : order) {
            if(order2.getTime().toLocalDate().isEqual(LocalDate.now())){
                ordernow.add(order2);
            }
        }

        Double doanhThuDatHang = 0.0;
        Double doanhThuThanhCong =  0.0;



        for (Order order2 : ordernow) {
            doanhThuDatHang += order2.getSumpayment();
        }

        for (Order order2 : ordernow) {
            List<Orderdetails> orderdetails = orderDetailRepository.getOrderdetailsByOrderID(order2.getOrderID());
            for (Orderdetails orderdetail : orderdetails) {
                if(orderdetail.getOrder().getOrderstatus().getOrderstatusname().equals("Thành công")){
                    doanhThuThanhCong+= orderdetail.getTotalpayment();
                }
            }

        }

        DecimalFormat df = new DecimalFormat("#,##0");

        String message = "Doanh thu hôm nay - " + LocalDate.now() + "\n" 
        + "Doanh thu các đơn đặt hàng: " +  df.format(doanhThuDatHang) + "\n"
        + "Doanh thu các đơn thành công: " +  df.format(doanhThuThanhCong) + "\n"
        ;

        TelegramBot bot = new TelegramBot("7041078559:AAFBCCotM3Oipx2qNDeUelRT0RnvzmQO4sk");
        SendMessage send = new SendMessage("5884779776", message);
        SendResponse response = bot.execute(send);
        
    }
}
