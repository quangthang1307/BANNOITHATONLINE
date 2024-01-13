package poly.edu.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import poly.edu.Responsitory.CartResponsitory;
import poly.edu.Service.CartService;
import poly.edu.entity.Cart;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartResponsitory cartRepository;

    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    @Override
	public Optional<Cart> findById(Integer id) {
		return cartRepository.findById(id);
	}

	@Override
	public Page<Cart> findAll(Pageable pageable) {
		return cartRepository.findAll(pageable);
	}

	@Override
	public Cart create(Cart cart) {
		return cartRepository.save(cart);
	}

	@Override
	public Cart update(Cart cart) {
		return cartRepository.save(cart);
	}

	@Override
	public void delete(Integer id) {
		cartRepository.deleteById(id);
		
	}

    @Override
    public List<Cart> findByCustomerId(Integer customerId) {
        return cartRepository.findByCustomerId(customerId);
    }

    
}
