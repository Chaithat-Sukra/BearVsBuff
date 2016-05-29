package utility.abstractFactory;

import java.awt.Point;

import model.unit.*;
import utility.abstractFactory.BearFactory.TypeBear;

public class BuffaloFactory extends AbstractFactory {
	public enum TypeBuff
	{
		TypeBuffBoss, TypeBuffLeft, TypeBuffRight 
	};
	
	@Override
	public Buff getBuffalo(TypeBuff aType) {
		Buff buff = null;
		if (aType == TypeBuff.TypeBuffBoss) {
			buff = new BuffBoss(new Point(0, 3));
		}
		else if (aType == TypeBuff.TypeBuffLeft) {
			buff = new BuffLeft(new Point(0, 2));
		}
		else if (aType == TypeBuff.TypeBuffRight) {
			buff = new BuffRight(new Point(0, 4));
		}
		return buff;
	}
	
	@Override
	public Bear getBear(TypeBear aType) {
		return null;
	}
}
