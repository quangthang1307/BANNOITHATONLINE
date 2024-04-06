package poly.edu.controller;

import java.util.List;
import java.util.Locale.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
import poly.edu.Service.BrandService;
import poly.edu.entity.Brands;

@Controller
@RequestMapping("/admin")
public class BrandsConTroller {

  @Autowired
  BrandService brandService;

  @GetMapping("/brand")
  public String showList(@ModelAttribute("brand") Brands brands, Model model) {
    model.addAttribute("allbrands", brandService.findAll()); // Thêm danh sách thương hiệu vào model
    // model.addAttribute("newBrand", new Brands()); // Thêm một đối tượng Brands
    // mới cho form
    return "admin/brand";
  }

  @GetMapping("/formbrand")
  public String Formbrand(@ModelAttribute("brand") Brands brands, Model model) {
    model.addAttribute("allbrands", brandService.findAll()); // Thêm danh sách thương hiệu vào model
    // model.addAttribute("newBrand", new Brands()); // Thêm một đối tượng Brands
    // mới cho form
    return "admin/brands";
  }

  @PostMapping("/saveBrand")
  public String saveAccount(
      @Validated @ModelAttribute("brand") Brands brands,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes,
      Model model) {
    // Kiểm tra xem tên thương hiệu đã tồn tại trong danh sách hay chưa
    if (brandService.existsByNameIgnoreCase(brands.getBrandname())) {
      bindingResult.rejectValue(
          "brandname",
          "error.brand",
          "Tên thương hiệu đã tồn tại.");
    }

    // Kiểm tra xem có lỗi khi binding dữ liệu không
    if (bindingResult.hasErrors()) {
      model.addAttribute("allbrands", brandService.findAll());
      return "/admin/brands"; // Trả về trang brand nếu có lỗi
    }

    // Kiểm tra độ dài tối đa của tên thương hiệu
    if (brands.getBrandname().length() > 50) {
      bindingResult.rejectValue(
          "brandname",
          "error.brand",
          "Tên thương hiệu không được vượt quá 50 ký tự.");
      model.addAttribute("allbrands", brandService.findAll());
      return "/admin/brands"; // Trả về trang brand nếu tên thương hiệu vượt quá 50 ký tự
    }

    // Lưu thương hiệu vào cơ sở dữ liệu
    brandService.create(brands);

    // Thêm thông báo thành công vào flash attribute để hiển thị

    redirectAttributes.addFlashAttribute(
        "successMessage",
        "Lưu thương hiệu thành công!");

    // Chuyển hướng đến trang danh sách brand
    return "redirect:/admin/formbrand";
  }

  @GetMapping("/deleteBrands/{brandsId}")
  public String deleteBrands(
      @PathVariable("brandsId") Integer brandsId,
      RedirectAttributes redirectAttributes) {
    try {
      Brands brands = brandService.findById(brandsId);
      brandService.delete(brandsId);
      // Đặt thông báo thành công vào redirectAttributes
      redirectAttributes.addFlashAttribute(
          "deletesuccessMessage",
          "Xóa thành công!");
    } catch (Exception e) {
      // Đặt thông báo lỗi vào redirectAttributes nếu có lỗi
      redirectAttributes.addFlashAttribute(
          "deleteerrorMessage",
          "Xóa thất bại: " + e.getMessage());
    }
    // Chuyển hướng người dùng đến trang hiển thị danh sách loại sản phẩm hoặc trang
    // chính của trang quản trị
    return "redirect:/admin/brand";
  }

  // edit product
  @GetMapping("editBrands/{brandsId}")
  public String EditBrands(
      @PathVariable("brandsId") Integer brandsId,
      Model model) {
    Brands brands = brandService.findById(brandsId);
    model.addAttribute("brand", brands);
    model.addAttribute("allbrands", brandService.findAll());

    return "admin/brands";
  }
  // @GetMapping("/editBrands/{brandsId}")
  // public String showUpdateForm(@PathVariable("brandsId") Integer brandsId,
  // Model model) {
  // // Lấy category cần sửa
  // Brands brands = brandService.findById(brandsId)
  // .orElseThrow(() -> new IllegalArgumentException("Invalid brands Id:" +
  // brandsId));
  // // Lấy danh sách categories để giữ nguyên dữ liệu dưới bảng
  // Page<Brands> pages = brandService.findAll(PageRequest.of(0, 5));
  // // Thêm category và danh sách categories vào model
  // model.addAttribute("brands", brands);
  // model.addAttribute("pages", pages);
  // model.addAttribute("currentPage", 0);

  // return "/admin/brand";
  // }

  // // chuc nang delete
  // @GetMapping("/deleteBrands/{brandsId}")
  // public String deleteBrands(@PathVariable("brandsId") Integer brandsId, Model
  // model) {
  // Brands brands = brandService.findById(brandsId)
  // .orElseThrow(() -> new IllegalArgumentException("Invalid brands Id:" +
  // brandsId));
  // brandService.delete(brandsId);
  // return "redirect:/admin/brand";
  // }
}
