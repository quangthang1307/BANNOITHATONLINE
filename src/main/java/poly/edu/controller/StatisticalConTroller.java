package poly.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class StatisticalConTroller {
	@RequestMapping("/admin/statistical")
	public String statistical() {
		return "admin/statistical";
	}
	@RequestMapping("/admin")
	public String admin() {
		return "admin/statistical";
	}
}
