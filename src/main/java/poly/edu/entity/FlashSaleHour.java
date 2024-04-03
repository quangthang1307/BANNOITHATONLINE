package poly.edu.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Flashsalehour")
public class FlashSaleHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer ID;

    @Column(name = "Starthour")
    private LocalTime hourStart;

    @Column(name = "Endhour")
    private LocalTime hourEnd;

    @Column(name = "Status")
    private Boolean status;

    @Column(name = "Startdate")
    private LocalDate dateStart;

    @Column(name = "Frequency")
    private Integer Frequency;

    @Column(name = "Frequencyfor")
    private String FrequencyFor;
}
