package utility.abstractFactory;

import model.unit.Bear;
import model.unit.Buff;
import utility.abstractFactory.BearFactory.TypeBear;
import utility.abstractFactory.BuffaloFactory.TypeBuff;

public abstract class AbstractFactory {
	public abstract Bear getBear(TypeBear aType);
	public abstract Buff getBuffalo(TypeBuff aType);
}  

