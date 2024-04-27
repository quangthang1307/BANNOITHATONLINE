package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.edu.repository.CustomerRepository;

@Controller
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @RequestMapping("/admin/customer")
    public String showAdminOrder(Model model) {
        model.addAttribute("customers", customerRepository.findCustomerDESC());
        return "admin/customer";
    }
}
