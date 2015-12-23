package cn.tsuki.namecraft.serverJson;

public class RPVP {
	public int Round;
	public String AttackID;
	public String DefenseID;
	
	public int Damage;
	public boolean IsMiss;
	
	// Ability Effects
	public int AbIndex;
	public int HPA;
	public int HPD;
	public int AbDamage;
	public int AbCure;
	
	public int exp;

	public int getRound() {
		return Round;
	}

	public void setRound(int round) {
		Round = round;
	}

	public String getAttackID() {
		return AttackID;
	}

	public void setAttackID(String attackID) {
		AttackID = attackID;
	}

	public String getDefenseID() {
		return DefenseID;
	}

	public void setDefenseID(String defenseID) {
		DefenseID = defenseID;
	}

	public int getDamage() {
		return Damage;
	}

	public void setDamage(int damage) {
		Damage = damage;
	}

	public boolean getIsMiss() {
		return IsMiss;
	}

	public void setIsMiss(boolean isMiss) {
		IsMiss = isMiss;
	}

	public int getAbIndex() {
		return AbIndex;
	}

	public void setAbIndex(int abIndex) {
		AbIndex = abIndex;
	}

	public int getAbDamage() {
		return AbDamage;
	}

	public void setAbDamage(int abDamage) {
		AbDamage = abDamage;
	}

	public int getAbCure() {
		return AbCure;
	}

	public void setAbCure(int abCure) {
		AbCure = abCure;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public RPVP(int round, String attackID, String defenseID, int damage, boolean isMiss, int abIndex, int abDamage,
			int abCure, int exp) {
		super();
		Round = round;
		AttackID = attackID;
		DefenseID = defenseID;
		Damage = damage;
		IsMiss = isMiss;
		AbIndex = abIndex;
		AbDamage = abDamage;
		AbCure = abCure;
		this.exp = exp;
	}
	
	
	
}
