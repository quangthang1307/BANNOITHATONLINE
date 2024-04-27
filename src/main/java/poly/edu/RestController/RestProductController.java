package poly.edu.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import poly.edu.Service.FlashSaleService;
import poly.edu.Service.InventoryService;
import poly.edu.Service.ProductService;
import poly.edu.Service.SaleService;
import poly.edu.entity.Flashsale;
import poly.edu.entity.Inventory;
import poly.edu.entity.Product;
import poly.edu.entity.Sale;

@CrossOrigin("*")
@RestController
public class RestProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private SaleService saleService;
    @Autowired
    private FlashSaleService flashSaleService;
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/rest/product")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findAll(pageRequest);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/relate")
    public ResponseEntity<List<Product>> getTop5ProductByCategory(
            @RequestParam(name = "categoryId") Integer categoryId) {
        return ResponseEntity.ok(productService.findTop5ByCategory(categoryId));
    }

    @GetMapping("/rest/product/sales")
    public ResponseEntity<Page<Product>> getAllProductsSale(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findProductSale(pageRequest);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/sales/desc")
    public ResponseEntity<Page<Product>> getAllProductsSaleDESC(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findProductSaleDESC(pageRequest);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/sales/asc")
    public ResponseEntity<Page<Product>> getAllProductsSaleASC(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findProductSaleASC(pageRequest);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/sales/az")
    public ResponseEntity<Page<Product>> getAllProductsSaleAZ(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findProductSaleAZ(pageRequest);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/sales/za")
    public ResponseEntity<Page<Product>> getAllProductsSaleZA(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findProductSaleZA(pageRequest);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/desc")
    public ResponseEntity<Page<Product>> getProductsDESC(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findProductDESC(pageRequest);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/az")
    public ResponseEntity<Page<Product>> getProductsAZ(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findProductAZ(pageRequest);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/za")
    public ResponseEntity<Page<Product>> getProductsZA(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findProductZA(pageRequest);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/asc")
    public ResponseEntity<Page<Product>> getProductsASC(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findProductASC(pageRequest);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/{productId}")
    public ResponseEntity<Product> getProductDetails(@PathVariable Integer productId) {
        Optional<Product> productOptional = productService.findById(productId);
        Product product = productOptional.get();
        return ResponseEntity.ok(product);

    }

    @GetMapping("/rest/product/category")
    public ResponseEntity<Page<Product>> getProductByCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "categoryId") List<Integer> categoryId,
            @RequestParam(defaultValue = "0") int minprice,
            @RequestParam(defaultValue = "1000000000") int maxprice) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findByCategory(pageRequest, categoryId, minprice, maxprice);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/categoryprice")
    public ResponseEntity<List<Product>> getProductByCategoryAndPrice(
            @RequestParam(name = "categoryId") List<Integer> categoryId,
            @RequestParam(defaultValue = "0") int minprice,
            @RequestParam(defaultValue = "1000000000") int maxprice) {

        List<Product> productPage = productService.findByCategoryAndPrice(categoryId, minprice, maxprice);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/priceon")
    public ResponseEntity<List<Product>> getProductByPrice(
            @RequestParam(defaultValue = "0") int minprice,
            @RequestParam(defaultValue = "1000000000") int maxprice) {

        List<Product> productPage = productService.findByPrice(minprice, maxprice);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/productsale/categoryprice")
    public ResponseEntity<List<Product>> getProductSaleByCategoryAndPrice(
            @RequestParam(name = "categoryId") List<Integer> categoryId,
            @RequestParam(defaultValue = "0") int minprice,
            @RequestParam(defaultValue = "1000000000") int maxprice) {

        List<Product> productPage = productService.findSaleByCategoryAndPrice(categoryId, minprice, maxprice);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/productsale/priceon")
    public ResponseEntity<List<Product>> getProductSaleByPrice(
            @RequestParam(defaultValue = "0") int minprice,
            @RequestParam(defaultValue = "1000000000") int maxprice) {

        List<Product> productPage = productService.findSaleByPrice(minprice, maxprice);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/category/desc")
    public ResponseEntity<Page<Product>> getProductByCategoryDESC(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "categoryId") List<Integer> categoryId) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findByCategoryDESC(pageRequest, categoryId);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/category/asc")
    public ResponseEntity<Page<Product>> getProductByCategoryASC(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "categoryId") List<Integer> categoryId) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findByCategoryASC(pageRequest, categoryId);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/category/az")
    public ResponseEntity<Page<Product>> getProductByCategoryAZ(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "categoryId") List<Integer> categoryId) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findByCategoryAZ(pageRequest, categoryId);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/category/za")
    public ResponseEntity<Page<Product>> getProductByCategoryZA(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "categoryId") List<Integer> categoryId) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findByCategoryZA(pageRequest, categoryId);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/sales/category")
    public ResponseEntity<Page<Product>> getProductSaleByCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "categoryId") List<Integer> categoryId) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findSaleByCategory(pageRequest, categoryId);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/sales/category/desc")
    public ResponseEntity<Page<Product>> getProductSaleByCategoryAndDESC(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "categoryId") List<Integer> categoryId) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findSaleByCategoryAndDESC(pageRequest, categoryId);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/sales/category/asc")
    public ResponseEntity<Page<Product>> getProductSaleByCategoryAndASC(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "categoryId") List<Integer> categoryId) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findSaleByCategoryAndASC(pageRequest, categoryId);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/sales/category/az")
    public ResponseEntity<Page<Product>> getProductSaleByCategoryAndAZ(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "categoryId") List<Integer> categoryId) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findSaleByCategoryAndAZ(pageRequest, categoryId);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/sales/category/za")
    public ResponseEntity<Page<Product>> getProductSaleByCategoryAndZA(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "categoryId") List<Integer> categoryId) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findSaleByCategoryAndZA(pageRequest, categoryId);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/category/room")
    public ResponseEntity<Page<Product>> getProductByRoom(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "categoryId") Integer categoryId) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findByRoom(pageRequest, categoryId);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/category/room/desc")
    public ResponseEntity<Page<Product>> getProductByRoomDESC(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "categoryId") Integer categoryId) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findByRoomDESC(pageRequest, categoryId);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/category/room/asc")
    public ResponseEntity<Page<Product>> getProductByRoomASC(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "categoryId") Integer categoryId) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findByRoomASC(pageRequest, categoryId);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/category/room/az")
    public ResponseEntity<Page<Product>> getProductByRoomAZ(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "categoryId") Integer categoryId) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findByRoomAZ(pageRequest, categoryId);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/category/room/za")
    public ResponseEntity<Page<Product>> getProductByRoomZA(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "categoryId") Integer categoryId) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productService.findByRoomZA(pageRequest, categoryId);

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/rest/product/sale")
    public ResponseEntity<List<Sale>> getProductByCategory() {
        return ResponseEntity.ok(saleService.findAllOnSale());
    }

    @GetMapping("/rest/product/flashsale")
    public ResponseEntity<List<Flashsale>> getProductFlashsale() {
        return ResponseEntity.ok(flashSaleService.getFlashsalesNowAll());
    }

    @GetMapping("/rest/product/bestseller")
    public ResponseEntity<Optional<List<Product>>> getProductBestSeller() {
        Integer[] idproduct = productService.findTop5ProductBestSeller();

        if (idproduct != null && idproduct.length > 0) {
            List<Product> productbestseller = new ArrayList();

            for (int i = 0; i <= idproduct.length - 1; i++) {
                Optional<Product> product = productService.findById(idproduct[i]);
                productbestseller.add(product.get());
            }

            Optional<List<Product>> optionalProductList = Optional.ofNullable(productbestseller);
            return ResponseEntity.ok(optionalProductList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/rest/product/inventory")
    public ResponseEntity<Optional<List<Inventory>>> getInventory() {
        List<Inventory> list = inventoryService.findAll();
        Optional<List<Inventory>> inventory = Optional.ofNullable(list);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/rest/product/search/{searchvalue}")
    public ResponseEntity<Optional<List<Product>>> getTop4ForSearchProduct(
            @PathVariable("searchvalue") String searchvalue) {
        List<Product> list = productService.findTop4ForSearch(searchvalue);
        Optional<List<Product>> products = Optional.ofNullable(list);
        return ResponseEntity.ok(products);
    }
}
