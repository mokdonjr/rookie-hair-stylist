package yapp.devcamp.hairstylistserver.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import yapp.devcamp.hairstylistserver.model.Shop;
import yapp.devcamp.hairstylistserver.model.Stylist;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Integer> {
	Shop findByShopName(String shopName);
	
	Shop findByShopCode(int shopCode);
	
	@Query(value="select u from Shop u order by shop_date desc")
	List<Shop> orderByshopDate();
	
	List<Shop> findByStylist(Stylist stylist);
}
