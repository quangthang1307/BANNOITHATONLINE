package poly.edu.Service;

import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

public interface ForgotPasswordService {
     
     void sendEmailHuyDon(String to, String subject, String content) throws MessagingException, UnsupportedEncodingException;
     void sendEmail(HttpServletRequest request, String to, String subject, String emailLink) throws MessagingException, UnsupportedEncodingException;
} 
