package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ChatEmployeeController {
    @Autowired
    private SimpMessagingTemplate template;

    @GetMapping("/employee/chatRealtime")
    public String getChatPage(Model model) {
        // Trả về trang chat realtime cho Employee
        return "employeeChatRealtime";
    }

    @MessageMapping("/employee/message")
    @SendTo("/topic/messages")
    public String postMessage(@RequestBody String message) {
        // Gửi tin nhắn từ Employee tới tất cả các người dùng khác
        template.convertAndSend("/topic/messages", message);
        return "redirect:/employee/chatRealtime";
    }
}
