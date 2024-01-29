package poly.edu.Service;

import java.util.List;

import poly.edu.entity.Brands;

public interface BrandService {

    public List<Brands> findAll();

    public Brands findById(Integer brandsId);

    /**
     * Lấy danh sách tất cả các nhà sản xuất theo trang.
     */

    /**
     * Tìm một nhà sản xuất theo ID.
     */

    /**
     * Tạo một nhà sản xuất mới.
     */
    public Brands create(Brands brands);

    /**
     * Cập nhật thông tin của một nhà sản xuất.
     */
    public Brands update(Brands brands);

    /**
     * Xóa một nhà sản xuất.
     */
    public void delete(Integer brandsId);

}
