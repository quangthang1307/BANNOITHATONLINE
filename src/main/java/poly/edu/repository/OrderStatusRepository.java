package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.edu.entity.Orderstatus;

public interface OrderStatusRepository extends JpaRepository<Orderstatus, Integer> {

    @Query(value="select * from Orderstatus where Orderstatusname = ?1", nativeQuery = true)
    Orderstatus findByOrderStatusName(String statusName);
}
