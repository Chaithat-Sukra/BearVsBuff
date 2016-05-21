package model.unit;

import java.awt.Point;
import java.util.List;

public class BuffLeft extends Buff {

	private static int kHp = 70;
	private static int kDamage = 20;

	public BuffLeft(Point aPoint) {
		super(aPoint);
		
		this.hp = kHp;
		this.damage = kDamage;
	}
	
	public List<Point> getMoveRange() {
		List<Point> ranges = super.getMoveRange();
		int currentX = this.point.x; 
		int currentY = this.point.y;

		ranges.remove(new Point(currentX + 2, currentY + 2));
		return ranges;
	}
	
	public String getImage() {
		return "resource/" + "buff_left.png";
	}
}
