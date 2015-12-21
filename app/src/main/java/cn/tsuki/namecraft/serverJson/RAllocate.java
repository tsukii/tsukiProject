package cn.tsuki.namecraft.serverJson;

public class RAllocate {
	public int ReturnNum;/*0失败,1正常*/

	public RAllocate(int returnNum) {
		super();
		ReturnNum = returnNum;
	}

	public int getReturnNum() {
		return ReturnNum;
	}

	public void setReturnNum(int returnNum) {
		ReturnNum = returnNum;
	}
	
	
}
