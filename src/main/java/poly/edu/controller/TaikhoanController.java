package poly.edu.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@RequestMapping("/admin")
public class TaikhoanController {
    @Autowired
    AccountService accountService;

    @GetMapping("/taikhoan")
    public String showList(@ModelAttribute("taikhoan") Account account, Model model) {

        model.addAttribute("allAccount", accountService.findAll()); // Thêm danh sách thương hiệu vào model
        // model.addAttribute("newBrand", new Brands()); // Thêm một đối tượng Brands
        // mới cho form
        return "admin/taikhoan";
    }

    @PostMapping("/saveAccount")
    public String saveAccount(@Validated @ModelAttribute("taikhoan") Account account,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allAccount", accountService.findAll());

            return "/admin/taikhoan";
        }

        accountService.create(account);

        redirectAttributes.addFlashAttribute("successMessage", "Lưu sản phẩm thành công!");

        return "redirect:/admin/taikhoan"; // Chuyển hướng đến trang danh sách accounts
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