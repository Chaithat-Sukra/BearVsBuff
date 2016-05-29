package controller.system;

import java.awt.Point;

public class BoardState {

	public class UnitData {
		String _unitType;
		int _unitDamage;
		int _unitHp;
		boolean _isBear;
		boolean _isBuff;
	}
	
	public class BoardTile {
		UnitData _unitData;
		boolean _highlighted;	
	}

	public BoardState(int boardSizeX, int boardSizeY){
		_boardTiles = new BoardTile[boardSizeX][boardSizeY];
		
		for(int x = 0; x < boardSizeX; x++){
			for(int y = 0; y < boardSizeY; y++){
				_boardTiles[x][y] = new BoardTile();
			}
		}
		
	}
	
	
	boolean _isBearTurn;
	BoardTile[][] _boardTiles;

}
