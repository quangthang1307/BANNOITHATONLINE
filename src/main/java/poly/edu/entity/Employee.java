package poly.edu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmployeeID")
    private Integer employeeID;

    @NotBlank(message = "Tên nhân viên không được để trống")
    @Column(name = "Name")
    private String name;

    @NotBlank(message = "Số điện thoại nhân viên không được để trống")
    @Column(name = "Phone")
    private String phone;

    @Column(name = "Address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "AccountID")
    private Account account;
}

