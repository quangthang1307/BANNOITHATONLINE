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
        System.out.println(telegram);
        return telegram.getTime();
    }
}
