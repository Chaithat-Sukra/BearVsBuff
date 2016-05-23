package controller.system;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

import javax.swing.JOptionPane;

import view.BoardFrame;
import view.BroadPanel;
import model.board.Block;
import model.unit.*;
import utility.AbstractFactory;
import utility.BearFactory.TypeBear;
import utility.BuffaloFactory.TypeBuff;
import utility.FactoryCreator;
import utility.FactoryCreator.TypeUnit;

public class GameEngineController {

	private BroadPanel _boardPanel = new BroadPanel();
	private BoardFrame _fBoard;
	private Bear _currentBear;
	private Buff _currentBuff;
	private boolean _isTurnBear;  
	
	private ArrayList<BoardState> _previousBoardStates;
	
	int _noOfBuff = 3;
	int _noOfBear = 3;
	
	int _turnsToUndo = 3;	//As specified by the spec sheet
	
	public GameEngineController() {
		this._isTurnBear = true;
		
		_previousBoardStates = new ArrayList<BoardState>();
		
		this._fBoard = new BoardFrame();  	
		this._promptStartGame();
	}
	
	private void _promptStartGame() {
		int dialogResult = JOptionPane.showConfirmDialog (null, 
				"Would You Like to Start the Game?", 
				"", JOptionPane.YES_NO_OPTION);
		if (dialogResult == JOptionPane.YES_OPTION) {
			//create board			
			for (int x = 0; x < BroadPanel.kVERTICAL_NO; x++) {
				for (int y = 0; y < BroadPanel.kHORIZONTALL_NO; y++) {
					Point pointClick = new Point(x, y);
					this._boardPanel.setContentOfBoard(pointClick, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {	
							Block block = _boardPanel.block[pointClick.x][pointClick.y];
							if (_isTurnBear) {
								//check to show move range
								Bear bear = block.getBear();
								if (bear != null) {
									Point pointUnit = bear.getCurrentPoint();
									if (pointUnit.x == pointClick.x && pointUnit.y == pointClick.y) {
										_currentBear = bear;
										_highlightMoveRanges(true, _boardPanel);
										return;
									}	
								}
								//move unit
								if (_currentBear != null) {
									Point oldPoint = _currentBear.getCurrentPoint();
									//check not to press enemy
									if (!block.isStoreBuff) {
										//remove highlight
										_highlightMoveRanges(false, _boardPanel);
										
										//remove old block
										Block oldBlock = _boardPanel.block[oldPoint.x][oldPoint.y];
										oldBlock.remove();
										
										//set new point of unit && draw unit
										Block newBlock = _boardPanel.block[pointClick.x][pointClick.y];									
										_currentBear.setCurrentPoint(pointClick);									
										newBlock.store(_currentBear);
										
										//attack enemy in range
										_attackEnemyInRange(_boardPanel);

										_isTurnBear = false;
										
										_saveLastTurn();
									}
								}
							}
							else {
								//check to show move range
								Buff buff = block.getBuff();
								if (buff != null) {
									Point pointUnit = buff.getCurrentPoint();
									if (pointUnit.x == pointClick.x && pointUnit.y == pointClick.y) {
										_currentBuff = buff;
										_highlightMoveRanges(true, _boardPanel);
										return;
									}
								}
								//move unit
								if (_currentBuff != null) {
									Point oldPoint = _currentBuff.getCurrentPoint();
									if (!block.isStoreBear) {
										//remove highlight
										_highlightMoveRanges(false, _boardPanel);
										
										//remove old block
										Block oldBlock = _boardPanel.block[oldPoint.x][oldPoint.y];
										oldBlock.remove();
										
										//set new point of unit && draw unit
										Block newBlock = _boardPanel.block[pointClick.x][pointClick.y];									
										_currentBuff.setCurrentPoint(pointClick);									
										newBlock.store(_currentBuff);
										
										//attack enemy in range
										_attackEnemyInRange(_boardPanel);

										_isTurnBear = true;
										
										_saveLastTurn();
									}
								}
							}														
						}
					});
				}
			}
			
			this._fBoard.add(this._boardPanel);
			this._fBoard.setVisible(true);
			
			//create bear
			this._createBearTeam();		
			
			//create buffalo
			this._createBuffaloTeam();
		}
		else {
			//exit
			System.exit(0);
		}
	}
	
	private void _createBearTeam() {
		AbstractFactory unitFactory = FactoryCreator.getFactory(TypeUnit.TypeBear);
		Bear bear = null;
		
		bear = unitFactory.getBear(TypeBear.TypeBearBoss);
		Point pointBearBoss = bear.getCurrentPoint();
		Block blockBear = this._boardPanel.block[pointBearBoss.x][pointBearBoss.y];
		blockBear.store(bear);
		
		bear = unitFactory.getBear(TypeBear.TypeBearLeft);
		Point pointBearLeft = bear.getCurrentPoint();
		Block blockBearLeft = this._boardPanel.block[pointBearLeft.x][pointBearLeft.y];
		blockBearLeft.store(bear);

		bear = unitFactory.getBear(TypeBear.TypeBearRight);
		Point pointBearRight = bear.getCurrentPoint();
		Block blockBearRight = this._boardPanel.block[pointBearRight.x][pointBearRight.y];
		blockBearRight.store(bear);
	}
	
	private void _createBuffaloTeam() {
		AbstractFactory unitFactory = FactoryCreator.getFactory(TypeUnit.TypeBuff);
		Buff buff = null;
		
		buff = unitFactory.getBuffalo(TypeBuff.TypeBuffBoss);
		Point pointBuff = buff.getCurrentPoint();
		Block blockBuff = this._boardPanel.block[pointBuff.x][pointBuff.y];
		blockBuff.store(buff);
		
		buff = unitFactory.getBuffalo(TypeBuff.TypeBuffLeft);
		Point pointBuffLeft = buff.getCurrentPoint();
		Block blockBuffLeft = this._boardPanel.block[pointBuffLeft.x][pointBuffLeft.y];
		blockBuffLeft.store(buff);

		buff = unitFactory.getBuffalo(TypeBuff.TypeBuffRight);
		Point pointBuffRight = buff.getCurrentPoint();
		Block blockBuffRight = this._boardPanel.block[pointBuffRight.x][pointBuffRight.y];
		blockBuffRight.store(buff);
	}
	
	private void _highlightMoveRanges(boolean aHighlight, BroadPanel aBoardPanel) {
		List<Point> list = (this._isTurnBear)? this._currentBear.getMoveRange() : this._currentBuff.getMoveRange();
		for (Point aPoint : list) {
			if (this._isScope(aPoint)) {				
				Block block = aBoardPanel.block[aPoint.x][aPoint.y];
				block.setHighlight(aHighlight);
			}
		}
	}
	
	private void _attackEnemyInRange(BroadPanel aBoardPanel) {
		List<Point> list = (this._isTurnBear)? this._currentBear.getAttackRange() : this._currentBuff.getAttackRange();
		for (Point aPoint : list) {
			if (this._isScope(aPoint)) {
				Block block = aBoardPanel.block[aPoint.x][aPoint.y];
				if (this._isTurnBear) {
					if (block.isStoreBuff) {
						Buff buff = block.getBuff(); 
						if (buff.deal(this._currentBear.getDamage())) {
							//buffalo died;
							block.remove();
							_noOfBuff --;
							if (_noOfBuff == 0)
								JOptionPane.showConfirmDialog(null, "Bear is a winner -- Buffalo has been killed");							
						}
						System.out.println(buff.toString());
					}
				}
				else {
					if (block.isStoreBear) {
						Bear bear = block.getBear();
						if (bear.deal(this._currentBuff.getDamage())) {
							//buffalo died;
							block.remove();
							_noOfBear --;
							if (_noOfBear == 0)
								JOptionPane.showConfirmDialog(null, "Buffalo is a winner -- Bear has been killed");
						}
						System.out.println(bear.toString());	
					}
				}
			}			
		}
	}
	
	private boolean _isScope(Point aPoint) {

		
		if (aPoint.x < BroadPanel.kVERTICAL_NO && aPoint.y < BroadPanel.kHORIZONTALL_NO && aPoint.x >= 0 && aPoint.y >= 0)
			return true;
		return false;
	}

	private void _saveLastTurn(){
		BoardState currentBoardState = new BoardState(BroadPanel.kVERTICAL_NO, BroadPanel.kHORIZONTALL_NO);

		currentBoardState._isBearTurn = _isTurnBear;
		
		for (int x = 0; x < BroadPanel.kVERTICAL_NO; x++) {
			for (int y = 0; y < BroadPanel.kHORIZONTALL_NO; y++) {
				currentBoardState._boardTiles[x][y] = currentBoardState.new BoardTile();
				currentBoardState._boardTiles[x][y]._highlighted = _boardPanel.block[x][y].isHighlight;
				
				if(_boardPanel.block[x][y].isStoreBear){
					currentBoardState._boardTiles[x][y]._unitData = currentBoardState.new UnitData();
					currentBoardState._boardTiles[x][y]._unitData._isBear = true;
					currentBoardState._boardTiles[x][y]._unitData._unitType = _boardPanel.block[x][y].getBear().getClass().getSimpleName();
					currentBoardState._boardTiles[x][y]._unitData._unitHp = _boardPanel.block[x][y].getBear().getHp();
					currentBoardState._boardTiles[x][y]._unitData._unitDamage = _boardPanel.block[x][y].getBear().getDamage();
					
				}
				
				if(_boardPanel.block[x][y].isStoreBuff){
					currentBoardState._boardTiles[x][y]._unitData = currentBoardState.new UnitData();
					currentBoardState._boardTiles[x][y]._unitData._isBuff = true;
					currentBoardState._boardTiles[x][y]._unitData._unitType = _boardPanel.block[x][y].getBuff().getClass().getSimpleName();
					currentBoardState._boardTiles[x][y]._unitData._unitHp = _boardPanel.block[x][y].getBuff().getHp();
					currentBoardState._boardTiles[x][y]._unitData._unitDamage = _boardPanel.block[x][y].getBuff().getDamage();
				}
				
			}
		}
		
		_previousBoardStates.add(currentBoardState);
	}
	
	private void _restoreBoardState(BoardState stateToRestore){
		
		_isTurnBear = stateToRestore._isBearTurn;
		
		for (int x = 0; x < BroadPanel.kVERTICAL_NO; x++) {
			for (int y = 0; y < BroadPanel.kHORIZONTALL_NO; y++) {
				_boardPanel.block[x][y].remove();
				
				_boardPanel.block[x][y].isHighlight = stateToRestore._boardTiles[x][y]._highlighted;
				
				if(stateToRestore._boardTiles[x][y]._unitData != null){
					if(stateToRestore._boardTiles[x][y]._unitData._isBear){
						AbstractFactory unitFactory = FactoryCreator.getFactory(TypeUnit.TypeBear);
						Bear bear = null;
						bear = unitFactory.getBear(TypeBear.valueOf(TypeBear.class, "Type" + stateToRestore._boardTiles[x][y]._unitData._unitType));
						Block blockBear = this._boardPanel.block[x][y];
						bear.setHp( stateToRestore._boardTiles[x][y]._unitData._unitHp);
						bear.setDamage(stateToRestore._boardTiles[x][y]._unitData._unitDamage);
						bear.setCurrentPoint(new Point(x, y));
						blockBear.store(bear);
					}
					
					if(stateToRestore._boardTiles[x][y]._unitData._isBuff){
						AbstractFactory unitFactory = FactoryCreator.getFactory(TypeUnit.TypeBuff);
						Buff buff = null;
						buff = unitFactory.getBuffalo(TypeBuff.valueOf(TypeBuff.class, "Type" + stateToRestore._boardTiles[x][y]._unitData._unitType));
						Block blockBuff = this._boardPanel.block[x][y];
						buff.setHp( stateToRestore._boardTiles[x][y]._unitData._unitHp);
						buff.setDamage(stateToRestore._boardTiles[x][y]._unitData._unitDamage);
						buff.setCurrentPoint(new Point(x, y));
						blockBuff.store(buff);
					}
				}
			}
		}
	}
	
	private void _undoMoves(){
		
		//Remove the last 3 states, if there are 3 to remove. Otherwise leave 1 state.
		for(int i = 0; i < _turnsToUndo; i++){
			if(_previousBoardStates.size() > 1){
				_previousBoardStates.remove(_previousBoardStates.size()-1);
			}
		}
		
		//If there's a state in the list, then restore it.
		if(!_previousBoardStates.isEmpty()) _restoreBoardState(_previousBoardStates.get(_previousBoardStates.size()-1));
	}
	
	private void _saveBoardStateToFile()
	{
		BoardState boardState = _previousBoardStates.get(_previousBoardStates.size()-1);
		String newline = System.getProperty("line.separator");
		
		 try {
			OutputStream os = new FileOutputStream("board.txt");
			
			//Record if it was the bears turn
			os.write(Boolean.toString(_isTurnBear).getBytes());
			os.write( newline.getBytes());
	
			//Then save the rest of the board
			for (int x = 0; x < BroadPanel.kVERTICAL_NO; x++) {
				for (int y = 0; y < BroadPanel.kHORIZONTALL_NO; y++) {
					os.write( Boolean.toString(boardState._boardTiles[x][y]._highlighted).getBytes());
					os.write( newline.getBytes());
					
					
					if(boardState._boardTiles[x][y]._unitData != null) {
						os.write( "has_unit".getBytes());
						os.write( newline.getBytes());
						os.write( Integer.toString(boardState._boardTiles[x][y]._unitData._unitDamage).getBytes());
						os.write( newline.getBytes());
						os.write( Integer.toString(boardState._boardTiles[x][y]._unitData._unitHp).getBytes());
						os.write( newline.getBytes());
						os.write( Boolean.toString(boardState._boardTiles[x][y]._unitData._isBear).getBytes());
						os.write( newline.getBytes());
						os.write( Boolean.toString(boardState._boardTiles[x][y]._unitData._isBuff).getBytes());
						os.write( newline.getBytes());
						os.write( boardState._boardTiles[x][y]._unitData._unitType.getBytes());
						os.write( newline.getBytes());
					}
					else {
						os.write( "no_unit".getBytes());
						os.write( newline.getBytes());
					}
				}
			}
			
			os.close();
		 }
		 catch(IOException ioException) {
			 System.err.format("IOException: %s%n", ioException);
		 }
		 finally {
			 
		 }
	 
	}
	
	private void _loadBoardStateFromFile()
	{
		BoardState loadedState = new BoardState(BroadPanel.kVERTICAL_NO, BroadPanel.kHORIZONTALL_NO);
		
		 try {
		   BufferedReader is = new BufferedReader(new FileReader ("board.txt"));
		   
		   String line;
		   
		   line = is.readLine();
			   
		   boolean wasBearTurn = Boolean.parseBoolean(line);
		   loadedState._isBearTurn = wasBearTurn;
		   
			for (int x = 0; x < BroadPanel.kVERTICAL_NO; x++) {
				for (int y = 0; y < BroadPanel.kHORIZONTALL_NO; y++) {
					 line = is.readLine();
					 boolean wasHighlighted = Boolean.parseBoolean(line);
					 loadedState._boardTiles[x][y]._highlighted = wasHighlighted;
					 
					 line = is.readLine();
					 if(line.equals("has_unit"))
					 {
						 line = is.readLine();
						 loadedState._boardTiles[x][y]._unitData = loadedState.new UnitData();
						 loadedState._boardTiles[x][y]._unitData._unitDamage = Integer.parseInt(line);
						 line = is.readLine();
						 loadedState._boardTiles[x][y]._unitData._unitHp  = Integer.parseInt(line);
						 line = is.readLine();
						 loadedState._boardTiles[x][y]._unitData._isBear = Boolean.parseBoolean(line);
						 line = is.readLine();
						 loadedState._boardTiles[x][y]._unitData._isBuff = Boolean.parseBoolean(line);
						 line = is.readLine();
						 loadedState._boardTiles[x][y]._unitData._unitType = line;
					 }
				}
			}
			
			is.close();

		 }
		 catch(IOException ioException) {
			 System.err.format("IOException: %s%n", ioException);
		 }
		 finally {
			 
		 }
		 
		 _restoreBoardState(loadedState);
	}
}

