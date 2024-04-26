package poly.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class BannoithatonlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(BannoithatonlineApplication.class, args);

		
	}

	@Scheduled(cron = "0 32 15 * * *", zone = "GMT+7")
    public void runTaskAt12GMT7() {
        // Thực hiện đoạn code bạn muốn chạy vào 12h GMT+7 ở đây
        System.out.println("Đã chạy đoạn code vào 12h GMT+7.");
    }

}
