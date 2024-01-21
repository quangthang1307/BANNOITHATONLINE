package poly.edu.security;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Tên đăng nhập hoặc mật khẩu không chính xác";

        if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
            errorMessage = "Tài khoản của bạn đã bị vô hiệu hóa";
        } else if (exception.getMessage().equalsIgnoreCase("User account has expired")) {
            errorMessage = "Tài khoản của bạn đã hết hạn";
        }

        response.sendRedirect("/login?error=" + URLEncoder.encode(errorMessage, "UTF-8"));
    }
}

