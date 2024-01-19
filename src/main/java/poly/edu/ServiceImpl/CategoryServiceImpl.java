package poly.edu.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Service.CategoryService;
import poly.edu.entity.Categoryproduct;
import poly.edu.repository.CategoryProductRepository;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    CategoryProductRepository categoryProductRepository;

    @Override
    public List<Categoryproduct> findAllCapCon() {
        return categoryProductRepository.getAllCategoryProductCapCon();
    }
    
}
