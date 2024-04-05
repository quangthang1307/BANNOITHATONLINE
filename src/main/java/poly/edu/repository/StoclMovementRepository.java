package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.edu.entity.StoclMovement;

public interface StoclMovementRepository extends JpaRepository<StoclMovement, Integer> {
    
}
