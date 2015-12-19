package cn.tsuki.namecraft.clientJson;

public class PVE {
	public String Difficulty;
	public String UserID;
	
	public String getDifficulty() {
		return Difficulty;
	}
	public void setDifficulty(String difficulty) {
		Difficulty = difficulty;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public PVE(String difficulty, String userID) {
		super();
		Difficulty = difficulty;
		UserID = userID;
	}
	
	
}
