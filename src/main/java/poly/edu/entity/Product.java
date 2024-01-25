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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    private String productname;

    @Column(name = "Description", columnDefinition = "nvarchar(MAX)")
    private String description;

    @Column(name = "Productactivate")
    private boolean productactivate;

    @Column(name = "Viewcount")
    private Integer viewcount;

    @Column(name = "BrandID")
    private Integer brand;

    @Column(name = "CategoryID")
    private Integer category;

    @Column(name = "Createddate")
    private LocalDateTime createddate;

    @Column(name = "Pricenhap")
    private Double pricenhap;

    @Column(name = "Pricexuat")
    private Double pricexuat;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductImage> productimages;

}
