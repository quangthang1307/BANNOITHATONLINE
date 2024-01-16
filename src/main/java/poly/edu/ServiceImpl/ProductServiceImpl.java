package poly.edu.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Service.ProductService;
import poly.edu.entity.Product;
import poly.edu.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productResponsitory;

    @Override
    public List<Product> findAll() {
        return productResponsitory.findAllProducts();
    }
}
