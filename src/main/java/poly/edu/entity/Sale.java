package poly.edu.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Sale")
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
    private Integer Percent;

    @Column(name = "Daystartsale")
    private LocalDateTime dayStart;

    @Column(name = "Dayendsale")
    private LocalDateTime dayEnd;
}
