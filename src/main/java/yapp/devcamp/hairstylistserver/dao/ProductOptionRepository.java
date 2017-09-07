package yapp.devcamp.hairstylistserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yapp.devcamp.hairstylistserver.model.ProductOption;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Integer> {
	
}
