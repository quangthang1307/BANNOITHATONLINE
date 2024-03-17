// package poly.edu.Service;

// import org.springframework.stereotype.Service;
// import org.telegram.telegrambots.bots.TelegramLongPollingBot;
// import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
// import org.telegram.telegrambots.meta.api.objects.Update;


// public class TelegramService extends TelegramLongPollingBot{

//     @Override
//     public void onUpdateReceived(Update update) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//             SendMessage mess = new SendMessage();
//             mess.setChatId(update.getMessage().getChatId().toString());
//             mess.setText(update.getMessage().getText());
//             System.out.println(update.getMessage().getChatId().toString());

//             try {
//                 execute(mess);
//             } catch (Exception e) {

//             }
//         }
//     }

//     public void sendMess(String text) {
//         SendMessage mess = new SendMessage();
//         mess.setChatId("5884779776");
//         mess.setText(text);
//         try {
//             execute(mess);
//         } catch (Exception e) {

//         }
//     }

//     @Override
//     public String getBotToken() {
//         return "6853009990:AAHzyQYieOgmEUBr6cAI8-OXGQD_t_GHVW4";
//     }

//     @Override
//     public String getBotUsername() {
//         return "springdatnbot";
//     }
    
// }
