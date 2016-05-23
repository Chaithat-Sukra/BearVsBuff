package model.unit;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class BuffBoss extends Buff {

	private static int kHp = 100;
	private static int kDamage = 50;
	
	private List<Buff> _arTeams; 

	public BuffBoss(Point aPoint) {
		super(aPoint);
		this.hp = kHp;
		this.damage = kDamage;
		this._arTeams = new ArrayList<Buff>();
	}
	
	public void addTeam(Buff aBuff) {
		this._arTeams.add(aBuff);
	}

	public List<Buff> getTeams() {
		if (!this._arTeams.contains(this)) 
			this._arTeams.add(this);
		return this._arTeams;
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
