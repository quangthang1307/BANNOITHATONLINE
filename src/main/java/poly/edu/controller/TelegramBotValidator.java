package poly.edu.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetMe;
import com.pengrad.telegrambot.response.GetMeResponse;

public class TelegramBotValidator {
    public static void main(String[] args) {
        String botToken = "7041078559:AAFBCCotM3Oipx2qNDeUelRT0RnvzmQO4sk";
        
        TelegramBot bot = new TelegramBot(botToken);
        GetMe getMe = new GetMe();
        
        try {
            GetMeResponse response = bot.execute(getMe);
            if (response.isOk()) {
                System.out.println("Bot information:");
                System.out.println("Username: " + response.user().username());
                System.out.println("First name: " + response.user().firstName());
               System.out.println( response.user().id());
                // Hiển thị các thông tin khác nếu cần
            } else {
                System.out.println("Bot token is not valid!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
