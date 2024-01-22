package poly.edu.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {
	
	@RequestMapping("/index")
	public String index() {
		return "user/index";
	}

	@RequestMapping("/login")
	public String showLogin() {
		return "login";
	}

	@RequestMapping("/productdetails")
	public String showProductdetail() {
		return "productDetail";
	}

	@RequestMapping("/checkout")
	public String showpayment() {
		return "user/checkout1";
	}

	@RequestMapping("/order")
	public String showpayment2() {
		return "user/checkout2";
	}
	@RequestMapping("/orderdetail")
	public String showpayment3() {
		return "user/checkout3";
	}


	
}
