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
@Table(name = "Address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AddressID")
    private Integer addressID;

    @Column(name = "Tinhthanhpho")
    private String tinhthanhpho;

    @Column(name = "Quanhuyen")
    private String quanhuyen;
    

    @Column(name = "Phuongxa")
    private String phuongxa;


    @Column(name = "Sonha")
    private String sonha;

    @Column(name = "Duong")
    private String duong;

    @Column(name = "status")
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "CustomerID")
    private Customer customer;
    
}
