package poly.edu.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import poly.edu.entity.Product;

@Service
public interface ProductService {
    // List<Product> findAll();
    Page<Product> findAll(Pageable pageable);
    Optional<Product> findById(int id);
}
