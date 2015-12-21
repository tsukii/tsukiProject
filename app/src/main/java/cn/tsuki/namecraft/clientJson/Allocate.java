package cn.tsuki.namecraft.clientJson;

public class Allocate {
	public String RoleID;

	public int Attotal;//天赋点数
	public int Power;
	public int Intel;
	public int Agi;
	public int Lucky;

	public int Abtotal;//技能点数
	public int AbilityLevel1;
	public int AbilityLevel2;
	public int AbilityLevel3;
	public int AbilityLevel4;
	public int AbilityLevel5;
	public int AbilityLevel6;
	{
		Attotal = 0;
		Power = 0;
		Intel = 0;
		Agi = 0;
		Lucky = 0;

		Abtotal = 0;
		AbilityLevel1 = 0;
		AbilityLevel2 = 0;
		AbilityLevel3 = 0;
		AbilityLevel4 = 0;
		AbilityLevel5 = 0;
		AbilityLevel6 = 0;
	}
	//

	public Allocate(String roleid,int attotal, int power, int intel, int agi, int lucky) {
		super();
		RoleID = roleid;
		Attotal = attotal;
		Power = power;
		Intel = intel;
		Agi = agi;
		Lucky = lucky;
	}



	public Allocate(String roleid,int attotal, int power, int intel, int agi, int lucky, int abtotal, int abilityLevel1,
					int abilityLevel2, int abilityLevel3, int abilityLevel4, int abilityLevel5, int abilityLevel6) {
		super();
		RoleID = roleid;

		Attotal = attotal;
		Power = power;
		Intel = intel;
		Agi = agi;
		Lucky = lucky;
		Abtotal = abtotal;
		AbilityLevel1 = abilityLevel1;
		AbilityLevel2 = abilityLevel2;
		AbilityLevel3 = abilityLevel3;
		AbilityLevel4 = abilityLevel4;
		AbilityLevel5 = abilityLevel5;
		AbilityLevel6 = abilityLevel6;
	}

	public Allocate(String roleID, int abtotal, int abilityLevel1, int abilityLevel2, int abilityLevel3, int abilityLevel4,
					int abilityLevel5, int abilityLevel6) {
		super();
		RoleID = roleID;
		Abtotal = abtotal;
		AbilityLevel1 = abilityLevel1;
		AbilityLevel2 = abilityLevel2;
		AbilityLevel3 = abilityLevel3;
		AbilityLevel4 = abilityLevel4;
		AbilityLevel5 = abilityLevel5;
		AbilityLevel6 = abilityLevel6;
	}



	public String getRoleID() {
		return RoleID;
	}






	public void setRoleID(String roleID) {
		RoleID = roleID;
	}



	// Java Bean
	public int getAttotal() {
		return Attotal;
	}

	public void setAttotal(int attotal) {
		Attotal = attotal;
	}

	public int getPower() {
		return Power;
	}

	public void setPower(int power) {
		Power = power;
	}

	public int getIntel() {
		return Intel;
	}

	public void setIntel(int intel) {
		Intel = intel;
	}

	public int getAgi() {
		return Agi;
	}

	public void setAgi(int agi) {
		Agi = agi;
	}

	public int getLucky() {
		return Lucky;
	}

	public void setLucky(int lucky) {
		Lucky = lucky;
	}

	public int getAbtotal() {
		return Abtotal;
	}

	public void setAbtotal(int abtotal) {
		Abtotal = abtotal;
	}



	public int getAbilityLevel1() {
		return AbilityLevel1;
	}



	public void setAbilityLevel1(int abilityLevel1) {
		AbilityLevel1 = abilityLevel1;
	}



	public int getAbilityLevel2() {
		return AbilityLevel2;
	}



	public void setAbilityLevel2(int abilityLevel2) {
		AbilityLevel2 = abilityLevel2;
	}



	public int getAbilityLevel3() {
		return AbilityLevel3;
	}



	public void setAbilityLevel3(int abilityLevel3) {
		AbilityLevel3 = abilityLevel3;
	}



	public int getAbilityLevel4() {
		return AbilityLevel4;
	}



	public void setAbilityLevel4(int abilityLevel4) {
		AbilityLevel4 = abilityLevel4;
	}



	public int getAbilityLevel5() {
		return AbilityLevel5;
	}



	public void setAbilityLevel5(int abilityLevel5) {
		AbilityLevel5 = abilityLevel5;
	}



	public int getAbilityLevel6() {
		return AbilityLevel6;
	}



	public void setAbilityLevel6(int abilityLevel6) {
		AbilityLevel6 = abilityLevel6;
	}




}
