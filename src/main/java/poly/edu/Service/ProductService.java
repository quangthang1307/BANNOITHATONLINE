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

    Page<Product> findByCategory(Pageable pageable, List<Integer> category);

    List<Product> findTop5ByCategory(Integer category);
    
    Page<Product> findSaleByCategory(Pageable pageable, List<Integer> category);

    Page<Product> findSaleByCategoryAndDESC(Pageable pageable, List<Integer> category);

    Page<Product> findSaleByCategoryAndASC(Pageable pageable, List<Integer> category);

    Page<Product> findSaleByCategoryAndAZ(Pageable pageable, List<Integer> category);

    Page<Product> findSaleByCategoryAndZA(Pageable pageable, List<Integer> category);

    Integer[] findTop5ProductBestSeller();

    Page<Product> findProductSale(Pageable pageable);

    Page<Product> findProductSaleDESC(Pageable pageable);

    Page<Product> findProductSaleASC(Pageable pageable);

    Page<Product> findProductSaleAZ(Pageable pageable);

    Page<Product> findProductSaleZA(Pageable pageable);

    Page<Product> findProductDESC(Pageable pageable);

    Page<Product> findProductASC(Pageable pageable);

    Page<Product> findProductAZ(Pageable pageable);

    Page<Product> findProductZA(Pageable pageable);


}
