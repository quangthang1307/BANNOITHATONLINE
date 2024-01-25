package poly.edu.ServiceImpl;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import poly.edu.Service.ForgotPasswordService;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService{

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    HttpSession session;

    @Override
    public void sendEmail(HttpServletRequest request, String to, String subject, String emailLink) throws MessagingException, UnsupportedEncodingException {
        // TODO Auto-generated method stub
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String resetToken = UUID.randomUUID().toString();
        String email = request.getParameter("email");
        session.setAttribute("resetToken", resetToken);
        session.setAttribute("resetEmail", email);

        String emailTokenSession = emailLink + "?token=" + resetToken;
        
        String emailContent = "<p>Xin chào!!!<p>"
                                + "Nhấn vào đường dẫn để thay đổi mật khẩu:"
                                + "<p><a href=\"" + emailTokenSession + "\"> Thay đổi mật khẩu của tôi</a></p>"
                                + "<p>Mã xác nhận của bạn: " + resetToken + "</p>"
                                + "<br>"
                                + "Bỏ qua email này nếu bạn không thực hiện yêu cầu!!!";
        helper.setText(emailContent, true);        
        helper.setFrom("thangnqpc03483@fpt.edu.vn", "NoiThatSuper");     
        helper.setTo(to);
        javaMailSender.send(message);           
    }
    
}
