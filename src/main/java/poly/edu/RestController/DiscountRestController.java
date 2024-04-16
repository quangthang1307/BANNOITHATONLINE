package poly.edu.RestController;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import poly.edu.Service.DiscountService;
import poly.edu.entity.Customer;
import poly.edu.entity.Discount;
import poly.edu.entity.DiscountUsage;
import poly.edu.repository.AccountRepository;
import poly.edu.repository.CustomerRepository;
import poly.edu.repository.DiscountUsageRepository;


@CrossOrigin("*")
@RestController
public class DiscountRestController {
	
	@Autowired DiscountService discountService;
	@Autowired HttpSession session;
	@Autowired
    DiscountUsageRepository discountUsageRepository;
	@Autowired CustomerRepository customerRepository;
	@Autowired AccountRepository accountRepository;

	@GetMapping("/rest/discount/check")
	public ResponseEntity<?> checkCountDiscountforUser(@RequestParam String discountCode, @RequestParam String username){
		Customer customer = customerRepository.findByAccountUsername(username);
		if(customer != null){
			Discount discountfind = discountService.findByCode(discountCode);
			List<DiscountUsage> usageList = discountUsageRepository.findAllByCustomerId(customer.getCustomerId());
				int countDiscount = 0;
				for (DiscountUsage list : usageList) {
					if(list.getDiscount().getCode().equals(discountCode)){
						countDiscount++;
					}
					if (countDiscount >= discountfind.getMaxUsage()) {
						return ResponseEntity.badRequest().build();
					}
				}
                
		}
		return ResponseEntity.ok().build();	
                 
	}

	@GetMapping("/rest/discount")
	public ResponseEntity<List<Discount>> getAll() {
		return ResponseEntity.ok(discountService.findAll());
	}

	@GetMapping("/rest/discounttop4")
	public ResponseEntity<List<Discount>> getTop4Discount() {
		return ResponseEntity.ok(discountService.findTop4Discount());
	}

	@GetMapping("/rest/discount/{code}")
	public ResponseEntity<Discount> getDiscount(@PathVariable String code) {
		return ResponseEntity.ok(discountService.findByCode(code));
	}
}
