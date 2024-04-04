package poly.edu.ServiceImpl;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    @Override
    public void save(Sale sale) {
        saleRepository.save(sale);
    }

    @Override
    public void delete(Integer id) {
        saleRepository.deleteById(id);
    }

    @Override
    public Optional<Sale> findById(Integer id) {
       return saleRepository.findById(id);
    }
    
}
