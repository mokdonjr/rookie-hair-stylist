package yapp.devcamp.hairstylistserver.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yapp.devcamp.hairstylistserver.model.Product;
import yapp.devcamp.hairstylistserver.model.Shop;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	Product findByProductCode(int productCode);
	
	List<Product> findByShop(Shop shop);
}
