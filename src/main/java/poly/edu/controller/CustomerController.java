package poly.edu.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.edu.entity.Address;
import poly.edu.entity.Customer;
import poly.edu.entity.Order;
import poly.edu.entity.Orderdetails;
import poly.edu.entity.Product;
import poly.edu.repository.AddressRepository;
import poly.edu.repository.CustomerRepository;

@Controller
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AddressRepository addressRepository;

    @RequestMapping("/admin/customer")
    public String showAdminOrder(Model model) {
        model.addAttribute("customers", customerRepository.findCustomerDESC());
        return "admin/customer";
    }

    @RequestMapping("/admin/address/{customerId}")
    public String findByCustomerAddress(Model model,
    @PathVariable("customerId") Integer customerId) {
        List<Address> addresses = addressRepository.findARCustomerId(customerId);
        model.addAttribute("addresses", addresses);
        return "admin/customeraddress";
    }
}
