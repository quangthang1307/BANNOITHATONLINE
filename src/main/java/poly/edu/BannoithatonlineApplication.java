package poly.edu;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.context.annotation.ComponentScan;
import poly.edu.Service.DoanhThuTelegram;
import poly.edu.entity.Telegram;
import poly.edu.repository.TelegramRepository;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "poly.edu")
public class BannoithatonlineApplication {
    @Autowired 
    private DoanhThuTelegram doanhThuTelegram;


	@Autowired 
    TelegramRepository telegramRepository;

    public static void main(String[] args) {
        SpringApplication.run(BannoithatonlineApplication.class, args);
    }

    @Scheduled(cron = "0 * * * * *", zone = "GMT+7")
    public void runTaskAt12GMT7() {
        Telegram telegram = telegramRepository.findByMission(Telegram.MissionType.THONGKE);
		if(telegram != null){			
			if(localTimeToCron(LocalTime.now()).equals(telegram.getTime())){
				doanhThuTelegram.doanhThu();
			}
		}
    }

	public static String localTimeToCron(LocalTime localTime) {
		int hour = localTime.getHour();
		int minute = localTime.getMinute();
		int second = localTime.getSecond();
		String cronExpression = String.format("%d %d %d * * *", second, minute, hour);	
		return cronExpression;
	}
}



