package model.unit;

import java.awt.Point;
import java.util.List;

public class BuffBoss extends Buff {

	private static int kHp = 100;
	private static int kDamage = 50;

	public BuffBoss(Point aPoint) {
		super(aPoint);
		this.hp = kHp;
		this.damage = kDamage;
	}

	@Override
	public List<Point> getMoveRange() {
		List<Point> ranges = super.getMoveRange();
		int currentX = this.point.x; 
		int currentY = this.point.y;

		ranges.add(new Point(currentX + 2, currentY));
		return ranges;
	}
	
	public String getImage() {
		return "resource/" + "buff.png";
	}
		
	public String toString() {
		return "Buffalo\n" + "Type : " + "Boss" +  " current hp : " + this.hp;
		
	}
}
