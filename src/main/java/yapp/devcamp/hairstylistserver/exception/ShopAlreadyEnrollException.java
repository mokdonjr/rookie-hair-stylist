package yapp.devcamp.hairstylistserver.exception;

public class ShopAlreadyEnrollException extends RuntimeException {
	private static final long serialVersionUID = 8223138826701471568L;

	private String shopName;
	
	public ShopAlreadyEnrollException(String shopName){
		this.shopName = shopName;
	}
	
	public String getShopName(){
		return this.shopName;
	}
}
