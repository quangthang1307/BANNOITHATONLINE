package poly.edu.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import poly.edu.Service.CategoryService;
import poly.edu.entity.Brands;
import poly.edu.entity.Categoryproduct;
import poly.edu.entity.Discount;
import poly.edu.entity.Sale;

@Controller
@RequestMapping("/admin")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    int idcategory;

    @GetMapping("/categoryproduct")
    public String showList(Model model) {
        List<Categoryproduct> list = categoryService.findAllCapcha();
        model.addAttribute("categorys", list);
        return "admin/categoryproduct";
    }

    @GetMapping("/categoryproduct/form")
    public String showFormCatefory(Model model) {
        List<Categoryproduct> list = categoryService.findAllCapcha();
        model.addAttribute("categoryscha", list);
        model.addAttribute("categorys", new Categoryproduct());
        return "admin/categoryform";
    }

    @PostMapping("/categoryproduct/save")
    public String saveFormCatefory(Model model, @RequestParam("optionCategory") String optionCategory,
            @Valid @ModelAttribute("categorys") Categoryproduct categoryproduct, BindingResult result) {
        System.out.println(optionCategory);
        System.out.println(categoryproduct.getProductname());
        if (result.hasErrors()) {
            System.out.println("Có lỗi");
            List<Categoryproduct> list = categoryService.findAllCapcha();
            model.addAttribute("categoryscha", list);
            return "admin/categoryform";
        }

        if (!optionCategory.equals("0")) {
            categoryproduct.setIDcapcha(Integer.parseInt(optionCategory));
        }
        categoryService.save(categoryproduct);
        return "redirect:/admin/categoryproduct";
    }

    @GetMapping("/categoryproduct/capcon/{categoryID}")
    public String showListCapcon(Model model, @PathVariable("categoryID") Integer categoryID) {
        idcategory = categoryID;
        List<Integer> id = new ArrayList<>();
        id.add(categoryID);
        List<Categoryproduct> list = categoryService.findCategoryByCapCha(id);
        model.addAttribute("categorys", list);
        return "admin/categoryproductcapcon";
    }

    @GetMapping("/deletecategory/{categoryID}")
    public String deleteProduct(@PathVariable("categoryID") Integer categoryID) {

        categoryService.delete(categoryID);
        return "redirect:/admin/categoryproduct/capcon/" + idcategory;
    }
}
