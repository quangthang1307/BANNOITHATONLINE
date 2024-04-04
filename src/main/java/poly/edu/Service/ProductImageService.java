package poly.edu.Service;

import java.util.List;

import poly.edu.entity.ProductImage;


public interface ProductImageService {
    public List<ProductImage> getProductImageById(Integer productID);

    public void save(ProductImage productImage);

    public void deleteById(int id);
}
