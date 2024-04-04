package poly.edu.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Service.UpdateHistoryProductService;
import poly.edu.entity.UpdateHistoryProduct;
import poly.edu.repository.UpdateHistoryProductRepository;

@Service
public class UpdateHistoryProductServiceImpl implements UpdateHistoryProductService{

    @Autowired
    UpdateHistoryProductRepository historyProductRepository;

    @Override
    public void save(UpdateHistoryProduct update) {
        historyProductRepository.save(update);
    }
    
}
