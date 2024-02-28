package poly.edu.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import poly.edu.Service.DiscountService;
import poly.edu.entity.Discount;
import poly.edu.repository.DiscountRepository;


@Service
public class DiscountServiceImpl implements DiscountService {
@Autowired DiscountRepository discountRepository;
	
	@Override
	public Optional<Discount> findById(Integer id) {
		return discountRepository.findById(id);
	}

	@Override
	public Page<Discount> findAll(Pageable pageable) {
		return discountRepository.findAll(pageable);
	}

	@Override
	public Discount create(Discount discount) {
		return discountRepository.save(discount);
	}

	@Override
	public Discount update(Discount discount) {
		return discountRepository.save(discount);
	}

	@Override
	public void delete(Integer id) {
		discountRepository.deleteById(id);
		
	}

	@Override
	public List<Discount> findAll() {
		return discountRepository.findAll();
	}

	@Override
	public Optional<Discount> findById(int id) {
		return discountRepository.findById(id);
	}

	@Override
	public void save(Discount discount) {
		discountRepository.save(discount);
	}

	@Override
	public void delete(int id) {
		discountRepository.deleteById(id);
	}

	@Override
	public Discount findByCode(String code) {
		return discountRepository.findName(code);
	}

	@Override
	public List<Discount> findTop4Discount() {
		return discountRepository.findTop4Discount();
	}

}
