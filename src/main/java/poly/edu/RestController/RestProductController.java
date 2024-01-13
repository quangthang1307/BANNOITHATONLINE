package poly.edu.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import poly.edu.Service.ProductService;
import poly.edu.entity.Product;

@CrossOrigin("*")
@RestController
public class RestProductController {

     @Autowired
    private ProductService productService;



    @GetMapping("/rest/product")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products);
    }
}
