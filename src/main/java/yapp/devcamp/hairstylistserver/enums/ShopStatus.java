package yapp.devcamp.hairstylistserver.enums;


public enum ShopStatus {
	OPENED("opened"),
	CLOSED("closed");
	
	private String status;
	
	ShopStatus(String status){
		this.status = status;
	}
	
	public String getValue(){
		return this.status;
	}
	
	public static ShopStatus getShopStatus(String name){
		for(ShopStatus status : ShopStatus.values()){
			if(status.getValue().equals(name)){
				return status;
			}
		}
		throw new IllegalArgumentException(); // not existing status
	}
}
