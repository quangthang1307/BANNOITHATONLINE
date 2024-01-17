package poly.edu.RestController;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import poly.edu.Service.DiscountService;
import poly.edu.entity.Discount;


@CrossOrigin("*")
@RestController
public class DiscountRestController {
	
	@Autowired DiscountService discountService;

	@GetMapping("/rest/discount")
	public ResponseEntity<List<Discount>> getAll() {
		return ResponseEntity.ok(discountService.findAll());
	}
}
