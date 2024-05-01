package poly.edu.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import poly.edu.Service.EvaluateService;
import poly.edu.Service.ImageService;
import poly.edu.Service.ProductService;
import poly.edu.entity.Customer;
import poly.edu.entity.DiscountUsage;
import poly.edu.entity.Evaluate;
import poly.edu.entity.Order;
import poly.edu.entity.Orderdetails;
import poly.edu.entity.Product;
import poly.edu.repository.DiscountUsageRepository;
import poly.edu.repository.EvaluateRepository;
import poly.edu.repository.OrderDetailRepository;
import poly.edu.repository.OrderRepository;

@Controller
@RequestMapping("/productdetail")
public class EvaluateuserController {

    @Autowired
    EvaluateService evaluateService;
    @Autowired
    EvaluateRepository evaluateRepository;
    @Autowired
    ProductService productService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ImageService imageService;
    @Autowired
    OrderDetailRepository orderDetailRepository;

    int orderid;

    @GetMapping("/{productId}")
    public String showList(@PathVariable Integer productId, Model model) {
        model.addAttribute("evaluate", new Evaluate());
        model.addAttribute("allEvaluate", evaluateRepository.findByProductId1(productId));
        List<Evaluate> list = evaluateRepository.findByProductId1(productId);

        return "user/testproductdetail";
    }

    // @GetMapping("/formEvalate")
    // public String showForm(Model model) {

    // return "user/EvaluateForm";
    // }

    @GetMapping("/formEvalate/{productId}/{customerid}/{orderID}")
    public String showList1(@PathVariable Integer productId, @PathVariable Integer customerid,
            @PathVariable Integer orderID, Model model) {
        orderid = orderID;
        Evaluate evaluate = new Evaluate(); // Tạo một đối tượng Evaluate mới
        evaluate.setProductId(productId);
        Customer c = new Customer();
        c.setCustomerId(customerid);
        evaluate.setCustomer(c);
        model.addAttribute("evaluate", evaluate); // Đặt đối tượng Evaluate vào Model
        Optional<Product> productForm = productService.findById(productId);
        model.addAttribute("evaluateProduct", productForm.orElse(null)); // Đặt đối tượng Product vào Model
        return "user/EvaluateForm";
    }

    @PostMapping("/saveevaluate")
    public String submitReview(
            @RequestPart("allimage") List<MultipartFile> files,
            @ModelAttribute("evaluate") Evaluate evaluate, Model model,
            RedirectAttributes redirectAttributes) {

        System.out.println(orderid);
        // Danh sách từ cấm
        List<String> bannedWords = Arrays.asList("chết", "giết", "ngu", "fuck", "má", "mẹ", "địt", "đụ", "hiếp", "dâm");

        // Kiểm tra mỗi từ trong bình luận
        for (String word : bannedWords) {
            if (evaluate.getComment().toLowerCase().contains(word)) {
                // Nếu bình luận chứa từ cấm, hiển thị thông báo lỗi cho người dùng và quay trở
                // lại trang đánh giá sản phẩm
                model.addAttribute("error",
                        "Xin lỗi, bình luận của bạn không thể được đăng vì chứa từ hoặc cụm từ không phù hợp.");
                // Đặt lại dữ liệu đánh giá vào model để hiển thị lại trên trang đánh giá sản
                // phẩm
                model.addAttribute("evaluate", evaluate);
                // Đặt lại dữ liệu sản phẩm vào model để hiển thị lại trên trang đánh giá sản
                // phẩm
                Optional<Product> productForm = productService.findById(evaluate.getProductId());
                model.addAttribute("evaluateProduct", productForm.orElse(null));
                // Trả về trang đánh giá sản phẩm với thông báo lỗi
                return "user/EvaluateForm";
            }
        }

        // Lưu hình ảnh

        if (files.size() > 0) {
            for (MultipartFile file : files) {
                try {
                    imageService.saveImageEvaluate(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Xử lý ngoại lệ nếu cần
                }
            }
        }

        // Lưu đối tượng Evaluate
        evaluate.setEvaluateDate(LocalDateTime.now());
        evaluate.setEvaluatestatus(false);
        evaluateRepository.save(evaluate);
        Orderdetails orderdetails = orderDetailRepository.getOrderdetailsByOrderIDandProductid(orderid,
                evaluate.getProductId());
        System.out.println(orderdetails.getOrderdetailsID());
        orderdetails.setEvaluate(evaluate);
        orderDetailRepository.save(orderdetails);
        redirectAttributes.addFlashAttribute("success", "Đã gửi đánh giá thành công!");

        // Chuyển hướng đến trang productdetail với productId tương ứng
        return "redirect:/order";
    }

    // @PostMapping("/saveevaluate")
    // public String submitReview(
    // @RequestPart("allimage") List<MultipartFile> files,
    // @ModelAttribute("evaluate") Evaluate evaluate, Model model,
    // RedirectAttributes redirectAttributes) {

    // for (MultipartFile file : files) {
    // try {
    // imageService.saveImageEvaluate(file);
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }

    // evaluate.setEvaluateDate(LocalDateTime.now());
    // // Lưu đối tượng Evaluate
    // evaluateRepository.save(evaluate);
    // redirectAttributes.addFlashAttribute("success", "Đã gửi đánh giá thành
    // công!");

    // // Chuyển hướng đến trang hiển thị đánh giá với productId tương ứng
    // return "redirect:/order";
    // }
    // @PostMapping("/saveevaluate")
    // public String submitReview(@RequestBody Order order,
    // @ModelAttribute("evaluate") Evaluate evaluate, Model model,
    // RedirectAttributes redirectAttributes) {
    // // Lấy danh sách đơn hàng của khách hàng
    // List<Order> orderList =
    // orderRepository.findByCustomerid(order.getCustomer().getCustomerId());

    // // Thiết lập thông tin đánh giá
    // evaluate.setEvaluateDate(LocalDateTime.now());
    // // Thiết lập mã sản phẩm
    // evaluate.setCustomer(order.getCustomer()); // Thiết lập thông tin khách hàng

    // // Lưu đối tượng Evaluate
    // evaluateRepository.save(evaluate);

    // redirectAttributes.addFlashAttribute("success", "Đã gửi đánh giá thành
    // công!");

    // // Chuyển hướng đến trang hiển thị đánh giá với productId tương ứng
    // return "redirect:/order";
    // }

}
