package poly.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccountController {

    @RequestMapping("/user/register")
    public String showRegister() {
        return "user/register";
    }

    @RequestMapping("/user/forgotpassword")
    public String showForgotpassword() {
        return "user/forgotpassword";
    }
}
