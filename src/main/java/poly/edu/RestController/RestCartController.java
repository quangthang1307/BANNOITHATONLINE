package poly.edu.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import poly.edu.Service.CartService;
import poly.edu.entity.Flashsale;
import poly.edu.entity.Inventory;
import poly.edu.entity.Orderdetails;
import poly.edu.entity.Cart;
import poly.edu.entity.FlashSaleHour;
import poly.edu.entity.Product;
import poly.edu.entity.Sale;
import poly.edu.repository.CartRepository;
import poly.edu.repository.FlashSaleHourRepository;
import poly.edu.repository.FlashSaleRepository;
import poly.edu.repository.InventoryRepository;
import poly.edu.repository.ProductRepository;
import poly.edu.repository.SaleRepository;

@CrossOrigin("*")
@RestController
public class RestCartController {

    @Autowired
    CartService cartService;

    @Autowired
    CartRepository cartResponsitory;

    @Autowired
    SaleRepository saleRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    FlashSaleRepository flashSaleRepository;

    @Autowired
    FlashSaleHourRepository flashSaleHourRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    // @GetMapping("/rest/showCart/{customerId}")
    // public ResponseEntity<List<Cart>> getAllCarts(@PathVariable Integer
    // customerId) {
    // List<Cart> carts = cartService.findByCustomerId(customerId);
    // List<Flashsale> flash = new ArrayList<>();
    // List<Cart> newCart = new ArrayList<Cart>();

    // for (Cart cart2 : carts) {
    // if (cart2.getProduct().isProductactivate()) {
    // Sale sale =
    // saleRepository.findByProductID(cart2.getProduct().getProductid());
    // List<Flashsale> falshSale =
    // flashSaleRepository.findByProduct(cart2.getProduct());
    // boolean giamgia = false;

    // if(falshSale.size() > 0){
    // for (Flashsale f : falshSale) {
    // if(f.getStatus()){
    // flash.add(f);
    // break;
    // }
    // }
    // }
    // if (flash != null && !flash.isEmpty() && flash.get(0).getStatus()) {
    // Optional<FlashSaleHour> flashSaleHour = flashSaleHourRepository
    // .findById(flash.get(0).getFlashSaleHourID());
    // if (flashSaleHour.isPresent()) {
    // if (flashSaleHour.get().getStatus()
    // && flashSaleHour.get().getDateStart().isEqual(LocalDate.now())
    // && flashSaleHour.get().getHourStart().isBefore(LocalTime.now())
    // && flashSaleHour.get().getHourEnd().isAfter(LocalTime.now())) {
    // Optional<Product> product =
    // productRepository.findById(cart2.getProduct().getProductid());
    // product.get().setPricexuat(product.get().getPricexuat()
    // - (product.get().getPricexuat() * flash.get(0).getPercent() / 100));
    // cart2.setProduct(product.get());
    // giamgia = true;
    // }
    // }
    // }
    // if (sale != null && !giamgia) {
    // if (LocalDateTime.now().isAfter(sale.getDayStart())
    // && LocalDateTime.now().isBefore(sale.getDayEnd())) {
    // Optional<Product> product = productRepository.findById(sale.getProductID());
    // product.get().setPricexuat(product.get().getPricexuat()
    // - (product.get().getPricexuat() * sale.getPercent() / 100));
    // cart2.setProduct(product.get());
    // }

    // }
    // newCart.add(cart2);
    // }
    // }

    // List<Cart> filteredCarts = newCart.stream()
    // .filter(cart -> cart.getProduct().isProductactivate())
    // .collect(Collectors.toList());

    // List<Cart> show = new ArrayList<>();

    // for (Cart cart3 : filteredCarts) {
    // Inventory inventory = inventoryRepository.findByProduct(cart3.getProduct());
    // if(inventory.getQuantityonhand() > 0){
    // show.add(cart3);
    // }
    // if(inventory.getQuantityonhand() < cart3.getQuantity()){
    // cart3.setQuantity(inventory.getQuantityonhand());
    // }
    // }
    // return ResponseEntity.ok(show);
    // }

    @GetMapping("/rest/showCart/{customerId}")
    public ResponseEntity<List<Cart>> getAllCarts(@PathVariable Integer customerId) {
        List<Cart> carts = cartService.findByCustomerId(customerId);
        List<Flashsale> flash = new ArrayList<>();
        List<Cart> newCart = new ArrayList<Cart>();

        for (Cart cart2 : carts) {
            if (cart2.getProduct().isProductactivate()) {
                Sale sale = saleRepository.findByProductID(cart2.getProduct().getProductid());
                List<Flashsale> falshSale = flashSaleRepository.findByProduct(cart2.getProduct());
                boolean giamgia = false;

                if (falshSale.size() > 0) {
                    for (Flashsale f : falshSale) {
                        if (f.getStatus()) {
                            flash.add(f);
                            break;
                        }
                    }
                }

                for(Flashsale f2 : falshSale) {
                    if(cart2.getProduct().getProductid() == f2.getProduct().getProductid()){
                        if (flash != null && !flash.isEmpty() && flash.get(0).getStatus()) {
                            Optional<FlashSaleHour> flashSaleHour = flashSaleHourRepository
                                    .findById(flash.get(0).getFlashSaleHourID());
                            if (flashSaleHour.isPresent()) {
                                if (flashSaleHour.get().getStatus()
                                        && flashSaleHour.get().getDateStart().isEqual(LocalDate.now())
                                        && flashSaleHour.get().getHourStart().isBefore(LocalTime.now())
                                        && flashSaleHour.get().getHourEnd().isAfter(LocalTime.now())) {
                                    Optional<Product> product = productRepository.findById(cart2.getProduct().getProductid());
                                    product.get().setPricexuat(product.get().getPricexuat()
                                            - (product.get().getPricexuat() * flash.get(0).getPercent() / 100));
                                    cart2.setProduct(product.get());
                                    giamgia = true;
                                }
                            }
                        }
                    }
                }

                

                if (sale != null && !giamgia) {
                    if (LocalDateTime.now().isAfter(sale.getDayStart())
                            && LocalDateTime.now().isBefore(sale.getDayEnd())) {
                        Optional<Product> product = productRepository.findById(sale.getProductID());
                        product.get().setPricexuat(product.get().getPricexuat()
                                - (product.get().getPricexuat() * sale.getPercent() / 100));
                        cart2.setProduct(product.get());
                    }

                }
                newCart.add(cart2);
            }
        }

        List<Cart> filteredCarts = newCart.stream()
                .filter(cart -> cart.getProduct().isProductactivate())
                .collect(Collectors.toList());

        List<Cart> show = new ArrayList<>();

        for (Cart cart3 : filteredCarts) {
            Inventory inventory = inventoryRepository.findByProduct(cart3.getProduct());
            if (inventory.getQuantityonhand() > 0) {
                show.add(cart3);
            }
            if (inventory.getQuantityonhand() < cart3.getQuantity()) {
                cart3.setQuantity(inventory.getQuantityonhand());
            }
        }
        return ResponseEntity.ok(show);
    }

    @PostMapping("/rest/addToCart")
    public ResponseEntity<Cart> addToCart(@RequestBody Cart newCartItem) {
        Cart savedCartItem = cartService.create(newCartItem);
        return new ResponseEntity<>(savedCartItem, HttpStatus.CREATED);
    }

    @DeleteMapping("/rest/removeFromCart/{cartId}")
    public void removeFromCart(@PathVariable Integer cartId) {
        cartService.delete(cartId);
    }

    @DeleteMapping("/rest/removeAllCarts/{customerId}")
    public void removeAllCarts(@PathVariable Integer customerId) {
        List<Cart> carts = cartService.findByCustomerId(customerId);
        cartResponsitory.deleteAll(carts);
    }

    @PutMapping("/rest/cart/up/{customerId}/{productId}")
    public ResponseEntity<?> upCartQuantity(@PathVariable Integer customerId, @PathVariable Integer productId,
            @RequestParam Integer quantity) {
        try {
            Optional<Cart> cartEntry = cartService.findByCustomerAndProduct(customerId, productId);
            Optional<Product> product = productRepository.findById(productId);
            if (product.isPresent()) {
                Inventory inventory = inventoryRepository.findByProduct(product.get());
                if (inventory.getQuantityonhand() < quantity) {
                    return ResponseEntity
                            .status(HttpStatus.SEE_OTHER)
                            .body(inventory.getQuantityonhand());
                }
            }

            if (cartEntry.isPresent()) {
                Cart cart = cartEntry.get();
                int newQuantity = quantity;
                cart.setQuantity(newQuantity);
                cartService.update(cart);
                return new ResponseEntity<>(cart, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cart entry not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update cart quantity: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/rest/cart/down/{customerId}/{productId}")
    public ResponseEntity<?> downCartQuantity(@PathVariable Integer customerId, @PathVariable Integer productId) {
        try {
            Optional<Cart> cartEntry = cartService.findByCustomerAndProduct(customerId, productId);

            if (cartEntry.isPresent()) {
                Cart cart = cartEntry.get();
                if (cart.getQuantity() <= 1) {
                    return new ResponseEntity<>("Quantity not < 1", HttpStatus.BAD_REQUEST);
                }
                int newQuantity = cart.getQuantity() - 1;
                cart.setQuantity(newQuantity);
                cartService.update(cart);
                return new ResponseEntity<>(cart, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cart entry not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update cart quantity: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/rest/cart/{customerId}/{productId}")
    public ResponseEntity<?> checkCart(@PathVariable Integer customerId, @PathVariable Integer productId) {
        Optional<Cart> cart = cartResponsitory.findByCustomer_CustomerIdAndProduct_ProductID(customerId, productId);
        if (cart.isPresent()) {
            return ResponseEntity.ok(cart.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

}
