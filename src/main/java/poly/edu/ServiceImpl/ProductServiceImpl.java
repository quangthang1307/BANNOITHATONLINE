package poly.edu.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import poly.edu.Service.ProductService;
import poly.edu.entity.Product;
import poly.edu.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productResponsitory;

    @Override
    public Page<Product> findAll(Pageable pageable) {      
        return productResponsitory.findAllProducts(pageable);
    }

    @Override
	public Optional<Product> findById(int id) {
		return productResponsitory.findById(id);
	}

    @Override
    public Page<Product> findByCategory(Pageable pageable) {
        return productResponsitory.findProductByCategory(pageable);
    }

    // @Override
    // public List<Product> findAll() {
    //     return productResponsitory.findAllProducts();
    // }
}
