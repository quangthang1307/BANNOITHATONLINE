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
import poly.edu.Service.CategoryService;
import poly.edu.Service.ProductService;
import poly.edu.entity.Brands;
import poly.edu.entity.Product;

@Controller
@RequestMapping("/admin")
public class ProductController {
	@Autowired
    ProductService prooductService;

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/products")
	public String showList(@ModelAttribute("brand") Brands brands,Model model) {
      
        model.addAttribute("products", prooductService.findAllNoActive());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("categorys", categoryService.findAllCapCon());
		return "admin/products";
	}
    
}