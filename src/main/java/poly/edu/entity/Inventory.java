package poly.edu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Inventory")
public class Inventory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "InventoryID")
  private int inventoryId;

  @Column(name = "Quantityonhand")
  private int quantityonhand;

  @Column(name = "Lastupdatedate")
  private Date lastupdatedate = new Date();

  @ManyToOne
  @JoinColumn(name = "ProductID")
  private Product product;

  @ManyToOne
  @JoinColumn(name = "WarehouseID")
  private Warehouses warehouse;

  public int getInventoryId() {
    return inventoryId;
  }

  public void setInventoryId(int inventoryId) {
    this.inventoryId = inventoryId;
  }

  public int getQuantityonhand() {
    return quantityonhand;
  }

  public void setQuantityonhand(int quantityonhand) {
    this.quantityonhand = quantityonhand;
  }

  public Date getLastupdatedate() {
    return lastupdatedate;
  }

  public void setLastupdatedate(Date lastupdatedate) {
    this.lastupdatedate = lastupdatedate;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Warehouses getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(Warehouses warehouse) {
    this.warehouse = warehouse;
  }
}
