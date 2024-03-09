package poly.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatUserController {
    @GetMapping("/user/chatRealtime")
    public String showChat(Model model) {
        return "userChatRealtime";
    }
}
