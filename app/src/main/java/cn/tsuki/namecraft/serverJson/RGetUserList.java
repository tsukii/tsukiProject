package cn.tsuki.namecraft.serverJson;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class RGetUserList {
	public String HeroName;
	public String RoleID;
	public int Level;
	public int HeroType;
	
	public RGetUserList(String heroName, String roleID, int level, int heroType) {
		super();
		HeroName = heroName;
		RoleID = roleID;
		Level = level;
		HeroType = heroType;
	}
	
	// Java Bean
	public String getHeroName() {
		return HeroName;
	}
	public void setHeroName(String heroName) {
		HeroName = heroName;
	}
	public String getRoleID() {
		return RoleID;
	}
	public void setRoleID(String roleID) {
		RoleID = roleID;
	}
	public int getLevel() {
		return Level;
	}
	public void setLevel(int level) {
		Level = level;
	}
	public int getHeroType() {
		return HeroType;
	}
	public void setHeroType(int heroType) {
		HeroType = heroType;
	}
	
	
	
	
	
}
