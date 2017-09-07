package yapp.devcamp.hairstylistserver.oauth;

public enum AuthorityType {
	
	USER("user"), // 2nd : db저장후
	STYLIST("stylist"), // 3rd : stylist db 저장후
	ADMIN("admin"); // 4th
	
	private final String ROLE_PREFIX = "ROLE_";
	private String name;
	
	AuthorityType(String name){
		this.name = name;
	}
	
	public String getRoleType(){
		return ROLE_PREFIX + this.name.toUpperCase();
	}
	
	public String getValue(){
		return this.name;
	}
	
	public boolean isEquals(String authority){
		return this.getRoleType().equals(authority);
	}
	
	public static AuthorityType getAuthorityType(String name){
		for(AuthorityType type : AuthorityType.values()){
			if(type.getRoleType().equals(name)){
				return type;
			}
		}
		throw new IllegalArgumentException(); // not existing type
	}

}
