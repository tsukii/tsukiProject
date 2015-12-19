package cn.tsuki.namecraft.clientJson;

public class Allocate {
	public int Attotal;//天赋点数

	public int Power;
	public int Intel;
	public int Agi;
	public int Lucky;

	public int Abtotal;//技能点数
	public String AbilityLevel; //Json数组形式

	{
		Attotal = 0;
		Power = 0;
		Intel = 0;
		Agi = 0;
		Lucky = 0;

		Abtotal = 0;
	}
	//

	public Allocate(int attotal, int power, int intel, int agi, int lucky) {
		super();
		Attotal = attotal;
		Power = power;
		Intel = intel;
		Agi = agi;
		Lucky = lucky;
	}

	public Allocate(int abtotal, String abilityLevel) {
		super();
		Abtotal = abtotal;
		AbilityLevel = abilityLevel;
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

	public String getAbilityLevel() {
		return AbilityLevel;
	}

	public void setAbilityLevel(String abilityLevel) {
		AbilityLevel = abilityLevel;
	}




}