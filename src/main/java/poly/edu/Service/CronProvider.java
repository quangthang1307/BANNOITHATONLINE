package poly.edu.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.edu.entity.Telegram;
import poly.edu.repository.TelegramRepository;

@Service
public class CronProvider {
    @Autowired TelegramRepository telegramRepository;

    public String getCronThongKe() {
        Telegram telegram = telegramRepository.findByMission(Telegram.MissionType.THONGKE);
        if (telegram != null) {
            System.out.println(telegram);
            return telegram.getTime();
        } else {
            // Xử lý trường hợp không tìm thấy Telegram có mission là THONGKE
            return "0 55 23 * * *"; // hoặc bạn có thể trả về một giá trị mặc định khác tùy theo logic của ứng dụng
        }
    }
}