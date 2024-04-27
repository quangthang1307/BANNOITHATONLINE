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

    // @ManyToOne
    // @JoinColumn(name = "ProductID")
    // Product product;

    @ManyToOne
    @JoinColumn(name = "CustomerID")
    Customer customer;

    @Column(name = "Evaluatestar")
    private Integer evaluateStar;

    @Column(name = "Evaluatedate")
    private LocalDateTime evaluateDate;

    @Column(name = "Comment")
    private String comment;

    @Column(name = "Image1")
    private String image1;

    @Column(name = "Image2")
    private String image2;

    @Column(name = "Image3")
    private String image3;

    @Column(name = "Image4")
    private String image4;

    @Column(name = "Image5")
    private String image5;
    // Nếu cần, bạn có thể thêm quan hệ với các entity khác tại đây.
    // Thêm getter và setter cho productId

}
