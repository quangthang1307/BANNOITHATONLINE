package poly.edu.RestController;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import poly.edu.Service.CategoryService;
import poly.edu.entity.Categoryproduct;

@CrossOrigin("*")
@RestController
public class RestCategoryController {

    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/rest/category")
    public ResponseEntity<List<Categoryproduct>> getAllCapCon(){
        List<Categoryproduct> categoryproducts = categoryService.findAllCapCon();
           return ResponseEntity.ok(categoryproducts);
    }
}
