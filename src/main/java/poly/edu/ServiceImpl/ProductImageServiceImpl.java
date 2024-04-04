package poly.edu.ServiceImpl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Service.ProductImageService;
import poly.edu.entity.ProductImage;
import poly.edu.repository.ProductImageRepository;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired ProductImageRepository productImageRepository;


    @Override
    public List<ProductImage> getProductImageById(Integer productID) {
        return productImageRepository.getProductImageById(productID);
    }


    @Override
    public void save(ProductImage productImage) {
        productImageRepository.save(productImage);
    }


    @Override
    public void deleteById(int id) {
        productImageRepository.deleteById(id);
    }

}
