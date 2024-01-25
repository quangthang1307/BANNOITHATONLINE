package poly.edu.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import poly.edu.Service.ProductService;
import poly.edu.Service.SaleService;
import poly.edu.entity.Product;
import poly.edu.entity.Sale;

@CrossOrigin("*")
@RestController
public class RestProductController {

     @Autowired
    private ProductService productService;
     @Autowired
     private SaleService saleService;



    @GetMapping("/rest/product")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findAll(pageRequest);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/{productId}")
    public ResponseEntity<Product> getProductDetails(@PathVariable Integer productId) {
        Optional<Product> productOptional = productService.findById(productId);
        Product product = productOptional.get();
        return ResponseEntity.ok(product);
    
    }

    @GetMapping("/rest/product/category")
    public ResponseEntity<Page<Product>> getProductByCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "categoryId") Integer categoryId) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findByCategory(pageRequest, categoryId);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/sale")
    public ResponseEntity<List<Sale>> getProductByCategory(){
        return ResponseEntity.ok(saleService.findAllOnSale());
    }
}
