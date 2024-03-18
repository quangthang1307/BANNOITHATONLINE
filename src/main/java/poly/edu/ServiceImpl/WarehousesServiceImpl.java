package poly.edu.ServiceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.edu.Service.WarehousesService;
import poly.edu.entity.Warehouses;
import poly.edu.repository.WarehousesRepository;

@Service
public class WarehousesServiceImpl implements WarehousesService {

  @Autowired
  private WarehousesRepository repository;

  @Override
  public List<Warehouses> findAll() {
    return repository.findAll();
  }

  @Override
  public Warehouses update(Warehouses warehouses) {
    return repository.save(warehouses);
  }
}
