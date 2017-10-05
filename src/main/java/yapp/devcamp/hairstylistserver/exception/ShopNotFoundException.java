package yapp.devcamp.hairstylistserver.exception;

public class ShopNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -2501672070488575576L;
	
	private int shopCode;
	
	public ShopNotFoundException(int shopCode){
		this.shopCode = shopCode;
	}
	
	public int getShopCode(){
		return this.shopCode;
	}

}
