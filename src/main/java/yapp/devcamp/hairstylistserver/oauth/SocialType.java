package yapp.devcamp.hairstylistserver.oauth;

public enum SocialType {
	FACEBOOK("facebook"),
	KAKAO("kakao");
	
	private final String ROLE_PREFIX = "ROLE_";
	private String name;
	
	SocialType(String name){
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
	
	public static SocialType getSocialType(String name){
		for(SocialType type : SocialType.values()){
			if(type.getRoleType().equals(name)){
				return type;
			}
		}
		throw new IllegalArgumentException(); // not existing type
	}
}
