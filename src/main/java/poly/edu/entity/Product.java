package poly.edu.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductID")
    private Integer productid;

    @Column(name = "Productname")
    @NotEmpty(message = "Vui lòng nhập tên sản phẩm")
    private String productname;

    @Column(name = "Description", columnDefinition = "nvarchar(MAX)")
    private String description;

    @Column(name = "Productactivate")
    private boolean productactivate;

    @Column(name = "Viewcount")
    private Integer viewcount;

    @ManyToOne
    @JoinColumn(name = "BrandID")
    private Brands brand;

    @ManyToOne
    @JoinColumn(name = "CategoryID")
    private Categoryproduct category;

    @ManyToOne
    @JoinColumn(name = "Createdby")
    private Employee createdby;

    @Column(name = "Createddate")
    private LocalDateTime createddate;

    @Column(name = "Pricenhap")
    @NotNull(message = "Vui lòng nhập giá nhập")
    @Min(value = 0, message = "Giá không được nhỏ hơn 0")
    private Double pricenhap;

    @Column(name = "Pricexuat")
    @Min(value = 0, message = "Giá không được nhỏ hơn 0")
    @NotNull(message = "Vui lòng nhập giá bán")
    private Double pricexuat;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductImage> productimages;

}
