package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.entity.Inventory;

@Repository
public interface InventoryRepository
  extends JpaRepository<Inventory, Integer> {}
