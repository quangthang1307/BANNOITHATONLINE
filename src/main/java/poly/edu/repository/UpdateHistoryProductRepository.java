package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.edu.entity.UpdateHistoryProduct;


public interface UpdateHistoryProductRepository extends JpaRepository<UpdateHistoryProduct, Integer>{
    
}
