package poly.edu.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
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
@Table(name = "Discount")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DiscountID")
    private Integer discountId;

    @Column(name = "Code")
    @NotEmpty(message = "Vui lòng nhập mã giảm giá")
    private String code;

    @Column(name = "[Percent]")
    @NotNull(message = "Vui lòng nhập phần trăm giảm")
    @Min(value = 1, message = "Phần trăm giảm ít nhất là 1")
    @Max(value = 49, message = "Phần trăm giảm tối đa là 49")
    private Integer percent;

    @Column(name = "Startdate")
    private LocalDateTime StartDate;

    @Column(name = "Enddate")
    private LocalDateTime EndDate;

    @Column(name = "Quantity")
    @NotNull(message = "Vui lòng nhập số lượng")
    @Min(value = 1, message = "Số lượng ít nhất là 1")
    private Integer quantity;

    @Column(name = "Quantityused")
    private Integer quantityUsed;

    @Column(name = "Description")
    private String description;

    @Column(name = "Maxusage")
    @NotNull(message = "Vui lòng nhập số lần được sử dụng")
    @Min(value = 1, message = "Số lần được sử dụng ít nhất là 1")
    private Integer MaxUsage;

    // other fields, getters, and setters
}