package poly.edu.Service;

import java.util.List;

import poly.edu.entity.Productimage;


public interface ProductImageService {
    public List<Productimage> getProductImageById(Integer productID);
}
