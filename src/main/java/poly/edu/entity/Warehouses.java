package poly.edu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Warehouses")
public class Warehouses {

  // các field và annotation validation

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "WarehouseID")
  private Integer warehouseID;

  @NotBlank(message = "Tên kho không được để trống")
  @Size(max = 255, message = "Tên kho không được vượt quá 255 ký tự")
  @Column(name = "Warehousename")
  private String warehousename;

  @NotBlank(message = "Vị trí không được để trống")
  @Column(name = "Location")
  private String location;

  @Column(name = "Capacity")
  @Min(value = 1, message = "Dung lượng phải lớn hơn hoặc bằng 0")
  private int capacity;

  public int getWarehouseID() {
    return warehouseID;
  }

  public void setWarehouseID(int warehouseID) {
    this.warehouseID = warehouseID;
  }

  public String getWarehousename() {
    return warehousename;
  }

  public void setWarehousename(String warehousename) {
    this.warehousename = warehousename;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }
}
