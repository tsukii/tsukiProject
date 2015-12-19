package cn.tsuki.namecraft.clientJson;

public class GetUserList {
	public String UserID;

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public GetUserList(String userID) {
		super();
		UserID = userID;
	}
	
}
