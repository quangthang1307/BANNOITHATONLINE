package poly.edu.ServiceImpl;

import java.io.UnsupportedEncodingException;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

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

    private final Queue<EmailRequest> emailQueue = new ConcurrentLinkedQueue<>(); // Hàng đợi các yêu cầu gửi email

    public ForgotPasswordServiceImpl() {
        // Khởi tạo và chạy một luồng xử lý hàng đợi
        Thread queueHandlerThread = new Thread(() -> {
            while (true) {
                EmailRequest request = emailQueue.poll(); // Lấy yêu cầu đầu tiên từ hàng đợi
                if (request != null) {
                    try {
                        sendEmail2(request.getTo(), request.getSubject(), request.getContent()); // Gửi email
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Xử lý lỗi nếu cần thiết
                    }
                }
            }
        });
        queueHandlerThread.start(); // Bắt đầu chạy luồng xử lý hàng đợi
    }

    @Override
    public void sendEmail(HttpServletRequest request, String to, String subject, String emailLink) throws MessagingException, UnsupportedEncodingException {
        // TODO Auto-generated method stub
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String resetToken = UUID.randomUUID().toString();
        String email = request.getParameter("email");
        session.setAttribute("resetToken", resetToken);
        session.setAttribute("resetEmail", email);

        String emailTokenSession = emailLink;
        
        String emailContent = "<p>Xin chào!!!<p>"
                                + "Nhấn vào đường dẫn để thay đổi mật khẩu:"
                                + "<p><a href=\"" + emailTokenSession + "\"> Thay đổi mật khẩu của tôi</a></p>"
                                + "<p>Mã xác nhận của bạn: " + resetToken + "</p>"
                                + "<br>"
                                + "Bỏ qua email này nếu bạn không thực hiện yêu cầu!!!";
        helper.setText(emailContent, true);        
        helper.setFrom("thangnqpc03483@fpt.edu.vn", "NoiThatSuper"); 
        helper.setSubject("Thông tin khôi phục tải khoản");    
        helper.setTo(to);
        javaMailSender.send(message);           
    }

    @Override
    public void sendEmailHuyDon(String to, String subject, String content) {
        EmailRequest request = new EmailRequest(to, subject, content);
        emailQueue.offer(request); // Thêm yêu cầu vào hàng đợi
    }

   
    public void sendEmail2(String to, String subject, String content) throws MessagingException, UnsupportedEncodingException {        
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);       
        helper.setSubject(subject);
        helper.setText(content, true);        
        helper.setFrom("thangnqpc03483@fpt.edu.vn", "NoiThatSuper");     
        helper.setTo(to);
        javaMailSender.send(message);           
    }


    private static class EmailRequest {
        private String to;
        private String subject;
        private String content;

        public EmailRequest(String to, String subject, String content) {
            this.to = to;
            this.subject = subject;
            this.content = content;
        }

        public String getTo() {
            return to;
        }

        public String getSubject() {
            return subject;
        }

        public String getContent() {
            return content;
        }
    }
    
}
