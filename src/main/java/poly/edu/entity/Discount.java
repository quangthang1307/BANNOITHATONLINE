package poly.edu.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Discount")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DiscountID")
    private Integer discountId;

    @Column(name = "Code")
    private String code;

    @Column(name = "[Percent]")
    private Integer percent;

    @Column(name = "Startdate")
    private LocalDateTime StartDate;

    @Column(name = "Enddate")
    private LocalDateTime EndDate;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "Quantityused")
    private Integer quantityUsed;

    @Column(name = "Description")
    private String description;

    // other fields, getters, and setters
}