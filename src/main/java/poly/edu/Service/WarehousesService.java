package poly.edu.Service;

import java.util.List;
import poly.edu.entity.Warehouses;

public interface WarehousesService {
  public List<Warehouses> findAll();

  // public Warehouses findById(Integer WarehousesId);

  // /**
  //  * Lấy danh sách tất cả các nhà sản xuất theo trang.
  //  */

  // /**
  //  * Tìm một nhà sản xuất theo ID.
  //  */

  // /**
  //  * Tạo một nhà sản xuất mới.
  //  */
  // public Warehouses create(Warehouses Warehouses);

  // /**
  //  * Cập nhật thông tin của một nhà sản xuất.
  //  */
  public Warehouses update(Warehouses Warehouses);
  // /**
  //  * Xóa một nhà sản xuất.
  //  */
  // public void delete(Integer WarehousesId);
  // //   public boolean existsByNameIgnoreCase(String Brandname);
}
