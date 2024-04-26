package poly.edu.controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

import poly.edu.entity.Telegram;
import poly.edu.repository.TelegramRepository;

@Controller
public class HomeController {

	@Autowired
	TelegramRepository telegramRepository;

	@RequestMapping("")
	public String showindex() {
		return "user/index";
	}

	@RequestMapping("/index")
	public String index() {
		return "user/index";
	}

	@RequestMapping("/admin")
	public String Adminindex() {
		return "redirect:/admin/index";
	}

	@RequestMapping("/admin/index")
	public String showAdminindex() {
		return "admin/index";
	}

	@GetMapping("/admin/bot")
	public String showBOT(Model model) {
		List<Telegram> telegramList = telegramRepository.findAll();
		if (telegramList.size() > 0) {
			for (Telegram telegram : telegramList) {
				if (telegram.getMission().equals(Telegram.MissionType.THONGKE)) {

					String cronExpression = telegram.getTime();

					// Phân tích cú pháp cron expression
					String[] parts = cronExpression.split(" ");

					// Lấy giá trị giây, phút và giờ
					int second = Integer.parseInt(parts[0]);
					int minute = Integer.parseInt(parts[1]);
					int hour = Integer.parseInt(parts[2]);

					// Tạo đối tượng LocalTime từ các giá trị trên
					LocalTime localTime = LocalTime.of(hour, minute, second);
					telegram.setTime(String.valueOf(localTime));
				}
			}
			model.addAttribute("telegramList", telegramList);
		}
		return "admin/bot";
	}

	@PostMapping("/admin/bot")
	public String showBOTPOST(Model model, Telegram telegram) {

		Telegram telegramFind = telegramRepository.findByBotToken(telegram.getBotToken());
		if (telegramFind != null) {
			telegramFind.setMission(telegram.getMission());
			if (telegram.getMission().equals(Telegram.MissionType.THONGKE)) {
				String timeString = telegram.getTime();
				LocalTime localTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));

				// Chuyển đổi thời gian sang định dạng cron
				int seconds = localTime.getSecond();
				int minutes = localTime.getMinute();
				int hours = localTime.getHour();

				// Định dạng lại thành chuỗi "0 30 17 * * *"
				String cronFormat = String.format("%d %d %d * * *", seconds, minutes, hours);
				telegramFind.setTime(cronFormat);
				telegramRepository.save(telegramFind);

			}
		} else {
			List<Telegram> errorTelegram = telegramRepository.findAll();
			for (Telegram te : errorTelegram) {
				if (telegram.getMission().equals(te.getMission())) {
					List<Telegram> telegramList = telegramRepository.findAll();
					if (telegramList.size() > 0) {
						for (Telegram telegram4 : telegramList) {
							if (telegram4.getMission().equals(Telegram.MissionType.THONGKE)) {
			
								String cronExpression = telegram4.getTime();
			
								// Phân tích cú pháp cron expression
								String[] parts = cronExpression.split(" ");
			
								// Lấy giá trị giây, phút và giờ
								int second = Integer.parseInt(parts[0]);
								int minute = Integer.parseInt(parts[1]);
								int hour = Integer.parseInt(parts[2]);
			
								// Tạo đối tượng LocalTime từ các giá trị trên
								LocalTime localTime = LocalTime.of(hour, minute, second);
								telegram4.setTime(String.valueOf(localTime));
							}
						}
						model.addAttribute("telegramList", telegramList);
					}
					model.addAttribute("error", "Nhiệm vụ cho mỗi BOT là duy nhất !");
					return "admin/bot";
				}
			}
			Telegram tele = new Telegram();
			if (telegram.getBotToken() != null) {
				TelegramBot bot = new TelegramBot(telegram.getBotToken());
				bot.setUpdatesListener(updates -> {
					// Xử lý từng cập nhật
					for (Update update : updates) {
						if (update.message() != null && update.message().text() != null) {
							if (update.message().text().equals("/start")) {
								String chatId = String.valueOf(update.message().chat().id());
								tele.setBotToken(telegram.getBotToken());
								tele.setChatId(chatId);
								tele.setMission(telegram.getMission());
								if (tele.getMission().equals(Telegram.MissionType.THONGKE)) {
									String timeString = telegram.getTime();
									LocalTime localTime = LocalTime.parse(timeString,
											DateTimeFormatter.ofPattern("HH:mm"));

									// Chuyển đổi thời gian sang định dạng cron
									int seconds = localTime.getSecond();
									int minutes = localTime.getMinute();
									int hours = localTime.getHour();

									// Định dạng lại thành chuỗi "0 30 17 * * *"
									String cronFormat = String.format("%d %d %d * * *", seconds, minutes, hours);
									tele.setTime(cronFormat);
								}
								telegramRepository.save(tele);
								break;
							}
						}
					}
					// Trả về id của cập nhật cuối cùng đã xử lý hoặc xác nhận tất cả cập nhật
					return UpdatesListener.CONFIRMED_UPDATES_ALL;
					// Xử lý ngoại lệ
				}, e -> {
					if (e.response() != null) {
						// got bad response from telegram
						e.response().errorCode();
						e.response().description();
					} else {
						// probably network error
						e.printStackTrace();
					}
				});
			}

		}
		List<Telegram> telegramList = telegramRepository.findAll();
		if (telegramList.size() > 0) {
			for (Telegram telegram3 : telegramList) {
				if (telegram3.getMission().equals(Telegram.MissionType.THONGKE)) {

					String cronExpression = telegram3.getTime();

					// Phân tích cú pháp cron expression
					String[] parts = cronExpression.split(" ");

					// Lấy giá trị giây, phút và giờ
					int second = Integer.parseInt(parts[0]);
					int minute = Integer.parseInt(parts[1]);
					int hour = Integer.parseInt(parts[2]);

					// Tạo đối tượng LocalTime từ các giá trị trên
					LocalTime localTime = LocalTime.of(hour, minute, second);
					telegram3.setTime(String.valueOf(localTime));
				}
			}
			model.addAttribute("telegramList", telegramList);
		}
		return "admin/bot";
	}

	@RequestMapping("/user")
	public String userIndex() {
		return "redirect:/user/index";
	}

	@RequestMapping("/user/index")
	public String showUserIndex() {
		return "user/index";
	}

	@RequestMapping("/login")
	public String showLogin() {
		return "login";
	}

	@RequestMapping("/logout")
	public String showLogout() {
		return "login";
	}

	@RequestMapping("/product")
	public String showProduct() {
		return "user/testproduct";
	}

	@RequestMapping("/productdetail/{productid}")
	public String showProductDetail(@PathVariable Integer productid) {
		return "user/testproductdetail";
	}

	@RequestMapping("/product/room")
	public String showProductByRoom() {
		return "user/productbyroom";
	}

	@RequestMapping("/product/flashsale")
	public String showProductFlashsale() {
		return "user/productflashsale";
	}

	@RequestMapping("/product/search")
	public String showSearchProduct() {
		return "user/searchproduct";
	}

	// @RequestMapping("/productdetail")
	// public String showProductDetail() {
	// return "user/productDetail";
	// }
	@RequestMapping("/checkout")
	public String showpayment() {
		return "user/checkout1";
	}

	@RequestMapping("/order")
	public String showpayment2() {
		return "user/checkout2";
	}

	@RequestMapping("/orderdetail")
	public String showpayment3() {
		return "user/checkout3";
	}

	@RequestMapping("/myModalContent.html")
	public String modalShow() {
		return "myModalContent";
	}

	@RequestMapping("/myModalEvaluate.html")
	public String modal2Show() {
		return "myModalEvaluate";
	}

	@GetMapping("/chat")
	public String showChat() {
		return "user/chat";
	}

	@GetMapping("/stream")
	public String stream() {
		return "stream.html";
	}
}
