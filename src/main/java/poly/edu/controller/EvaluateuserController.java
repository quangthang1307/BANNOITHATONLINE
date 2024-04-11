package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.edu.Service.EvaluateService;
import poly.edu.entity.Evaluate;

@Controller
@RequestMapping("/productdetail")
public class EvaluateuserController {

    @Autowired
    EvaluateService evaluateService;

    @GetMapping("/{productId}")
    public String showList(Model model, Evaluate evaluate) {
        model.addAttribute("evaluate", new Evaluate());
        model.addAttribute("allEvaluate", evaluateService.findAll());

        return "user/testproductdetail";
    }
}
