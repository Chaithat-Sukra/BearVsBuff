package utility.observer;

import controller.system.GameEngineController;

public abstract class Observer {
	protected GameEngineController subject;
	
	public abstract void setSubject(GameEngineController aSubject);
	public abstract void updateHighlight(boolean aHighlight);
	public abstract boolean dealDamage();
}
