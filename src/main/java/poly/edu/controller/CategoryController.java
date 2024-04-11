package poly.edu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.edu.Service.CategoryService;
import poly.edu.entity.Brands;
import poly.edu.entity.Categoryproduct;

@Controller
@RequestMapping("/admin")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/categoryproduct")
    public String showList(Model model) {
        List<Categoryproduct> list = categoryService.findAllCapcha();
        model.addAttribute("categorys", list);
        return "admin/categoryproduct";
    }
}
