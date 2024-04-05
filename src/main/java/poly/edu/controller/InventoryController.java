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
import poly.edu.Service.ProductService;
import poly.edu.entity.Brands;
import poly.edu.entity.Inventory;
import poly.edu.entity.Product;
import poly.edu.entity.Warehouses;
import poly.edu.repository.ProductImageRepository;
import poly.edu.repository.ProductRepository;
import poly.edu.repository.WarehousesRepository;

@Controller
@RequestMapping("/admin")
public class InventoryController {

  @Autowired
  InventoryService inventoryService;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  BrandService brandService;

  @Autowired
  WarehousesRepository wRepository;

  @GetMapping("/inventory")
  public String showList(
    @ModelAttribute("inventory") Inventory inventory,
    Model model
  ) {
    List<Inventory> list = inventoryService.findAll();

    model.addAttribute("inventories", list);
    return "admin/inventory";
  }

  @RequestMapping("/deleteInventory/{inventoryId}")
  public String deleteInventory(
    @PathVariable("inventoryId") Integer InventoryId,
    RedirectAttributes redirectAttributes
  ) {
    try {
      Inventory inventory = inventoryService.findById(InventoryId);
      inventoryService.delete(InventoryId);
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

    return "redirect:/admin/inventory";
  }

  // edit inventory
  @RequestMapping("/editInventory/{inventoryId}")
  public String editInventory(
    @PathVariable("inventoryId") Integer inventoryId,
    Model model
  ) {
    Inventory inventory = inventoryService.findById(inventoryId);
    model.addAttribute("inventory", inventory);
    model.addAttribute("brands", brandService.findAll());
    model.addAttribute("stores", wRepository.findAll());
    model.addAttribute("products", productRepository.findAll());

    return "admin/editInventory";
  }

  // @PostMapping("/updateInventory")
  // public String updateInventory(
  //   @ModelAttribute("inventory") Inventory inventory,
  //   @RequestParam("warehouseId") int warehouseId,
  //   @RequestParam("productId") int productId, // Chuyển đổi giá trị ngày từ String
  //   RedirectAttributes redirectAttributes
  // ) {
  //   try {
  //     // Tìm warehouse trong cơ sở dữ liệu bằng ID
  //     Warehouses warehouse = wRepository.findById(warehouseId);

  //     // Kiểm tra xem warehouse có null hay không
  //     if (warehouse != null) {
  //       // warehouse.setWarehouseID(warehouseId);
  //       inventory.setWarehouse(warehouse);
  //     } else {
  //       System.out.println("AAAAAAAAAAAAAAAAAAAAA");
  //     }
  //     Optional<Product> productOptional = productRepository.findById(productId);
  //     // Kiểm tra xem sản phẩm có tồn tại hay không
  //     if (productOptional.isPresent()) {
  //       Product product = productOptional.get();
  //       inventory.setProduct(product);
  //     }
  //     inventoryService.update(inventory);
  //     redirectAttributes.addFlashAttribute(
  //       "successMessage",
  //       "Cập nhật thành công!"
  //     );
  //     return "redirect:/admin/inventory";
  //   } catch (Exception e) {
  //     e.printStackTrace();
  //     // If there's an exception, add error message
  //     redirectAttributes.addFlashAttribute(
  //       "errorMessage",
  //       "Cập nhật thất bại: " + e.getMessage()
  //     );
  //     // Redirect to the inventory page
  //     return "redirect:/admin/editInventory";
  //   }
  // }

  @PostMapping("/updateInventory")
  public String updateInventory(
    @Validated @ModelAttribute("inventory") Inventory inventory,
    @RequestParam("warehouseId") int warehouseId,
    @RequestParam("productId") int productId,
    BindingResult bindingResult,
    RedirectAttributes redirectAttributes
  ) {
    try {
      if (bindingResult.hasErrors()) {
        return "redirect:/admin/editInventory";
      }
      Warehouses warehouse = wRepository.findById(warehouseId);
      if (warehouse != null) {
        inventory.setWarehouse(warehouse);
      } else {
        // // Thêm lỗi vào BindingResult nếu warehouse không tồn tại
        // bindingResult.rejectValue(
        //   "warehouseId",
        //   "error.inventory",
        //   "Warehouse không tồn tại."
        // );
        return "redirect:/admin/editInventory"; // Thay your_previous_page bằng trang bạn cần trả về
      }

      // Kiểm tra xem sản phẩm có tồn tại hay không
      Optional<Product> productOptional = productRepository.findById(productId);
      if (productOptional.isPresent()) {
        Product product = productOptional.get();
        inventory.setProduct(product);
      } else {
        return "redirect:/admin/editInventory"; // Thay your_previous_page bằng trang bạn cần trả về
      }

      if (inventory.getQuantityonhand() < 0) {
        bindingResult.rejectValue(
          "quantityonhand",
          "error.inventory",
          "Tên thương hiệu không được vượt quá 50 ký tự."
        );
      }

      // Tiếp tục xử lý logic khác

      inventoryService.update(inventory);
      redirectAttributes.addFlashAttribute(
        "successMessage",
        "Cập nhật thành công!"
      );
      return "redirect:/admin/inventory";
    } catch (Exception e) {
      e.printStackTrace();
      // Thêm lỗi chung nếu có lỗi xảy ra khi xử lý logic
      bindingResult.reject(
        "error.inventory",
        "Có lỗi xảy ra khi cập nhật Inventory: " + e.getMessage()
      );
      return "redirect:/admin/editInventory"; // Thay your_previous_page bằng trang bạn cần trả về
    }
  }

  @GetMapping("/addInventory")
  public String showAddInventoryForm(Model model) {
    model.addAttribute("inventory", new Inventory());
    model.addAttribute("warehouses", wRepository.findAll());
    model.addAttribute("products", productRepository.findAll());
    return "admin/addInventory";
  }

  @PostMapping("/addInventoryPlus")
  public String addInventory(
    @ModelAttribute("inventory") Inventory inventory,
    @RequestParam("warehouseId") int warehouseId,
    @RequestParam("productId") int productId,
    BindingResult bindingResult,
    RedirectAttributes redirectAttributes
  ) {
    try {
      Warehouses warehouse = wRepository.findById(warehouseId);

      if (warehouse != null) {
        inventory.setWarehouse(warehouse);
      }
      Optional<Product> productOptional = productRepository.findById(productId);
      // Kiểm tra xem sản phẩm có tồn tại hay không
      if (productOptional.isPresent()) {
        Product product = productOptional.get();
        inventory.setProduct(product);
      }

      inventoryService.create(inventory);
      redirectAttributes.addFlashAttribute(
        "successMessage",
        "Thêm dữ liệu thành công!"
      );
      return "redirect:/admin/inventory";
    } catch (Exception e) {
      e.printStackTrace();
      redirectAttributes.addFlashAttribute(
        "errorMessage",
        "Thêm dữ liệu thất bại: " + e.getMessage()
      );
      return "redirect:/admin/addInventory";
    }
  }
}
