package poly.edu.Service;

import org.springframework.stereotype.Service;

import poly.edu.entity.UpdateHistoryProduct;

@Service
public interface UpdateHistoryProductService {
    void save(UpdateHistoryProduct update);
}
