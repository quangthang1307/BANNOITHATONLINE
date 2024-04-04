package poly.edu.Service;

import java.util.List;
import java.util.Optional;

import poly.edu.entity.Sale;

public interface SaleService {
    List<Sale> findAllOnSale();

    List<Sale> findAll();

    Optional<Sale> findById(Integer id);

    void save(Sale sale);

    void delete(Integer id);

}
