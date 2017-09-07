package yapp.devcamp.hairstylistserver.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yapp.devcamp.hairstylistserver.model.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Integer> {
	Shop findByShopName(String shopName);
	
	Shop findByShopCode(int shopCode);
}
