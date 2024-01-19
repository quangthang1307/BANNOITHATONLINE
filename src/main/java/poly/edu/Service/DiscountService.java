package poly.edu.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import poly.edu.entity.Discount;

public interface DiscountService {
	
	public List<Discount> findAll();
	public Optional<Discount> findById(Integer id) ;	
	
	public Page<Discount> findAll(Pageable pageable) ;
	
	public Discount create(Discount accountRole) ;

	public Discount update(Discount accountRole) ;

	public void delete(Integer id) ;
	public Discount findByCode(String code);
	
	
	Optional<Discount> findById(int id);
	void save(Discount discount);
	void delete(int id);
}
