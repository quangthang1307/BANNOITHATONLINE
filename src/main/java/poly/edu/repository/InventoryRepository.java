package poly.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import poly.edu.entity.Inventory;
import poly.edu.entity.Product;


@Repository
public interface InventoryRepository
  extends JpaRepository<Inventory, Integer> {

    Inventory findByProduct(Product product);


  }

