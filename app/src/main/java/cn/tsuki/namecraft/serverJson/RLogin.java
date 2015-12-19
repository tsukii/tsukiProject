package cn.tsuki.namecraft.serverJson;

public class RLogin {
	public int ReturnNum;/*0,1*/
	public String UserId;
	public String UserPassword;
	
	public int getReturnNum() {
		return ReturnNum;
	}
	public void setReturnNum(int returnNum) {
		ReturnNum = returnNum;
	}
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public String getUserPassword() {
		return UserPassword;
	}
	public void setUserPassword(String userPassword) {
		UserPassword = userPassword;
	}
	public RLogin(int returnNum, String userId, String userPassword) {
		super();
		ReturnNum = returnNum;
		UserId = userId;
		UserPassword = userPassword;
	}
	
	
	
}
