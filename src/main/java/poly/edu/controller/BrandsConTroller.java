package poly.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class BrandsConTroller {
	@RequestMapping("/admin/brands")
	public String brands() {
		return "admin/brands";
	}
	

}
