package poly.edu.RestController;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import poly.edu.Responsitory.CartResponsitory;
import poly.edu.Service.CartService;
import poly.edu.entity.Cart;

@CrossOrigin("*")
@RestController
public class RestCartController {

    @Autowired
    CartService cartService;

    @Autowired
    CartResponsitory cartResponsitory;

    @GetMapping("/rest/showCart/{customerId}")
    public ResponseEntity<List<Cart>> getAllCarts(@PathVariable Integer customerId) {
        List<Cart> carts = cartService.findByCustomerId(customerId);

        List<Cart> filteredCarts = carts.stream()
                .filter(cart -> cart.getProduct().isProductactivate())
                .collect(Collectors.toList());

        System.out.println(carts);
        return ResponseEntity.ok(filteredCarts);
    }

    @PostMapping("/rest/addToCart")
    public ResponseEntity<Cart> addToCart(@RequestBody Cart newCartItem) {
        Cart savedCartItem = cartService.create(newCartItem);
        return new ResponseEntity<>(savedCartItem, HttpStatus.CREATED);
    }

    // @DeleteMapping("/rest/removeFromCart/{cartId}")
    // public ResponseEntity<Void> removeFromCart(@PathVariable Integer cartId) {
    //     cartService.delete(cartId);
    //     return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // }

    @DeleteMapping("/rest/removeAllCarts/{customerId}")
    public void removeAllCarts(@PathVariable Integer customerId) {
        List<Cart> carts = cartService.findByCustomerId(customerId);
        cartResponsitory.deleteAll(carts);
    }

}
