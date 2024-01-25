package poly.edu.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import poly.edu.Service.AccountService;
import poly.edu.Service.ForgotPasswordService;
import poly.edu.entity.Account;

@Controller
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    ForgotPasswordService forgotPasswordService;

    //Edit profile
    @RequestMapping("/user/editprofile")
    public String showEditProfile() {
        return "user/editprofile";
    }

    @RequestMapping("/user/profile")
	public String showUserProfile(Model model) {
	    return "user/profiles";
	}
	
	@RequestMapping("/user/profile/pageaddress")
	public String showAddressProfile(Model model) {
	    return "user/profilesaddress";
	}
	//

    @RequestMapping("/user/register")
    public String showRegister() {
        return "user/register";
    }

    @RequestMapping("/user/forgotpassword")
    public String showForgotpassword() {
        return "user/forgotpassword";
    }

    @PostMapping("/user/saveforgotpassword")
    public String saveForgotpassword(@RequestParam("email") String email, Model model) {
        Account account = accountService.findByEmail(email);
        if (account == null) {
            model.addAttribute("error", "Email không đúng với email đăng ký");
        }

        String emailLink = "http://localhost:8080/user/resetpassword";

        try {
            forgotPasswordService.sendEmail(account.getEmail(), "Đường dẫn thay đổi mật khẩu", emailLink);
        } catch (UnsupportedEncodingException | MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "user/forgotpassword";
    }

    @GetMapping("/user/resetpassword")
    public String showResetPassword() {
        return "user/resetpassword";
    }

    @PostMapping("/user/resetpassword")
    public String saveResetPassword(HttpServletRequest request, Model model) {

        String password = request.getParameter("password");
        String username = request.getParameter("username");
        String confirmPassword = request.getParameter("confirmpassword");

        if(password.equals(confirmPassword)){
            model.addAttribute("message", "Mật khẩu trùng khớp");
        }else{
            model.addAttribute("message", "Mật khẩu xác nhận không trùng khớp");
        }

        HttpSession session = request.getSession();
        String resetToken = (String) session.getAttribute("resetToken");

        if(resetToken != null && !resetToken.isEmpty()){
            Account checkEmail = accountService.findByUserName(username);
            if(checkEmail != null){
                checkEmail.setPassword(password);
                accountService.saveAccount(checkEmail);

                session.removeAttribute("resetToken");

                return "user/resetpassword";
            }
            
        }

        return "user/resetpassword";
    }
}
