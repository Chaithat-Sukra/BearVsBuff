package utility;

import model.unit.Bear;
import model.unit.Buff;
import utility.BearFactory.TypeBear;
import utility.BuffaloFactory.TypeBuff;

public abstract class AbstractFactory {
	public abstract Bear getBear(TypeBear aType);
	public abstract Buff getBuffalo(TypeBuff aType);
}  

