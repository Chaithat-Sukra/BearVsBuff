package utility.abstractFactory;

import java.awt.Point;

import model.unit.Bear;
import model.unit.Buff;
import utility.abstractFactory.BearFactory.TypeBear;
import utility.abstractFactory.BuffaloFactory.TypeBuff;
import view.BoardPanel;

public class BoardFactory extends AbstractFactory {
	public enum TypeBoard
	{
		TypeBoard7_7, TypeBoard9_9 
	};
	
	@Override
	public Bear getBear(TypeBear aType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Buff getBuffalo(TypeBuff aType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BoardPanel getBoard(TypeBoard aType) {
		if (aType == TypeBoard.TypeBoard7_7) {
			return new BoardPanel(new Point(7, 7));
		}
		else if (aType == TypeBoard.TypeBoard9_9) {
			return new BoardPanel(new Point(9, 9));
		}
		return null;
	}
}
