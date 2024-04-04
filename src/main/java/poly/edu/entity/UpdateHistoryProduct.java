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
@Table(name = "Updatehistoryproduct")
public class UpdateHistoryProduct{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UpdateID")
    private Integer UpdateId;

    @Column(name = "EmployeeID")
    private int employeeID;

    @Column(name = "ProductID")
    private int productID;

    @Column(name = "Updatedate")
    private LocalDateTime updatedate;
}
