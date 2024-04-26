package poly.edu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import poly.edu.Service.CronProvider;
import poly.edu.Service.DoanhThuTelegram;
import poly.edu.entity.Telegram;

@SpringBootApplication
@EnableScheduling
public class BannoithatonlineApplication {
	@Autowired DoanhThuTelegram doanhThuTelegram;
	@Autowired 
    private CronProvider cronProvider;

	public static void main(String[] args) {
		SpringApplication.run(BannoithatonlineApplication.class, args);

		
	}
	

	@Scheduled(cron = "#{@cronProvider.getCronValue()}", zone = "GMT+7")
    public void runTaskAt12GMT7() {
        doanhThuTelegram.doanhThu();
        System.out.println("Đã chạy đoạn code vào 12h GMT+7.");
    }

}
