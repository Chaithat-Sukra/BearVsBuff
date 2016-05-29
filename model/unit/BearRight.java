package model.unit;

import java.awt.Point;
import java.util.List;

public class BearRight extends Bear {

	private static int kHp = 50;
	private static int kDamage = 20;

	public BearRight(Point aCurrentPoint) {
		super(aCurrentPoint);
		this.hp = kHp;
		this.damage = kDamage;
	}

	public List<Point> getMoveRange() {
		List<Point> ranges = super.getMoveRange();
		int currentX = this.point.x; 
		int currentY = this.point.y;

		ranges.remove(new Point(currentX, currentY - 1));
		return ranges;
	}
	
	public String getImage() {
		return "resource/" + "bear_right.png";
	}
	
	public String toString() {
		return "Bear\n" + "Type : " + "Right" +  " current hp : " + this.hp;		
	}
}
