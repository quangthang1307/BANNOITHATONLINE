// package poly.edu.entity;



// import java.time.LocalDateTime;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @NoArgsConstructor
// @AllArgsConstructor
// @Data
// @Entity
// @Table(name = "Transactions")
// public class Transaction {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     @Column(name = "TransactiID")
//     private Integer transaction;

//     @Column(name = "Amount")
//     private Integer amount;


//     @Column(name = "Status")
//     private String status;


//     @Column(name = "Date")
//     private LocalDateTime date;

//     @Column(name = "Message")
//     private String message;

//     @Column(name = "Bank")
//     private String bank;

//     @ManyToOne
//     @JoinColumn(name = "OrderID")
//     private Order order;
    
// }
