package poly.edu.ServiceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.edu.Service.InventoryService;
import poly.edu.entity.Inventory;
import poly.edu.repository.InventoryRepository;

@Service
public class InventoryServiceImpl implements InventoryService {

  @Autowired
  InventoryRepository inventoryRepository;

  @Override
  public List<Inventory> findAll() {
    // TODO Auto-generated method stub
    return inventoryRepository.findAll();
  }

  @Override
  public Inventory create(Inventory Inventory) {
    // TODO Auto-generated method stub
    return inventoryRepository.save(Inventory);
  }

  @Override
  public Inventory update(Inventory Inventory) {
    // TODO Auto-generated method stub
    return inventoryRepository.save(Inventory);
  }

  @Override
  public void delete(Integer InventoryId) {
    // TODO Auto-generated method stub
    inventoryRepository.deleteById(InventoryId);
  }

  @Override
  public Inventory findById(Integer InventoryId) {
    // TODO Auto-generated method stub
    return inventoryRepository.findById(InventoryId).get();
  }
  // @Override
  // public boolean existsByNameIgnoreCase(String brandname) {
  //   return inventoryRepository.existsByNameIgnoreCase(brandname) > 0;
  // }
}
