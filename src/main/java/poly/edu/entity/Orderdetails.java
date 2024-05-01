package poly.edu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "[Orderdetails]")
public class Orderdetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderdetailsID")
    private Integer orderdetailsID;

    @Column(name = "Productquantity")
    private Integer productquantity;

    @Column(name = "Totalpayment")
    private Double totalpayment;

    @Column(name = "Price")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "OrderID")
    private Order Order;

    @ManyToOne
    @JoinColumn(name = "ProductID")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "EvaluateID")
    private Evaluate evaluate;

}
