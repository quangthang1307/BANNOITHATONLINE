package poly.edu.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import poly.edu.Service.ProductImageService;
import poly.edu.entity.ProductImage;

@CrossOrigin("*")
@RestController
public class RestProductImageController {
    @Autowired
    private ProductImageService productImageService;


    @GetMapping("/rest/products/{productID}")
    public ResponseEntity<List<ProductImage>> getProducts(@PathVariable Integer productID){
        List<ProductImage> listProductImages = productImageService.getProductImageById(productID);
        
        return ResponseEntity.ok(listProductImages);
        
    }
}
