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
@Table(name = "Customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CustomerID")
    private Integer customerId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Phone")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "AccountID")
    private Account account;

    // @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    // private List<Address> addresses;

    // @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    // private List<Cart> carts;
}
