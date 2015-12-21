package cn.tsuki.namecraft.clientJson;

public class PVE {
	public int Difficulty;
	public String UserID;
	
	public int getDifficulty() {
		return Difficulty;
	}
	public void setDifficulty(int difficulty) {
		Difficulty = difficulty;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public PVE(int difficulty, String userID) {
		super();
		Difficulty = difficulty;
		UserID = userID;
	}
	
	
}
