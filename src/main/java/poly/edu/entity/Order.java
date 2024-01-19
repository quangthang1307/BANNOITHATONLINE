package poly.edu.entity;




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
@Table(name = "[Order]")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID")
    private Integer OrderID;

    @Column(name = "Sumpayment")
    private Double sumpayment;

    @ManyToOne
    @JoinColumn(name = "DiscountID")
    private Discount discount;

    @ManyToOne
    @JoinColumn(name = "CustomerID")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "PaymentID")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "OrderstatusID")
    private Orderstatus orderstatus;

    @ManyToOne
    @JoinColumn(name = "AddressID")
    private Address address;



    
}
