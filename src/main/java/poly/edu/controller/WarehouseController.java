package poly.edu.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import poly.edu.Service.BrandService;
import poly.edu.Service.InventoryService;
import poly.edu.Service.WarehousesService;
import poly.edu.entity.Inventory;
import poly.edu.entity.Product;
import poly.edu.entity.Warehouses;
import poly.edu.repository.ProductRepository;
import poly.edu.repository.WarehousesRepository;

@Controller
@RequestMapping("/admin")
public class WarehouseController {

  @Autowired
  InventoryService inventoryService;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  BrandService brandService;

  @Autowired
  WarehousesRepository wRepository;

  @Autowired
  WarehousesService wService;

  @GetMapping("/warehouse")
  public String showList(
    @ModelAttribute("warehouse") Warehouses warehouse,
    Model model
  ) {
    List<Warehouses> list = wRepository.findAll();
    model.addAttribute("warehouses", list);
    return "admin/warehouse";
  }

  @RequestMapping("/deleteWarehouse/{warehouseID}")
  public String deleteWarehouse(
    @PathVariable("warehouseID") Integer warehouseID,
    RedirectAttributes redirectAttributes
  ) {
    try {
      wRepository.deleteById(warehouseID);
      redirectAttributes.addFlashAttribute(
        "deletesuccessMessage",
        "Xóa thành công!"
      );
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute(
        "deleteerrorMessage",
        "Xóa thất bại: " + e.getMessage()
      );
    }
    return "redirect:/admin/warehouse";
  }

  // edit inventory
  @RequestMapping("/editWarehouse/{warehouseId}")
  public String editWarehouse(
    @PathVariable("warehouseId") Integer warehouseId,
    Model model
  ) {
    Optional<Warehouses> warehouse = wRepository.findById(warehouseId);
    model.addAttribute("warehouse", warehouse);
    return "admin/editWarehouse";
  }

  @PostMapping("/updateWarehouse")
  public String updateWarehouse(
    @ModelAttribute("warehouse") Warehouses warehouse,
    BindingResult bindingResult,
    RedirectAttributes redirectAttributes
  ) {
    try {
      if (warehouse.getWarehousename().isEmpty()) {
        bindingResult.rejectValue(
          "warehousename",
          "error.warehouse",
          "Tên kho không hợp lệ."
        );
        return "/admin/editWarehouse"; // Trả về trang brand nếu tên thương hiệu vượt quá 50 ký tự
      } else if (warehouse.getWarehousename().length() > 255) {
        bindingResult.rejectValue(
          "warehousename",
          "error.warehouse",
          "Tên kho không được vượt quá 255 ký tự."
        );
      } else if (warehouse.getLocation().isEmpty()) {
        bindingResult.rejectValue(
          "location",
          "error.warehouse",
          "Vị trí kho không hợp lệ."
        );
        return "/admin/editWarehouse"; // Trả về trang brand nếu tên thương hiệu vượt quá 50 ký tự
      }

      try {
        if (warehouse.getCapacity() <= 0) {
          bindingResult.rejectValue(
            "capacity",
            "error.warehouse",
            "Dung lượng kho không hợp lệ."
          );
          return "/admin/editWarehouse"; // Trả về trang brand nếu tên thương hiệu vượt quá 50 ký tự
        }
      } catch (Exception e) {
        // TODO: handle exception
        bindingResult.rejectValue(
          "capacity",
          "error.warehouse",
          "Dung lượng kho không hợp lệ."
        );
        return "/admin/editWarehouse"; // Trả về trang brand nếu tên thương hiệu vượt quá 50 ký tự
      }

      wService.update(warehouse);

      redirectAttributes.addFlashAttribute(
        "successMessage",
        "Cập nhật thành công!"
      );
      return "redirect:/admin/warehouse";
    } catch (Exception e) {
      e.printStackTrace();
      redirectAttributes.addFlashAttribute(
        "errorMessage",
        "Cập nhật thất bại: " + e.getMessage()
      );
      return "redirect:/admin/editWarehouse";
    }
  }

  @GetMapping("/addWarehouse")
  public String showAddWarehouseForm(Model model) {
    model.addAttribute("warehouse", wRepository.findAll());
    return "admin/addWarehouse";
  }

  @PostMapping("/addWarehouses")
  public String addWarehouse(
    @ModelAttribute("warehouse") Warehouses warehouse,
    RedirectAttributes redirectAttributes,
    BindingResult bindingResult
  ) {
    try {
      if (warehouse.getWarehousename().isEmpty()) {
        bindingResult.rejectValue(
          "warehousename",
          "error.warehouse",
          "Tên kho không hợp lệ."
        );
        return "/admin/addWarehouse"; // Trả về trang thêm kho nếu có lỗi
      } else if (warehouse.getWarehousename().length() > 255) {
        bindingResult.rejectValue(
          "warehousename",
          "error.warehouse",
          "Tên kho không được vượt quá 255 ký tự."
        );
        return "/admin/addWarehouse"; // Trả về trang thêm kho nếu có lỗi
      } else if (warehouse.getLocation().isEmpty()) {
        bindingResult.rejectValue(
          "location",
          "error.warehouse",
          "Vị trí kho không hợp lệ."
        );
        return "/admin/addWarehouse"; // Trả về trang thêm kho nếu có lỗi
      }
      if (warehouse.getCapacity() <= 0) {
        bindingResult.rejectValue(
          "capacity",
          "error.warehouse",
          "Dung lượng kho không hợp lệ."
        );
        return "/admin/addWarehouse"; // Trả về trang thêm kho nếu có lỗi
      }
      wRepository.save(warehouse);
      redirectAttributes.addFlashAttribute(
        "addsuccessMessage",
        "Thêm thành công!"
      );
      return "redirect:/admin/warehouse"; // Điều hướng người dùng đến trang hiển thị danh sách kho sau khi thêm thành công
    } catch (Exception e) {
      e.printStackTrace();
      redirectAttributes.addFlashAttribute(
        "adderrorMessage",
        "Thêm kho thất bại: " + e.getMessage()
      );
      return "redirect:/admin/addWarehouse"; // Nếu có lỗi, điều hướng người dùng đến trang thêm kho để thử lại
    }
  }
}
