package model.unit;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class BearBoss extends Bear {

	private static int kHp = 100;
	private static int kDamage = 40;

	private List<Bear> _arTeams;
	
	public BearBoss(Point aCurrentPoint) {
		super(aCurrentPoint);
		this.hp = kHp;
		this.damage = kDamage;
		
		this._arTeams = new ArrayList<Bear>();
	}
	
	public void addTeam(Bear aBear) {
		this._arTeams.add(aBear);
	}
	
	public List<Bear> getTeams() {
		if (!this._arTeams.contains(this)) 
			this._arTeams.add(this);
		return this._arTeams;
	}
	
	public String getImage() {
		return "resource/" + "bear.png";
	}
	
	public String toString() {
		return "Bear\n" + "Type : " + "Boss" +  " current hp : " + this.hp;		
	}
}
