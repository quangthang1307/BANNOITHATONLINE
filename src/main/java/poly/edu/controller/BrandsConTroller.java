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
import poly.edu.entity.Product;

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

	@PostMapping("/saveBrands")
	public String saveAccount(@Validated @ModelAttribute("brand") Brands brands,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("allbrands", brandService.findAll());

			return "/admin/brand";
		}

		brandService.create(brands);

		redirectAttributes.addFlashAttribute("successMessage", "Lưu sản phẩm thành công!");

		return "redirect:/admin/brand"; // Chuyển hướng đến trang danh sách accounts
	}

	@GetMapping("/deleteBrands/{brandsId}")
	public String deleteBrands(@PathVariable("brandsId") Integer brandsId, RedirectAttributes redirectAttributes) {
		try {
			Brands brands = brandService.findById(brandsId);
			brandService.delete(brandsId);
			;
			// Đặt thông báo thành công vào redirectAttributes
			redirectAttributes.addFlashAttribute("deletesuccessMessage", "Xóa thành công!");
		} catch (Exception e) {
			// Đặt thông báo lỗi vào redirectAttributes nếu có lỗi
			redirectAttributes.addFlashAttribute("deleteerrorMessage", "Xóa thất bại: " + e.getMessage());
		}
		// Chuyển hướng người dùng đến trang hiển thị danh sách loại sản phẩm hoặc trang
		// chính của trang quản trị
		return "redirect:/admin/brand";
	}

	// edit product
	@GetMapping("editBrands/{brandsId}")
	public String EditBrands(@PathVariable("brandsId") Integer brandsId, Model model) {
		Brands brands = brandService.findById(brandsId);
		model.addAttribute("brand", brands);
		model.addAttribute("allbrands", brandService.findAll());

		return "admin/brand";
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
