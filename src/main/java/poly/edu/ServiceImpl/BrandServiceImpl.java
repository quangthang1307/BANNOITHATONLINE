package poly.edu.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Service.BrandService;
import poly.edu.entity.Brands;
import poly.edu.repository.BrandRepository;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    BrandRepository brandRepository;

    @Override
    public List<Brands> findAll() {
        // TODO Auto-generated method stub
        return brandRepository.findAll();

    }

    @Override
    public Brands create(Brands brands) {
        // TODO Auto-generated method stub
        return brandRepository.save(brands);
    }

    @Override
    public Brands update(Brands brands) {
        // TODO Auto-generated method stub
        return brandRepository.save(brands);
    }

    @Override
    public void delete(Integer brandsId) {
        // TODO Auto-generated method stub
        brandRepository.deleteById(brandsId);
    }

    @Override
    public Brands findById(Integer brandsId) {
        // TODO Auto-generated method stub
        return brandRepository.findById(brandsId).get();
    }
    
    @Override
	public boolean existsByNameIgnoreCase(String brandname) {
	    return brandRepository.existsByNameIgnoreCase(brandname) > 0;
	}

}
