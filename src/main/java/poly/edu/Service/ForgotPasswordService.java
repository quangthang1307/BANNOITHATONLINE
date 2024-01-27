package poly.edu.Service;

import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

public interface ForgotPasswordService {
     void sendEmail(HttpServletRequest request, String to, String subject, String emailLink) throws MessagingException, UnsupportedEncodingException;
} 
