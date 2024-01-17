package poly.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

	@RequestMapping("/product")
	public String showProduct() {
		return "user/products";
	}

	@RequestMapping("/productdetail/{productid}")
	public String showProductDetail(@PathVariable Integer productid) {
		return "user/productDetail";
	}

	// 	@RequestMapping("/productdetail")
	// public String showProductDetail() {
	// 	return "user/productDetail";
	// }
}
