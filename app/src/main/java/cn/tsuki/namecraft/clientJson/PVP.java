package cn.tsuki.namecraft.clientJson;

public class PVP {
	public String UserID;
	public String EnemyID;
	
	public PVP(String userID, String enemyID) {
		super();
		UserID = userID;
		EnemyID = enemyID;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getEnemyID() {
		return EnemyID;
	}

	public void setEnemyID(String enemyID) {
		EnemyID = enemyID;
	}
	
	
	
}
