package poly.edu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import poly.edu.entity.Inventory;
import poly.edu.entity.Product;
import poly.edu.entity.Warehouses;

@Repository
public interface InventoryRepository
    extends JpaRepository<Inventory, Integer> {

  Inventory findByProduct(Product product);

  @Query("SELECT MAX(inventoryId) FROM Inventory")

  Integer getMaxId();

  Optional<Inventory> findByWarehouseAndProduct(

      Warehouses warehouses,

      Product product

  );

  @Query(

      value = "SELECT SUM(i.quantityonhand) FROM Inventory i WHERE i.warehouse.warehouseID = :warehouseID"

  )

  int findTotalQuantityByWarehouse(@Param("warehouseID") Integer warehouseID);

}
