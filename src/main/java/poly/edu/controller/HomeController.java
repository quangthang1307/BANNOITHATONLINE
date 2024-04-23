package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.edu.Service.ImageService;

@Controller
public class HomeController {
	@Autowired
	ImageService imageService;

	@RequestMapping("")
	public String showindex() {
		return "user/index";
	}

	@RequestMapping("/index")
	public String index() {
		return "user/index";
	}

	@RequestMapping("/admin")
	public String Adminindex() {
		return "redirect:/admin/index";
	}

	@RequestMapping("/admin/index")
	public String showAdminindex() {
		return "admin/index";
	}

	@RequestMapping("/user")
	public String userIndex() {
		return "redirect:/user/index";
	}

	@RequestMapping("/user/index")
	public String showUserIndex() {
		return "user/index";
	}

	@RequestMapping("/login")
	public String showLogin() {
		return "login";
	}

	@RequestMapping("/logout")
	public String showLogout() {
		return "login";
	}

	@RequestMapping("/product")
	public String showProduct() {
		return "user/testproduct";
	}

	@RequestMapping("/productdetail/{productid}")
	public String showProductDetail(@PathVariable Integer productid) {
		return "user/testproductdetail";
	}

	@RequestMapping("/product/room")
	public String showProductByRoom() {
		return "user/productbyroom";
	}

	@RequestMapping("/product/flashsale")
	public String showProductFlashsale() {
		return "user/productflashsale";
	}

	@RequestMapping("/product/search")
	public String showSearchProduct() {
		return "user/searchproduct";
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

	@RequestMapping("/myModalEvaluate.html")
	public String modal2Show() {
		return "myModalEvaluate";
	}

	@GetMapping("/chat")
	public String showChat() {
		return "user/chat";
	}

	@GetMapping("/stream")
	public String stream() {
		return "stream.html";
	}
}
