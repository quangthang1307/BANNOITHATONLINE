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
import poly.edu.Service.CustomerService;
import poly.edu.entity.Customer;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerService customerService;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final CustomAccountDetails customAccountDetails = (CustomAccountDetails) authentication.getPrincipal();
        Customer customer = customAccountDetails.getCustomer();
        final String jwt = jwtUtil.generateToken(userDetails);

        Cookie cookie = new Cookie("jwtToken", jwt);
        cookie.setMaxAge(60*60); // 7 days
        response.addCookie(cookie);

        response.addHeader("Authorization", "Bearer " + jwt);

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ADMIN"));
        boolean isEmployee = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("EMPLOYEE"));
        boolean isUser = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("USER"));

        HttpSession session = request.getSession();
        session.setAttribute("username", customAccountDetails.getUsername());
        session.setAttribute("customerName", customer.getName());
        session.setAttribute("isAdmin", isAdmin);

        if (isAdmin) {
            redirectStrategy.sendRedirect(request, response, "/admin");
        } else if (isEmployee) {
            redirectStrategy.sendRedirect(request, response, "/employee");
        } else if (isUser) {
            redirectStrategy.sendRedirect(request, response, "/user");
        } else {
            redirectStrategy.sendRedirect(request, response, "/index");
        }
    }

}
