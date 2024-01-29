package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.edu.entity.Brands;

public interface BrandRepository extends JpaRepository<Brands, Integer> {

}
