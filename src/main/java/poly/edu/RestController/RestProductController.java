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
import poly.edu.entity.Product;

@CrossOrigin("*")
@RestController
public class RestProductController {

     @Autowired
    private ProductService productService;




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
}
