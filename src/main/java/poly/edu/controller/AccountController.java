package poly.edu.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String saveForgotpassword(@RequestParam("email") String email, Model model, HttpServletRequest request) {

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (email == null || email.isEmpty()) {
            model.addAttribute("error", "Hãy nhập email của bạn");
            return "user/forgotpassword";
        } else if (!matcher.matches()) {
            model.addAttribute("error", "Vui lòng nhập đúng định dạng email");
            return "user/forgotpassword";
        }else{
            model.addAttribute("error", "Email đã được gửi thành công!");
        }

        Account account = accountService.findByEmail(email);
        if (account == null) {
            model.addAttribute("error", "Không tìm thấy tài khoản nào khớp với email này");
            return "user/forgotpassword";
        }

        String emailLink = "http://localhost:8080/user/resetpassword";

        try {
            forgotPasswordService.sendEmail(request, account.getEmail(), "Đường dẫn thay đổi mật khẩu", emailLink);
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
    public String saveResetPassword(@RequestParam("password") String password,
            @RequestParam("confirmpassword") String confirmPassword,
            @RequestParam("resetTokenInput") String resetTokenInput,
            HttpServletRequest request, RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();
        String resetEmail = (String) session.getAttribute("resetEmail");
        String resetToken = (String) session.getAttribute("resetToken");

        List<String> validationMessages = new ArrayList<>();

        if (password == null || password.isEmpty()) {
            validationMessages.add("Hãy nhập mật khẩu của bạn");
        }
        if (!validationMessages.isEmpty()) {
            for (String message : validationMessages) {
                redirectAttributes.addFlashAttribute("message", message);
            }
            // redirectAttributes.addAttribute("token", resetToken);
            return "redirect:/user/resetpassword";
        }
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            validationMessages.add("Hãy nhập lại mật khẩu của bạn");
        }
        if (!validationMessages.isEmpty()) {
            for (String message : validationMessages) {
                redirectAttributes.addFlashAttribute("message", message);
            }
            // redirectAttributes.addAttribute("token", resetToken);
            return "redirect:/user/resetpassword";
        }
        if (password != null && confirmPassword != null && !password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("message", "Mật khẩu xác nhận không trùng khớp");
            // redirectAttributes.addAttribute("token", resetToken);
            return "redirect:/user/resetpassword";
        }

        if (resetTokenInput == null || resetTokenInput.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Hãy nhập mã xác thực");
            // redirectAttributes.addAttribute("token", resetToken);
            return "redirect:/user/resetpassword";
        }

        if (!resetTokenInput.equals(resetToken)) {
            redirectAttributes.addFlashAttribute("message", "Mã Token không hợp lệ");
            // redirectAttributes.addAttribute("token", resetToken);
            return "redirect:/user/resetpassword";
        }

        if (resetEmail != null && resetTokenInput != null && !resetEmail.isEmpty() && !resetTokenInput.isEmpty()) {
            Account checkEmail = accountService.findByEmail(resetEmail);
            if (checkEmail != null) {
                // Kiểm tra mật khẩu mới và cập nhật tùy thuộc vào đó
                checkEmail.setPassword(password);
                accountService.saveAccount(checkEmail);

                session.removeAttribute("resetToken");
                session.removeAttribute("resetEmail");

                redirectAttributes.addFlashAttribute("message", "Đổi mật khẩu thành công");
            }
        }

        return "redirect:/user/resetpassword";
    }

}
