package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import poly.edu.entity.Telegram;
import poly.edu.entity.Telegram.MissionType;

@Repository
public interface TelegramRepository extends JpaRepository <Telegram, Integer> {
    Telegram findByMission(Telegram.MissionType mission);

    Telegram findByBotToken(String botToken);
    
}
