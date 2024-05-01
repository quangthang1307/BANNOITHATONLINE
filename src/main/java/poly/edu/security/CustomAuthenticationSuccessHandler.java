package poly.edu.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import poly.edu.Service.EmployeeService;
import poly.edu.entity.Employee;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private EmployeeService employeeService;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ADMIN"));
        boolean isEmployee = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("EMPLOYEE"));
        boolean isUser = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("USER"));

        HttpSession session = request.getSession();
        session.setAttribute("username", authentication.getName());

        if (isAdmin) {
            redirectStrategy.sendRedirect(request, response, "/admin/statusproduct");
        } else if (isEmployee) {
            redirectStrategy.sendRedirect(request, response, "/employee/statusproduct");
        } else if (isUser) {
            redirectStrategy.sendRedirect(request, response, "/user");
        } else {
            redirectStrategy.sendRedirect(request, response, "/index");
        }
    }

}