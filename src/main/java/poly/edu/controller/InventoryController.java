package poly.edu.controller;

import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import poly.edu.Service.BrandService;
import poly.edu.Service.InventoryService;
import poly.edu.entity.Inventory;
import poly.edu.entity.Product;
import poly.edu.entity.StoclMovement;
import poly.edu.entity.Warehouses;
import poly.edu.repository.InventoryRepository;
import poly.edu.repository.ProductRepository;
import poly.edu.repository.StoclMovementRepository;
import poly.edu.repository.WarehousesRepository;

@Controller
@RequestMapping("/admin")
public class InventoryController {

  @Autowired
  InventoryService inventoryService;

  @Autowired
  InventoryRepository inventoryRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  BrandService brandService;

  @Autowired
  WarehousesRepository wRepository;

  @Autowired
  StoclMovementRepository stoclRepository;

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

  @GetMapping("addInventory")
  public String addStocl(Model model) {
    StoclMovement move = new StoclMovement();
    model.addAttribute("move", move);
    model.addAttribute("warehouses", wRepository.findAll());
    model.addAttribute("products", productRepository.findAll());
    return "/admin/addInventory";
  }

  @PostMapping("/addInventoryPlus")
  public String addStocl(
    @ModelAttribute("move") StoclMovement move,
    @NotNull @RequestParam("warehouseID") Integer warehouseID,
    @NotNull @RequestParam("productId") Integer productId,
    @NotNull @RequestParam("quantity") Integer quantity,
    @RequestParam("movementType") boolean moveType,
    RedirectAttributes redirectAttributes,
    BindingResult bindingResult,
    Model model
  ) {
    model.addAttribute("warehouses", wRepository.findAll());
    model.addAttribute("products", productRepository.findAll());

    if (quantity < 0) {
      bindingResult.rejectValue(
        "quantity",
        "error.move",
        "Số lượng không hợp lệ."
      );
      return "/admin/addInventory";
    }

    try {
      Optional<Warehouses> op = wRepository.findById(warehouseID);
      Optional<Product> op2 = productRepository.findById(productId);

      if (op.isPresent() && op2.isPresent()) {
        Warehouses warehouses = op.get();
        Product product = op2.get();
        move.setProduct(product);
        move.setWarehouses(warehouses);
        move.setMovementtype(moveType);

        int warehouseCapacity = warehouses.getCapacity();

        // Check if inventory for this warehouse and product already exists
        Optional<Inventory> existingInventory = inventoryRepository.findByWarehouseAndProduct(
          warehouses,
          product
        );

        if (existingInventory.isPresent()) {
          // Update existing inventory quantity
          Inventory inventory = existingInventory.get();
          int newTotalQuantity = inventoryRepository.findTotalQuantityByWarehouse(warehouses.getWarehouseID()) +
                  (moveType ? move.getQuantity() : -move.getQuantity());

          int newQuantity = inventory.getQuantityonhand() +
                  (moveType ? move.getQuantity() : -move.getQuantity());

          if (newTotalQuantity <= warehouses.getCapacity()) {
            // Update quantity as usual
            if (newQuantity >= 0) { // Kiểm tra số lượng mới không âm
              inventory.setQuantityonhand(newQuantity);
              inventoryService.update(inventory);
              redirectAttributes.addFlashAttribute(
                      "successMessage",
                      "Cập nhật số lượng thành công!"
              );
              return "redirect:/admin/inventory";
            } else {
              // Handle negative quantity error
              bindingResult.rejectValue(
                      "quantity",
                      "error.move",
                      "Số lượng không hợp lệ!"
              );
              return "/admin/addInventory";
            }
          } else {
            // Handle exceeding capacity error
            bindingResult.rejectValue(
                    "quantity",
                    "error.move",
                    "Số lượng vượt quá dung lượng kho!"
            );
            return "/admin/addInventory";
          }
        }
        else {
          if (moveType == true) {
            int newTotalQuantity =
              inventoryRepository.findTotalQuantityByWarehouse(
                warehouses.getWarehouseID()
              ) +
              move.getQuantity();
            if (newTotalQuantity <= warehouses.getCapacity()) {
              // Create new inventory if it doesn't exist
              Inventory inventory = new Inventory();
              int maxId = inventoryRepository.getMaxId();
              if (maxId == 0) {
                maxId = 1;
              } else {
                maxId++;
              }
              inventory.setInventoryId(maxId);
              inventory.setQuantityonhand(move.getQuantity());
              inventory.setWarehouse(warehouses);
              inventory.setProduct(product);
              inventory.setLastupdatedate(new Date());
              inventoryService.create(inventory);
              redirectAttributes.addFlashAttribute(
                "successMessage",
                "Thêm thành công!"
              );
              return "redirect:/admin/inventory";
            } else {
              // Handle exceeding capacity error
              bindingResult.rejectValue(
                "quantity",
                "error.move",
                "Số lượng vượt quá dung lượng kho!"
              );
              return "/admin/addInventory";
            }
          } else {
            bindingResult.rejectValue(
              "quantity",
              "error.move",
              "Số lượng không đủ để xuất kho!"
            );
            return "/admin/addInventory";
          }
        }
      } else {
        bindingResult.rejectValue(
          "warehouseID",
          "error.move",
          "Kho hàng không tồn tại!"
        );
        bindingResult.rejectValue(
          "productId",
          "error.move",
          "Sản phẩm không tồn tại!"
        );
        return "/admin/addInventory";
      }
    } catch (Exception e) {
      e.printStackTrace();
      redirectAttributes.addFlashAttribute(
        "adderrorMessage",
        "Thêm kho thất bại: " + e.getMessage()
      );
      return "redirect:/admin/addInventory";
    }
  }
}
