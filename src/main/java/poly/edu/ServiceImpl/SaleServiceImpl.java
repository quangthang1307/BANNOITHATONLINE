package poly.edu.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Service.SaleService;
import poly.edu.entity.Sale;
import poly.edu.repository.SaleRepository;

@Service
public class SaleServiceImpl implements SaleService{

    @Autowired
    private SaleRepository saleRepository;

    @Override
    public List<Sale> findAllOnSale() {
        return saleRepository.findProductOnSale();
    }
    
}
