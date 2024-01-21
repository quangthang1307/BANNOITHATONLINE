package poly.edu.RestController;

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
import poly.edu.entity.Cart;
import poly.edu.repository.CartRepository;

@CrossOrigin("*")
@RestController
public class RestCartController {

    @Autowired
    CartService cartService;

    @Autowired
    CartRepository cartResponsitory;

    @GetMapping("/rest/showCart/{customerId}")
    public ResponseEntity<List<Cart>> getAllCarts(@PathVariable Integer customerId) {
        List<Cart> carts = cartService.findByCustomerId(customerId);

        List<Cart> filteredCarts = carts.stream()
                .filter(cart -> cart.getProduct().isProductactivate())
                .collect(Collectors.toList());
        return ResponseEntity.ok(filteredCarts);
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
    public ResponseEntity<?> upCartQuantity(@PathVariable Integer customerId, @PathVariable Integer productId, @RequestParam Integer quantity) {
        try {
            Optional<Cart> cartEntry = cartService.findByCustomerAndProduct(customerId, productId);

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
    public ResponseEntity<?> checkCart(@PathVariable Integer customerId, @PathVariable Integer productId){
        Optional<Cart> cart = cartResponsitory.findByCustomer_CustomerIdAndProduct_ProductID(customerId, productId);
        if(cart.isPresent()){
            return ResponseEntity.ok(cart.get());
        }else{
            return ResponseEntity.notFound().build();
        }
        
        
    }

}
