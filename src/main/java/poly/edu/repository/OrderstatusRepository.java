package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.edu.entity.Orderstatus;

public interface OrderstatusRepository extends JpaRepository<Orderstatus, Integer>{
    
}
