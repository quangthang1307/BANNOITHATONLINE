package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.entity.Warehouses;

@Repository
public interface WarehousesRepository
  extends JpaRepository<Warehouses, Integer> {
  Warehouses findById(int warehouseId);
}
