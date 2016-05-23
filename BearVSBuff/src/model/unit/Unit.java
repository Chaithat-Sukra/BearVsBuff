package model.unit;

import java.awt.Point;
import java.util.List;

public interface Unit {	
	public abstract List<Point> getMoveRange();
	public abstract List<Point> getAttackRange();
	public abstract boolean deal(int aDamage);
	public abstract int getHp();
	public abstract int getDamage();
	public abstract void setHp(int hp);
	public abstract void setDamage(int damage);
	public abstract Point getCurrentPoint();
	public abstract void setCurrentPoint(Point aPoint);
	public abstract String getImage();
	public abstract String toString();
}
