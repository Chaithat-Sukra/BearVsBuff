package model.unit;

import java.awt.Point;

public class BearBoss extends Bear {

	private static int kHp = 100;
	private static int kDamage = 40;

	public BearBoss(Point aCurrentPoint) {
		super(aCurrentPoint);
		this.hp = kHp;
		this.damage = kDamage;
	}
	
	public String getImage() {
		return "resource/" + "bear.png";
	}
	
	public String toString() {
		return "Bear\n" + "Type : " + "Boss" +  " current hp : " + this.hp;		
	}
}
