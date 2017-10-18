package yapp.devcamp.hairstylistserver.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yapp.devcamp.hairstylistserver.model.ProductOption;
import yapp.devcamp.hairstylistserver.model.Shop;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Integer> {
	
	ProductOption findByOptionCode(int optionCode);
	
	List<ProductOption> findByShop(Shop shop);
}
