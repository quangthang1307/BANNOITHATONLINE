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

    void save(Product product);
    void deleteById(int productid);

    Page<Product> findAll(Pageable pageable);

    List<Product> findTop4ForSearch(String searchValue);

    List<Product> findAllNoActive();

    List<Product> findAllByAllId(List<Integer> productid);

    Optional<Product> findById(int id);

    Page<Product> findByRoom(Pageable pageable, Integer category);

    Page<Product> findByRoomDESC(Pageable pageable, Integer category);

    Page<Product> findByRoomASC(Pageable pageable, Integer category);

    Page<Product> findByRoomAZ(Pageable pageable, Integer category);

    Page<Product> findByRoomZA(Pageable pageable, Integer category);

    Page<Product> findByCategory(Pageable pageable, List<Integer> category, double minprice, double maxprice);

    List<Product> findByCategoryAndPrice(List<Integer> category, double minprice, double maxprice);

    List<Product> findByPrice(double minprice, double maxprice);

    List<Product> findSaleByCategoryAndPrice(List<Integer> category, double minprice, double maxprice);

    List<Product> findSaleByPrice(double minprice, double maxprice);

    Page<Product> findByCategoryDESC(Pageable pageable, List<Integer> category);

    Page<Product> findByCategoryASC(Pageable pageable, List<Integer> category);

    Page<Product> findByCategoryAZ(Pageable pageable, List<Integer> category);

    Page<Product> findByCategoryZA(Pageable pageable, List<Integer> category);

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
