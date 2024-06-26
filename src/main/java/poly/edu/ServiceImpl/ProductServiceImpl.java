package poly.edu.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
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
    public Page<Product> findByCategory(Pageable pageable, List<Integer> category, double minprice, double maxprice) {
        return productResponsitory.findProductByCategory(pageable, category, minprice, maxprice);
    }

    @Override
    public Integer[] findTop5ProductBestSeller() {
        return productResponsitory.findProductBestSeller();
    }

    @Override
    public Page<Product> findProductSale(Pageable pageable) {
        return productResponsitory.findProductOnSale(pageable);
    }

    @Override
    public Page<Product> findProductDESC(Pageable pageable) {
        return productResponsitory.findProductDESC(pageable);
    }

    @Override
    public Page<Product> findProductASC(Pageable pageable) {
        return productResponsitory.findProductASC(pageable);
    }

    @Override
    public Page<Product> findProductAZ(Pageable pageable) {
        return productResponsitory.findProductAZ(pageable);
    }

    @Override
    public Page<Product> findProductZA(Pageable pageable) {
        return productResponsitory.findProductZA(pageable);
    }

    @Override
    public Page<Product> findProductSaleDESC(Pageable pageable) {
        return productResponsitory.findProductOnSaleDESC(pageable);
    }

    @Override
    public Page<Product> findProductSaleASC(Pageable pageable) {
        return productResponsitory.findProductOnSaleASC(pageable);
    }

    @Override
    public Page<Product> findProductSaleAZ(Pageable pageable) {
        return productResponsitory.findProductOnSaleAZ(pageable);
    }

    @Override
    public Page<Product> findProductSaleZA(Pageable pageable) {
        return productResponsitory.findProductOnSaleZA(pageable);
    }

    @Override
    public Page<Product> findSaleByCategory(Pageable pageable, List<Integer> category) {
        return productResponsitory.findProductSaleByCategory(pageable, category);
    }

    @Override
    public Page<Product> findSaleByCategoryAndDESC(Pageable pageable, List<Integer> category) {
        return productResponsitory.findProductSaleByCategoryAndDESC(pageable, category);
    }

    @Override
    public Page<Product> findSaleByCategoryAndASC(Pageable pageable, List<Integer> category) {
        return productResponsitory.findProductSaleByCategoryAndASC(pageable, category);
    }

    @Override
    public Page<Product> findSaleByCategoryAndAZ(Pageable pageable, List<Integer> category) {
        return productResponsitory.findProductSaleByCategoryAndAZ(pageable, category);
    }

    @Override
    public Page<Product> findSaleByCategoryAndZA(Pageable pageable, List<Integer> category) {
        return productResponsitory.findProductSaleByCategoryAndZA(pageable, category);
    }

    @Override
    public List<Product> findTop5ByCategory(Integer category) {
        return productResponsitory.findTop5ProductByCategory(category);
    }

    @Override
    public Page<Product> findByRoom(Pageable pageable, Integer category) {
        return productResponsitory.findProductByRoom(pageable, category);
    }

    @Override
    public Page<Product> findByCategoryDESC(Pageable pageable, List<Integer> category) {
        return productResponsitory.findProductByCategoryDESC(pageable, category);
    }

    @Override
    public Page<Product> findByCategoryASC(Pageable pageable, List<Integer> category) {
        return productResponsitory.findProductByCategoryASC(pageable, category);
    }

    @Override
    public Page<Product> findByCategoryAZ(Pageable pageable, List<Integer> category) {
        return productResponsitory.findProductByCategoryAZ(pageable, category);
    }

    @Override
    public Page<Product> findByCategoryZA(Pageable pageable, List<Integer> category) {
        return productResponsitory.findProductByCategoryZA(pageable, category);
    }

    @Override
    public Page<Product> findByRoomDESC(Pageable pageable, Integer category) {
        return productResponsitory.findProductByRoomDESC(pageable, category);
    }

    @Override
    public Page<Product> findByRoomASC(Pageable pageable, Integer category) {
        return productResponsitory.findProductByRoomASC(pageable, category);
    }

    @Override
    public Page<Product> findByRoomAZ(Pageable pageable, Integer category) {
        return productResponsitory.findProductByRoomAZ(pageable, category);
    }

    @Override
    public Page<Product> findByRoomZA(Pageable pageable, Integer category) {
        return productResponsitory.findProductByRoomZA(pageable, category);
    }

    @Override
    public List<Product> findAllNoActive() {
        return productResponsitory.findAll();
    }

    @Override
    public void save(Product product) {
        productResponsitory.save(product);
    }

    @Override
    public void deleteById(int productid) {
        productResponsitory.deleteById(productid);
    }

    @Override
    public List<Product> findAllByAllId(List<Integer> productid) {
        return productResponsitory.findProductByAllId(productid);
    }

    @Override
    public List<Product> findTop4ForSearch(String searchValue) {
        return productResponsitory.findTop4ForSearch(searchValue);
    }

    @Override
    public List<Product> findByPrice(double minprice, double maxprice) {
        return productResponsitory.findProductByPrice(minprice, maxprice);
    }

    @Override
    public List<Product> findByCategoryAndPrice(List<Integer> category, double minprice, double maxprice) {
        return productResponsitory.findProductByCategoryAndPrice(category, minprice, maxprice);
    }

    @Override
    public List<Product> findSaleByCategoryAndPrice(List<Integer> category, double minprice, double maxprice) {
        return productResponsitory.findProductSaleByCategoryAndPrice(category, minprice, maxprice);
    }

    @Override
    public List<Product> findSaleByPrice(double minprice, double maxprice) {
        return productResponsitory.findProductSaleByPrice(minprice, maxprice);
    }

    // @Override
    // public List<Product> findAll() {
    // return productResponsitory.findAllProducts();
    // }
}
