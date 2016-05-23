package utility;

public class FactoryCreator {
	public enum TypeUnit
	{
		TypeBear, TypeBuff 
	};
	
	public static AbstractFactory getFactory(TypeUnit aType) {
		if (aType == TypeUnit.TypeBear) {
			return new BearFactory();
		}
		else if (aType == TypeUnit.TypeBuff) {
			return new BuffaloFactory();
		}
		return null;
	}
}
