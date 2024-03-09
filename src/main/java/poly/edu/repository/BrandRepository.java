package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import poly.edu.entity.Brands;

@Repository
public interface BrandRepository extends JpaRepository<Brands, Integer> {

	@Query("SELECT COUNT(c) FROM Brands c WHERE LOWER(c.brandname) = LOWER(:brandname)")
	  int existsByNameIgnoreCase(@Param("brandname") String brandname);
	
}
