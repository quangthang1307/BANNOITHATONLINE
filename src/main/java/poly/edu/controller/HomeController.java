package poly.edu.controller;

import org.springframework.stereotype.Controller;
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

	@RequestMapping("/logout")
	public String showLogout() {
		return "login";
	}

	@RequestMapping("/productdetails")
	public String showProductdetail() {
		return "productDetail";
	}
}
