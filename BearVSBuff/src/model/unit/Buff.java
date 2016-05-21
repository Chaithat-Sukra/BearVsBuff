package model.unit;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Buff implements Unit {
	protected int hp;
	protected int damage;
	protected Point point;

	public Buff(Point aPoint) {
		this.point = aPoint;
	}

	@Override
	public List<Point> getMoveRange() {
		List<Point> ranges = new ArrayList<Point>();
		int currentX = this.point.x; 
		int currentY = this.point.y;

		//for upper 2 row 
		for (int i = 0; i < 7; i++) {
			if (i == currentY + 2 || i == currentY - 2) {
				Point point = new Point(currentX + 2, i);
				ranges.add(point);	
			}
		}
		//for upper 1 row
		for (int i = 0; i < 7; i++) {
			if (i == currentY + 1 || i == currentY - 1)
				ranges.add(new Point(currentX + 1, i));			
		}
		//for lower 1 row
		for (int i = 0; i < 7; i++) {
			if (i == currentY + 1 || i == currentY - 1)
				ranges.add(new Point(currentX - 1, i));			
		}
		//for lower 2 row
		for (int i = 0; i < 7; i++) {
			if (i == currentY + 2 || i == currentY - 2)
				ranges.add(new Point(currentX - 2, i));
		}
		return ranges;
	}

	@Override
	public List<Point> getAttackRange() {
		return this.getMoveRange();
	}

	@Override
	public boolean deal(int aDamage) {
		this.hp -= aDamage;
		if (this.hp <= 0) {
			return true;
		}
		return false;
	}

	@Override
	public int getHp() {
		return this.hp;
	}

	@Override
	public int getDamage() {
		return this.damage;
	}

	@Override
	public Point getCurrentPoint() {
		return this.point;
	}

	@Override
	public void setCurrentPoint(Point aPoint) {
		this.point = aPoint;
	}

	@Override
	public String getImage() {
		return null;
	}

	@Override
	public String toString() {
		return null;
	}
}
