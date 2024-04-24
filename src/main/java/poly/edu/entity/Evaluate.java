package poly.edu.entity;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Evaluate")
public class Evaluate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EvaluateID")
    private Integer evaluateId;

    @Column(name = "ProductID")
    private Integer productId;

    @Column(name = "CustomerID")
    private Integer customerId;

    @Column(name = "Evaluatestar")
    private Integer evaluateStar;

    @Column(name = "Evaluatedate")
    private LocalDateTime evaluateDate;

    // Nếu cần, bạn có thể thêm quan hệ với các entity khác tại đây.
}
