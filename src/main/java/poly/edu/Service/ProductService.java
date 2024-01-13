package poly.edu.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import poly.edu.entity.Product;

@Service
public interface ProductService {
    List<Product> findAll();
}
