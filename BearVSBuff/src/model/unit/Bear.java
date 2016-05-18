package model.unit;

import model.unit.Unit;

import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Bear implements Unit {
	
	public enum TypeBear
	{
		TypeBearBoss, TypeBearLeft, TypeBearRight 
	};
	
	private TypeBear _typeBear;
	private ActionListener _listener;
	private int _hp = 80;
	private int _damage = 20;
	private int _rangeX = 1;
	private int _rangeY = 1;
	private Point _point;
	
	public Bear(Point aCurrentPoint, TypeBear aType) {
		this._point = aCurrentPoint;
		this._typeBear = aType;
		
		if (this._typeBear == TypeBear.TypeBearBoss)
			_hp += 50;
	}
	
	public List<Point> getMoveRange() {
		List<Point> ranges = new ArrayList<Point>();
		//for upper row
		for (int i = 0; i < 7; i++) {
			int currentX = this._point.x; 
			int currentY = this._point.y;
			if (i == currentY || i == currentY + 1 || i == currentY - 1)
				ranges.add(new Point(currentX + 1, i));
		}
		//for current row
		for (int i = 0; i < 7; i++) {
			int currentX = this._point.x; 
			int currentY = this._point.y;
			if (i == currentY + 1 || i == currentY - 1) {		
				Point point = new Point(currentX, i);
				ranges.add(point);	
				if (this._typeBear == TypeBear.TypeBearLeft && i == currentY + 1) {
					ranges.remove(point);
				}
				if (this._typeBear == TypeBear.TypeBearRight && i == currentY - 1) {
					ranges.remove(point);
				}
			}						
		}
		//for lower row
		for (int i = 0; i < 7; i++) {
			int currentX = this._point.x; 
			int currentY = this._point.y;
			if (i == currentY || i == currentY + 1 || i == currentY - 1)
				ranges.add(new Point(currentX - 1, i));
		}
		return ranges;
	}
	
	public List<Point> getAttackRange() {
		return this.getMoveRange();
	}
	
	public boolean deal(int aDamage) {
		this._hp -= aDamage;
		if (this._hp <= 0) {
			return true;
		}
		return false;
	}
	
	public int getHp() {
		return _hp;
	}
	public void setHp(int _hp) {
		this._hp = _hp;
	}
	public int getDamage() {
		return _damage;
	}
	public void setDamage(int _damage) {
		this._damage = _damage;
	}
	public int getRangeX() {
		return _rangeX;
	}
	public void setRangeX(int _rangeX) {
		this._rangeX = _rangeX;
	}
	public int getRangeY() {
		return _rangeY;
	}
	public void setRangeY(int _rangeY) {
		this._rangeY = _rangeY;
	}
	public Point getCurrentPoint() {
		return _point;
	}
	public void setCurrentPoint(Point _point) {
		this._point = _point;
	}
	public String getImage() {
		String imageName;
		switch (this._typeBear) {
        case TypeBearBoss:  
        	imageName = "resource/" + "bear.png";     
        	break;
        case TypeBearLeft:
        	imageName = "resource/" + "bear_left.png";
        	break;
        case TypeBearRight:
        	imageName = "resource/" + "bear_right.png";
        	break;                
		default:
			imageName = "";
			break;
		}
		return imageName;
	}
	public ActionListener getListener() {
		return _listener;
	}
	public void setListener(ActionListener _listener) {
		this._listener = _listener;
	}
	
	public String toString() {
		return "Bear\n" + "Type : " + this._typeBear.name() +  " current hp : " + this._hp;		
	}

	public TypeBear getTypeBear() {
		return _typeBear;
	}

	public void setTypeBear(TypeBear _typeBear) {
		this._typeBear = _typeBear;
	}
}