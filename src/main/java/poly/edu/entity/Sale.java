package poly.edu.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
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
@Table(name = "[Sale]")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SaleID")
    private Integer saleID;

    @Column(name = "Statussale")
    private Boolean status;

    @JoinColumn(name = "ProductID")
    private Integer productID;

    @Column(name = "Percentsale")
    @NotNull(message = "Vui lòng nhập phần trăm giảm")
    @Min(value = 1, message = "Phần trăm giảm ít nhất là 1")
    @Max(value = 49, message = "Phần trăm giảm tối đa là 49")
    private Integer Percent;

    @Column(name = "Daystartsale")
    private LocalDateTime dayStart;

    @Column(name = "Dayendsale")
    private LocalDateTime dayEnd;
}
