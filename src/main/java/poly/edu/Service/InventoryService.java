package poly.edu.Service;

import java.util.List;
import poly.edu.entity.Inventory;

public interface InventoryService {
  public List<Inventory> findAll();

  public Inventory findById(Integer inventoryId);

  /**
   * Lấy danh sách tất cả các nhà sản xuất theo trang.
   */

  /**
   * Tìm một nhà sản xuất theo ID.
   */

  /**
   * Tạo một nhà sản xuất mới.
   */
  public Inventory create(Inventory Inventory);

  /**
   * Cập nhật thông tin của một nhà sản xuất.
   */
  public Inventory update(Inventory Inventory);

  /**
   * Xóa một nhà sản xuất.
   */
  public void delete(Integer InventoryId);
  //   public boolean existsByNameIgnoreCase(String Brandname);
}
