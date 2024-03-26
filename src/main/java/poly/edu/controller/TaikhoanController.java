package poly.edu.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import poly.edu.Service.AccountService;
import poly.edu.entity.Account;

import poly.edu.entity.Brands;
import poly.edu.repository.CustomerRepository;
import poly.edu.repository.EmployeeRepository;

@Controller
@RequestMapping("/admin")
public class TaikhoanController {
    @Autowired
    AccountService accountService;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/taikhoan")
    public String showList(@ModelAttribute("taikhoan") Account account, Model model) {

        model.addAttribute("allAccount", accountService.findAll()); // Thêm danh sách thương hiệu vào model
        // model.addAttribute("newBrand", new Brands()); // Thêm một đối tượng Brands
        // mới cho form
        return "admin/taikhoan";
    }

    @GetMapping("/phanquyentaikhoan")
    public String showAccountpermissions(Model model){

        return "admin/phanQuyenTaiKhoan";
    }
    
//    @PostMapping("/updateOrderStatus")
//    public ResponseEntity<String> updateOrderStatus(@RequestParam Integer accountId, @RequestParam AccountStatus newStatus) {
//        return accountService.updateOrderStatus(accountId, newStatus);
//    }
//
//    @PostMapping("/saveAccount")
//    public String saveAccount(@Validated @RequestParam("accountId") Account account,
//            BindingResult bindingResult,
//            RedirectAttributes redirectAttributes, Model model) {
//    	try {
//    	    if (bindingResult.hasErrors()) {
//                model.addAttribute("allAccount", accountService.findAll());
//
//                return "/admin/taikhoan";
//            }
//
//            accountService.create(account);
//
//            redirectAttributes.addFlashAttribute("successMessage", "Lưu sản phẩm thành công!");
//
//           
//        } catch (Exception e) {
//            // Đặt thông báo lỗi vào redirectAttributes nếu có lỗi
//            redirectAttributes.addFlashAttribute("saveerrorMessage", "Xóa thất bại: " + e.getMessage());
//        }
//    	return "redirect:/admin/taikhoan"; 
//    }
//    @PostMapping("/saveAccount")
//    public String saveAccount(@RequestParam("accountId") Integer accountId,
//                              @RequestParam("active") boolean active,
//                              RedirectAttributes redirectAttributes) {
//        try {
//            // Tìm tài khoản dựa trên accountId
//            Account account = accountService.findById(accountId);
//            
//            if (account == null) {
//                // Trả về trang lỗi nếu không tìm thấy tài khoản
//                return "redirect:/error";
//            }
//            
//            // Cập nhật trạng thái của tài khoản
//            account.setActive(active);
//            
//            // Lưu tài khoản
//            accountService.saveAccount(account);
//
//            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái tài khoản thành công!");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("saveerrorMessage", "Cập nhật trạng thái tài khoản thất bại: " + e.getMessage());
//        }
//        return "redirect:/admin/taikhoan"; 
//    }
    @PostMapping("/saveAccount/{accountId}")
    public String saveAccount(@PathVariable("accountId") Integer accountId,
                              @RequestParam("active") boolean active,
                              RedirectAttributes redirectAttributes) {
        try {
            // Tìm tài khoản dựa trên accountId
            Account account = accountService.findById(accountId);
            
            if (account == null) {
                // Trả về trang lỗi nếu không tìm thấy tài khoản
                return "redirect:/error";
            }
            
            // Cập nhật trạng thái của tài khoản
            account.setActive(active);
            
            // Lưu tài khoản
            accountService.saveAccount(account);

            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái tài khoản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("saveerrorMessage", "Cập nhật trạng thái tài khoản thất bại: " + e.getMessage());
        }
        return "redirect:/admin/taikhoan"; 
    }


    
    @GetMapping("/deleteTaiKhoan/{accountId}")
    public String deleteTaiKhoan(@PathVariable("accountId") Integer accountId, RedirectAttributes redirectAttributes) {
        try {
            Account account = accountService.findById(accountId);
            accountService.delete(accountId);
            // Đặt thông báo thành công vào redirectAttributes
            redirectAttributes.addFlashAttribute("deletesuccessMessage", "Xóa thành công!");
        } catch (Exception e) {
            // Đặt thông báo lỗi vào redirectAttributes nếu có lỗi
            redirectAttributes.addFlashAttribute("deleteerrorMessage", "Xóa thất bại: " + e.getMessage());
        }
        // Chuyển hướng người dùng đến trang hiển thị danh sách tài khoản
        return "redirect:/admin/taikhoan";
    }


    // edit product
    @GetMapping("editAccount/{accountId}")
    public String EditAccount(@PathVariable("accountId") Integer accountId, Model model) {
        Account account = accountService.findById(accountId);
        model.addAttribute("taikhoan", account);
        model.addAttribute("allbrands", accountService.findAll());

        return "admin/taikhoan1";
    }

    // @RequestMapping("/taikhoan/saveAccount1")
    // public String saveStatusOrder(@RequestParam("accountId") Integer accountId,
    // @RequestParam("taikhoan") Integer taikhoan,
    // RedirectAttributes redirectAttributes) {
    // // Tìm tài khoản theo ID
    // Account account = accountService.findById(accountId);

    // // Nếu tài khoản tồn tại
    // if (account != null) {
    // // Cập nhật trường taikhoan trong tài khoản
    // account.setActive(null);

    // // Lưu lại thông tin tài khoản
    // accountService.saveAccount(account);

    // // Chuyển hướng về trang quản lý tài khoản
    // redirectAttributes.addFlashAttribute("successMessage", "Lưu tài khoản thành
    // công!");
    // return "redirect:/admin/taikhoan";
    // } else {
    // // Trường hợp tài khoản không tồn tại
    // redirectAttributes.addFlashAttribute("errorMessage", "Tài khoản không tồn
    // tại!");
    // return "redirect:/admin/taikhoan";
    // }
    // }

    // public Map<Integer, String> listAccount() {
    // Map<Integer, String> account = new HashMap<>();
    // account.put(1, "True");
    // account.put(2, "False");
    // return account;
    // }
}
