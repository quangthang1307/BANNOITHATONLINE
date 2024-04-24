package poly.edu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import poly.edu.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1", nativeQuery = true)
    Page<Product> findAllProducts(Pageable pageable);

    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 AND ProductID IN :productIDs", nativeQuery = true)
    List<Product> findProductByAllId(@Param("productIDs") List<Integer> productIDs);

    // @Query(value = "SELECT * FROM Product", nativeQuery = true)
    // Page<Product> findAllProductsNoActive(Pageable pageable);

    // @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 and
    // CategoryID = ?", nativeQuery = true)
    // Page<Product> findProductByCategory(Pageable pageable, Integer categoryID);

    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 AND categoryid IN (SELECT ID FROM Categoryproduct WHERE IDcapcha = ?)", nativeQuery = true)
    Page<Product> findProductByRoom(Pageable pageable, @Param("IDcapcha") Integer categoryID);

    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 AND categoryid IN (SELECT ID FROM Categoryproduct WHERE IDcapcha = ?) ORDER BY PriceXuat DESC", nativeQuery = true)
    Page<Product> findProductByRoomDESC(Pageable pageable, @Param("IDcapcha") Integer categoryID);

    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 AND categoryid IN (SELECT ID FROM Categoryproduct WHERE IDcapcha = ?) ORDER BY PriceXuat ASC", nativeQuery = true)
    Page<Product> findProductByRoomASC(Pageable pageable, @Param("IDcapcha") Integer categoryID);

    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 AND categoryid IN (SELECT ID FROM Categoryproduct WHERE IDcapcha = ?) ORDER BY Productname ASC", nativeQuery = true)
    Page<Product> findProductByRoomAZ(Pageable pageable, @Param("IDcapcha") Integer categoryID);

    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 AND categoryid IN (SELECT ID FROM Categoryproduct WHERE IDcapcha = ?) ORDER BY Productname DESC", nativeQuery = true)
    Page<Product> findProductByRoomZA(Pageable pageable, @Param("IDcapcha") Integer categoryID);

    @Query(value = "SELECT TOP 5 * FROM Product WHERE Productactivate = 1 AND CategoryID = ?", nativeQuery = true)
    List<Product> findTop5ProductByCategory(@Param("CategoryID") Integer categoryID);

    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 and CategoryID IN :categoryIDs AND Pricexuat > :minprice AND Pricexuat <= :maxprice ", nativeQuery = true)
    Page<Product> findProductByCategory(Pageable pageable, @Param("categoryIDs") List<Integer> categoryIDs,@Param("minprice") double minprice,@Param("maxprice") double maxprice);

    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 and CategoryID IN :categoryIDs AND Pricexuat > :minprice AND Pricexuat <= :maxprice ", nativeQuery = true)
    List<Product> findProductByCategoryAndPrice(@Param("categoryIDs") List<Integer> categoryIDs,@Param("minprice") double minprice,@Param("maxprice") double maxprice);

    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 AND Pricexuat > :minprice AND Pricexuat <= :maxprice ", nativeQuery = true)
    List<Product> findProductByPrice(@Param("minprice") double minprice, @Param("maxprice") double maxprice);

    @Query(value = "SELECT Product.BrandID, Product.CategoryID, Product.Createdby, Product.Createddate, Product.[Description], Product.PriceNhap, Product.PriceXuat, Product.Productactivate, Product.ProductID, Product.Productname, Product.Viewcount FROM Product JOIN Sale ON Product.ProductID = Sale.ProductID WHERE Sale.Statussale = 1 And Productactivate = 1 AND GETDATE() BETWEEN Sale.Daystartsale AND Sale.Dayendsale AND CategoryID IN :categoryIDs AND Pricexuat > :minprice AND Pricexuat <= :maxprice ", nativeQuery = true)
    List<Product> findProductSaleByCategoryAndPrice(@Param("categoryIDs") List<Integer> categoryIDs,@Param("minprice") double minprice,@Param("maxprice") double maxprice);

    @Query(value = "SELECT Product.BrandID, Product.CategoryID, Product.Createdby, Product.Createddate, Product.[Description], Product.PriceNhap, Product.PriceXuat, Product.Productactivate, Product.ProductID, Product.Productname, Product.Viewcount FROM Product JOIN Sale ON Product.ProductID = Sale.ProductID WHERE Sale.Statussale = 1 And Productactivate = 1 AND GETDATE() BETWEEN Sale.Daystartsale AND Sale.Dayendsale AND Pricexuat > :minprice AND Pricexuat <= :maxprice ", nativeQuery = true)
    List<Product> findProductSaleByPrice(@Param("minprice") double minprice, @Param("maxprice") double maxprice);
    
    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 and CategoryID IN :categoryIDs ORDER BY PriceXuat DESC", nativeQuery = true)
    Page<Product> findProductByCategoryDESC(Pageable pageable, @Param("categoryIDs") List<Integer> categoryIDs);

    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 and CategoryID IN :categoryIDs ORDER BY PriceXuat ASC", nativeQuery = true)
    Page<Product> findProductByCategoryASC(Pageable pageable, @Param("categoryIDs") List<Integer> categoryIDs);

    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 and CategoryID IN :categoryIDs ORDER BY Productname ASC", nativeQuery = true)
    Page<Product> findProductByCategoryAZ(Pageable pageable, @Param("categoryIDs") List<Integer> categoryIDs);

    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 and CategoryID IN :categoryIDs ORDER BY Productname DESC", nativeQuery = true)
    Page<Product> findProductByCategoryZA(Pageable pageable, @Param("categoryIDs") List<Integer> categoryIDs);

    @Query(value = "SELECT Product.BrandID, Product.CategoryID, Product.Createdby, Product.Createddate, Product.[Description], Product.PriceNhap, Product.PriceXuat, Product.Productactivate, Product.ProductID, Product.Productname, Product.Viewcount FROM Product JOIN Sale ON Product.ProductID = Sale.ProductID WHERE Sale.Statussale = 1 And Productactivate = 1 AND GETDATE() BETWEEN Sale.Daystartsale AND Sale.Dayendsale AND Product.CategoryID IN :categoryIDs", nativeQuery = true)
    Page<Product> findProductSaleByCategory(Pageable pageable, @Param("categoryIDs") List<Integer> categoryIDs);

    @Query(value = "SELECT Product.BrandID, Product.CategoryID, Product.Createdby, Product.Createddate, Product.[Description], Product.PriceNhap, Product.PriceXuat, Product.Productactivate, Product.ProductID, Product.Productname, Product.Viewcount FROM Product JOIN Sale ON Product.ProductID = Sale.ProductID WHERE Sale.Statussale = 1 And Productactivate = 1 AND GETDATE() BETWEEN Sale.Daystartsale AND Sale.Dayendsale AND Product.CategoryID IN :categoryIDs ORDER BY Product.PriceXuat DESC;", nativeQuery = true)
    Page<Product> findProductSaleByCategoryAndDESC(Pageable pageable, @Param("categoryIDs") List<Integer> categoryIDs);

    @Query(value = "SELECT Product.BrandID, Product.CategoryID, Product.Createdby, Product.Createddate, Product.[Description], Product.PriceNhap, Product.PriceXuat, Product.Productactivate, Product.ProductID, Product.Productname, Product.Viewcount FROM Product JOIN Sale ON Product.ProductID = Sale.ProductID WHERE Sale.Statussale = 1 And Productactivate = 1 AND GETDATE() BETWEEN Sale.Daystartsale AND Sale.Dayendsale AND Product.CategoryID IN :categoryIDs ORDER BY Product.PriceXuat ASC;", nativeQuery = true)
    Page<Product> findProductSaleByCategoryAndASC(Pageable pageable, @Param("categoryIDs") List<Integer> categoryIDs);

    @Query(value = "SELECT Product.BrandID, Product.CategoryID, Product.Createdby, Product.Createddate, Product.[Description], Product.PriceNhap, Product.PriceXuat, Product.Productactivate, Product.ProductID, Product.Productname, Product.Viewcount FROM Product JOIN Sale ON Product.ProductID = Sale.ProductID WHERE Sale.Statussale = 1 And Productactivate = 1 AND GETDATE() BETWEEN Sale.Daystartsale AND Sale.Dayendsale AND Product.CategoryID IN :categoryIDs ORDER BY Product.Productname ASC;", nativeQuery = true)
    Page<Product> findProductSaleByCategoryAndAZ(Pageable pageable, @Param("categoryIDs") List<Integer> categoryIDs);

    @Query(value = "SELECT Product.BrandID, Product.CategoryID, Product.Createdby, Product.Createddate, Product.[Description], Product.PriceNhap, Product.PriceXuat, Product.Productactivate, Product.ProductID, Product.Productname, Product.Viewcount FROM Product JOIN Sale ON Product.ProductID = Sale.ProductID WHERE Sale.Statussale = 1 And Productactivate = 1 AND GETDATE() BETWEEN Sale.Daystartsale AND Sale.Dayendsale AND Product.CategoryID IN :categoryIDs ORDER BY Product.Productname DESC;", nativeQuery = true)
    Page<Product> findProductSaleByCategoryAndZA(Pageable pageable, @Param("categoryIDs") List<Integer> categoryIDs);
   
    @Query(value = "SELECT TOP 5 p.ProductID FROM Product p JOIN Orderdetails od ON p.ProductID = od.ProductID WHERE p.Productactivate = 1 GROUP BY p.ProductID ORDER BY SUM(od.Productquantity) DESC;", nativeQuery = true)
    Integer[] findProductBestSeller();

    @Query(value = "SELECT Product.BrandID, Product.CategoryID, Product.Createdby, Product.Createddate, Product.[Description], Product.PriceNhap, Product.PriceXuat, Product.Productactivate, Product.ProductID, Product.Productname, Product.Viewcount FROM Product JOIN Sale ON Product.ProductID = Sale.ProductID WHERE Sale.Statussale = 1 And Productactivate = 1 AND GETDATE() BETWEEN Sale.Daystartsale AND Sale.Dayendsale", nativeQuery = true)
    Page<Product> findProductOnSale(Pageable pageable);

    @Query(value = "SELECT Product.BrandID, Product.CategoryID, Product.Createdby, Product.Createddate, Product.[Description], Product.PriceNhap, Product.PriceXuat, Product.Productactivate, Product.ProductID, Product.Productname, Product.Viewcount FROM Product JOIN Sale ON Product.ProductID = Sale.ProductID WHERE Sale.Statussale = 1 And Productactivate = 1 AND GETDATE() BETWEEN Sale.Daystartsale AND Sale.Dayendsale ORDER BY Product.PriceXuat DESC", nativeQuery = true)
    Page<Product> findProductOnSaleDESC(Pageable pageable);

    @Query(value = "SELECT Product.BrandID, Product.CategoryID, Product.Createdby, Product.Createddate, Product.[Description], Product.PriceNhap, Product.PriceXuat, Product.Productactivate, Product.ProductID, Product.Productname, Product.Viewcount FROM Product JOIN Sale ON Product.ProductID = Sale.ProductID WHERE Sale.Statussale = 1 And Productactivate = 1 AND GETDATE() BETWEEN Sale.Daystartsale AND Sale.Dayendsale ORDER BY Product.PriceXuat ASC", nativeQuery = true)
    Page<Product> findProductOnSaleASC(Pageable pageable);

    @Query(value = "SELECT Product.BrandID, Product.CategoryID, Product.Createdby, Product.Createddate, Product.[Description], Product.PriceNhap, Product.PriceXuat, Product.Productactivate, Product.ProductID, Product.Productname, Product.Viewcount FROM Product JOIN Sale ON Product.ProductID = Sale.ProductID WHERE Sale.Statussale = 1 And Productactivate = 1 AND GETDATE() BETWEEN Sale.Daystartsale AND Sale.Dayendsale ORDER BY Product.Productname ASC", nativeQuery = true)
    Page<Product> findProductOnSaleAZ(Pageable pageable);

    @Query(value = "SELECT Product.BrandID, Product.CategoryID, Product.Createdby, Product.Createddate, Product.[Description], Product.PriceNhap, Product.PriceXuat, Product.Productactivate, Product.ProductID, Product.Productname, Product.Viewcount FROM Product JOIN Sale ON Product.ProductID = Sale.ProductID WHERE Sale.Statussale = 1 And Productactivate = 1 AND GETDATE() BETWEEN Sale.Daystartsale AND Sale.Dayendsale ORDER BY Product.Productname DESC", nativeQuery = true)
    Page<Product> findProductOnSaleZA(Pageable pageable);

    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 ORDER BY PriceXuat DESC;", nativeQuery = true)
    Page<Product> findProductDESC(Pageable pageable);

    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 ORDER BY PriceXuat ASC;", nativeQuery = true)
    Page<Product> findProductASC(Pageable pageable);

    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 ORDER BY Productname ASC;;", nativeQuery = true)
    Page<Product> findProductAZ(Pageable pageable);

    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 ORDER BY Productname DESC;", nativeQuery = true)
    Page<Product> findProductZA(Pageable pageable);

    @Query(value = "SELECT * FROM Product WHERE Productactivate = 1 AND Productname LIKE %?%", nativeQuery = true)
    List<Product> findTop4ForSearch(String searchValue);






    

}
