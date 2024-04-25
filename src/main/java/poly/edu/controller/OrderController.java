package poly.edu.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import poly.edu.Service.CustomerService;
import poly.edu.entity.Order;
import poly.edu.entity.Orderdetails;
import poly.edu.Service.OrderService;
import poly.edu.Service.OrderStatusService;
import poly.edu.entity.Customer;
import poly.edu.entity.Inventory;
import poly.edu.entity.Orderstatus;
import poly.edu.entity.Product;
import poly.edu.repository.CustomerRepository;
import poly.edu.repository.InventoryRepository;
import poly.edu.repository.OrderRepository;
import poly.edu.repository.OrderStatusRepository;

@Controller
public class OrderController {

    private Customer customer;

    @Autowired
    CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStatusService orderStatusService;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @RequestMapping("/admin/invoice")
    public String showAdminOrder(Model model) {
        model.addAttribute("orders", orderRepository.findOrderAll());
        return "admin/admininvoice";
    }

    @RequestMapping("/admin/statusproduct")
    public String showAdminstatusproduct(Model model) {
        model.addAttribute("orders", orderRepository.findOrderAll());
        model.addAttribute("statuses", orderStatusRepository.findAll());
        return "admin/statusproduct";
    }

    @PostMapping("/admin/invoice/update")
    public String updateOrderStatus(@RequestParam String orderstatusname, @RequestParam Integer OrderID) {
        Order order = orderService.findById(OrderID);
        Orderstatus orderStatus = orderStatusRepository.findByStatusName(orderstatusname);
        order.setOrderstatus(orderStatus);
        orderService.save(order);
        return "redirect:/admin/statusproduct";
    }

    // @PostMapping("/admin/invoice/updatecheckbox")
    // public ResponseEntity<?> updateCheckboxOrderStatus(@RequestBody Map<String,
    // Object> params) {
    // String statusName = (String) params.get("statusName");
    // List<Integer> orderIds = (List<Integer>) params.get("orderIds");
    // OrderStatus status = orderStatusService.findByStatusName(statusName);
    // for (Integer orderId : orderIds) {
    // Order order = orderService.findById(orderId);
    // order.setOrderStatus(status);
    // orderService.save(order);
    // }
    // return ResponseEntity.ok().build();
    // }

    // @RequestMapping("/admin/statusproduct/findcustomer/{customerId}")
    // public String findByCustomerStatusProduct(Model model,
    // @PathVariable("customerId") Integer customerId,
    // @RequestParam("p") Optional<Integer> p) {
    // Pageable pageable = PageRequest.of(p.orElse(0), 5);
    // Page<Customer> pages = customerRepository.findByCustomerId(customerId,
    // pageable);
    // model.addAttribute("pages", pages);
    // model.addAttribute("customer", new Customer());
    // model.addAttribute("activateUser", true);
    // model.addAttribute("currentPage", p.orElse(0));
    // return "admin/customer";
    // }

    @RequestMapping("/admin/showinvoicedetails/{orderId}")
    public String adminShowInvoiceDetails(Model model, @PathVariable("orderId") Integer orderId) {
        Order order = orderService.findById(orderId); // 

        List<Orderdetails> orderDetails = order.getOrderDetails();
        List<Product> products = new ArrayList<>();
        for (Orderdetails orderDetail : orderDetails) {
            Product product = orderDetail.getProduct();
            products.add(product);
        }
        model.addAttribute("order", order);
        model.addAttribute("products", products);

        return "admin/admininvoicedetails";
    }

    @RequestMapping("/admin/deleteoder/{orderId}")
    public String deleteOrder(@PathVariable Integer orderId) {

        orderService.deleteOrder(orderId);
        return "redirect:/admin/statusproduct";
    }

    // @GetMapping("/user/order/showorder")
    // public String showUserOrder() {
    // return "user/quanlydonhang";
    // }

    // @GetMapping("/user/order/showorderdetail/{orderid}")
    // public String showUserOrderDetail(Model model, @PathVariable("orderid") int
    // orderid) {
    // return "user/chitietdonhang";
    // }
}
