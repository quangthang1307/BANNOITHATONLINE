package poly.edu.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {

	@RequestMapping("/index")
	public String index() {
		return "user/index";
	}

	@RequestMapping("/admin/index")
	public String showAdminindex() {
		return "admin/index";
	}

	@RequestMapping("/admin")
	public String Adminindex() {
		return "redirect:/admin/index";
	}

	@RequestMapping("/login")
	public String showLogin() {
		return "login";
	}

	@RequestMapping("/product")
	public String showProduct() {
		return "user/products";
	}

	@RequestMapping("/logout")
	public String showLogout() {
		return "login";
	}

	@RequestMapping("/productdetail/{productid}")
	public String showProductDetail(@PathVariable Integer productid) {
		return "user/productDetail";
	}

	// @RequestMapping("/productdetail")
	// public String showProductDetail() {
	// return "user/productDetail";
	// }
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

	@RequestMapping("/myModalContent.html")
	public String modalShow() {
		return "myModalContent";
	}


	@GetMapping("/chat")
	public String showChat(){
		return "chat";
	}
}
