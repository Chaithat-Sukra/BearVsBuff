package model.unit;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import model.unit.Unit;

public class Buffalo implements Unit {	
	public enum TypeBuff
	{
		TypeBuffBoss, TypeBuffLeft, TypeBuffRight 
	};
	
	private TypeBuff _typeBuff;
	private int _hp = 100;
	private int _damage = 10;
	private Point _point;
	
	public Buffalo(Point aCurrentPoint, TypeBuff aType) {
		this._point = aCurrentPoint;
		this._typeBuff = aType;
		
		if (this._typeBuff == TypeBuff.TypeBuffBoss)
			_hp += 50;
	}
	
	public List<Point> getMoveRange() {
		List<Point> ranges = new ArrayList<Point>();
		//for upper 2 row 
		for (int i = 0; i < 7; i++) {
			int currentX = this._point.x; 
			int currentY = this._point.y;
			if (i == currentY + 2 || i == currentY - 2) {
				Point point = new Point(currentX + 2, i);
				ranges.add(point);	
				if (this._typeBuff == TypeBuff.TypeBuffLeft && i == currentY + 2) {
					ranges.remove(point);
				}
				if (this._typeBuff == TypeBuff.TypeBuffRight && i == currentY - 2) {
					ranges.remove(point);
				}				
			}
			if (this._typeBuff == TypeBuff.TypeBuffBoss && i == currentY) {
				ranges.add(new Point(currentX + 2, i));
			}
		}
		//for upper 1 row
		for (int i = 0; i < 7; i++) {
			int currentX = this._point.x; 
			int currentY = this._point.y;
			if (i == currentY + 1 || i == currentY - 1)
				ranges.add(new Point(currentX + 1, i));			
		}
		//for lower 1 row
		for (int i = 0; i < 7; i++) {
			int currentX = this._point.x; 
			int currentY = this._point.y;
			if (i == currentY + 1 || i == currentY - 1)
				ranges.add(new Point(currentX - 1, i));			
		}
		//for lower 2 row
		for (int i = 0; i < 7; i++) {
			int currentX = this._point.x; 
			int currentY = this._point.y;
			if (i == currentY + 2 || i == currentY - 2)
				ranges.add(new Point(currentX - 2, i));
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
	public Point getCurrentPoint() {
		return _point;
	}
	public void setCurrentPoint(Point _point) {
		this._point = _point;
	}
	
	public String getImage() {
		String imageName;
		switch (this._typeBuff) {
        case TypeBuffBoss:  
        	imageName = "resource/" + "buff.png";     
        	break;
        case TypeBuffLeft:
        	imageName = "resource/" + "buff_left.png";
        	break;
        case TypeBuffRight:
        	imageName = "resource/" + "buff_right.png";
        	break;                
		default:
			imageName = "";
			break;
		}
		return imageName;
	}
		
	public String toString() {
		return "Buffalo\n" + "Type : " + this._typeBuff.name() +  " current hp : " + this._hp;
		
	}

	public TypeBuff getTypeBuff() {
		return _typeBuff;
	}

	public void setTypeBuff(TypeBuff _typeBuff) {
		this._typeBuff = _typeBuff;
	}
}