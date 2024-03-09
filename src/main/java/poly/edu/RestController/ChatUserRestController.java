package poly.edu.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatUserRestController {
    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/user/message")
    @SendTo("/topic/messages")
    public String postMessage(@RequestBody String message) {
        // Gửi tin nhắn từ User tới tất cả các người dùng khác
        template.convertAndSend("/topic/messages", message);
        return "Message sent";
    }
}
