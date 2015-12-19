package cn.tsuki.namecraft.clientJson;

public class Login {
	public int FirstLogin; /*0,1*/
	public String UserId;
	public String UserPassword;
	
	
	public Login(int firstLogin, String userId, String userPassword) {
		super();
		FirstLogin = firstLogin;
		UserId = userId;
		UserPassword = userPassword;
	}
	
	public Login(){
		UserId = null;
		UserPassword = null;
	}
	
	public int getFirstLogin() {
		return FirstLogin;
	}
	public void setFirstLogin(int firstLogin) {
		FirstLogin = firstLogin;
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
	
	
}
