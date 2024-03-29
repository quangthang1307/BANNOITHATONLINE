package poly.edu.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import poly.edu.entity.Cart;


public interface CartService {
     List<Cart> findAll();
	
	public Optional<Cart> findById(Integer id) ;
	
	public Page<Cart> findAll(Pageable pageable);
	
	public Cart create(Cart cart) ;

	public Cart update(Cart cart) ;

	public void delete(Integer id) ;

    List<Cart> findByCustomerId(Integer customerId);

    public Optional<Cart> findByCustomerAndProduct(Integer customerId, Integer productId);

    
}
