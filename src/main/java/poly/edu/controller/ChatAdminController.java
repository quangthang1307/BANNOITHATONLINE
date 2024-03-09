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
public class ChatAdminController {
    @Autowired
    private SimpMessagingTemplate template;

    @GetMapping("/admin/chatRealtime")
    public String getChatPage(Model model) {
        // Trả về trang chat realtime cho Admin
        return "adminChatRealtime";
    }

    @MessageMapping("/admin/message")
    @SendTo("/topic/messages")
    public String postMessage(@RequestBody String message) {
        // Gửi tin nhắn từ Admin tới tất cả các người dùng khác
        template.convertAndSend("/topic/messages", message);
        return "redirect:/admin/chatRealtime";
    }
}
