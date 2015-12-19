package cn.tsuki.namecraft.clientJson;

public class CreateHero {
	public String HeroName;
	public String UserID;
	public int HeroType; /*1.力量 2.敏捷 3.智力*/
	public CreateHero(String heroName, String userID, int heroType) {
		super();
		HeroName = heroName;
		UserID = userID;
		HeroType = heroType;
	}
	public String getHeroName() {
		return HeroName;
	}
	public void setHeroName(String heroName) {
		HeroName = heroName;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public int getHeroType() {
		return HeroType;
	}
	public void setHeroType(int heroType) {
		HeroType = heroType;
	}
	
	
}
