package poly.edu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "Telegram")
public class Telegram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    Integer ID;

    @Column(name = "bottoken")
    String botToken;

    @Column(name = "chatid")
    String chatId;

    @Enumerated(EnumType.STRING)
    @Column(name = "mission")
    MissionType mission;

    @Column(name = "time")
    String time;

    public enum MissionType {
        DATHANG,
        HUYHANG,
        THONGKE
    }
}
