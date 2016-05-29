package controller.system;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.*;

import javax.swing.JOptionPane;

import controller.listener.BoardActionListener;
import controller.listener.FrameActionListener;
import view.BoardFrame;
import view.BoardPanel;
import model.board.Block;
import model.unit.*;
import utility.abstractFactory.AbstractFactory;
import utility.abstractFactory.FactoryCreator;
import utility.abstractFactory.BearFactory.TypeBear;
import utility.abstractFactory.BuffaloFactory.TypeBuff;
import utility.abstractFactory.FactoryCreator.TypeUnit;
import utility.observer.Observer;

public class GameEngineController {

	public BoardPanel boardPanel = new BoardPanel();
	public boolean isTurnBear;
	
	private BearBoss _bossBear;
	private BuffBoss _bossBuff;
	public Bear currentBear;
	public Buff currentBuff;
	
	public BoardFrame _fBoard;	
	
	private ArrayList<BoardState> _previousBoardStates;
	
	int _noOfBuff = 3;
	int _noOfBear = 3;
	
	private List<Observer> _observers = new ArrayList<Observer>();
	int _turnsToUndo = 3;	//As specified by the spec sheet
	
	public GameEngineController() {
		this.isTurnBear = true;
		
		_previousBoardStates = new ArrayList<BoardState>();
		this._fBoard = new BoardFrame(new FrameActionListener(this));  	
		this._promptStartGame();
	}
	
	public void highlightMoveRanges(boolean aHighlight) {		
		/*
		List<Point> list = (this.isTurnBear)? this.currentBear.getMoveRange() : this.currentBuff.getMoveRange();
		for (Point aPoint : list) {
			if (this._isScope(aPoint)) {		
				Block block = this.boardPanel.block[aPoint.x][aPoint.y];
				block.setHighlight(aHighlight);
			}
		}
		/*/
		this._notifyHighlightObservers(aHighlight);
		//*/
	}
	
	public void attackEnemyInRange() {
		//*
		this._notifyDealObservers();
		if (this.isTurnBear) {
			for (Bear bear : this._bossBear.getTeams()) {
				System.out.println(bear.toString());	
			}			
		}
		else {
			for (Buff buff : this._bossBuff.getTeams()) {
				System.out.println(buff.toString());	
			}
		}
		/*/
		List<Point> list = (this.isTurnBear)? this.currentBear.getAttackRange() : this.currentBuff.getAttackRange();
		for (Point aPoint : list) {
			if (this.isScope(aPoint)) {
				Block block = this.boardPanel.block[aPoint.x][aPoint.y];
				if (this.isTurnBear) {
					if (block.isStoreBuff) {
						Buff buff = block.getBuff(); 
						if (buff.deal(this.currentBear.getDamage())) {
							//buffalo died;
							block.remove();
							_noOfBuff --;
							if (_noOfBuff == 0)
								JOptionPane.showConfirmDialog(null, "Bear is a winner -- Buffalo is been killed");							
						}
						
						for (Bear bear : this._bossBear.getTeams()) {
							System.out.println(bear.toString());	
						}
					}
				}
				else {
					if (block.isStoreBear) {
						Bear bear = block.getBear();
						if (bear.deal(this.currentBuff.getDamage())) {
							//buffalo died;
							block.remove();
							_noOfBear --;
							if (_noOfBear == 0)
								JOptionPane.showConfirmDialog(null, "Buffalo is a winner -- Bear is been killed");
						}
						
						for (Buff buff : this._bossBuff.getTeams()) {
							System.out.println(buff.toString());	
						}
					}
				}
			}			
		}
		//*/
	}
	
	public boolean isScope(Point aPoint) {
		if (aPoint.x < BoardPanel.kVERTICAL_NO && aPoint.y < BoardPanel.kHORIZONTALL_NO && aPoint.x >= 0 && aPoint.y >= 0)
			return true;
		return false;
	}
	
	public void attachObserver(Observer aObserver) {
		this._observers.add(aObserver);
	}
	
	public void clearObserver() {
		this._observers.clear();
	}
	
	public void saveLastTurn(){
		BoardState currentBoardState = new BoardState(BoardPanel.kVERTICAL_NO, BoardPanel.kHORIZONTALL_NO);

		currentBoardState._isBearTurn = isTurnBear;
		
		for (int x = 0; x < BoardPanel.kVERTICAL_NO; x++) {
			for (int y = 0; y < BoardPanel.kHORIZONTALL_NO; y++) {
				currentBoardState._boardTiles[x][y] = currentBoardState.new BoardTile();
				currentBoardState._boardTiles[x][y]._highlighted = boardPanel.block[x][y].isHighlight;
				
				if(boardPanel.block[x][y].isStoreBear){
					currentBoardState._boardTiles[x][y]._unitData = currentBoardState.new UnitData();
					currentBoardState._boardTiles[x][y]._unitData._isBear = true;
					currentBoardState._boardTiles[x][y]._unitData._unitType = boardPanel.block[x][y].getBear().getClass().getSimpleName();
					currentBoardState._boardTiles[x][y]._unitData._unitHp = boardPanel.block[x][y].getBear().getHp();
					currentBoardState._boardTiles[x][y]._unitData._unitDamage = boardPanel.block[x][y].getBear().getDamage();
					
				}
				
				if(boardPanel.block[x][y].isStoreBuff){
					currentBoardState._boardTiles[x][y]._unitData = currentBoardState.new UnitData();
					currentBoardState._boardTiles[x][y]._unitData._isBuff = true;
					currentBoardState._boardTiles[x][y]._unitData._unitType = boardPanel.block[x][y].getBuff().getClass().getSimpleName();
					currentBoardState._boardTiles[x][y]._unitData._unitHp = boardPanel.block[x][y].getBuff().getHp();
					currentBoardState._boardTiles[x][y]._unitData._unitDamage = boardPanel.block[x][y].getBuff().getDamage();
				}
				
			}
		}
		
		_previousBoardStates.add(currentBoardState);
	}
	
	private void _notifyHighlightObservers(boolean aHightlight) {
		for (Observer observer : this._observers) {
	         observer.updateHighlight(aHightlight);
	    }
	}
	
	private void _notifyDealObservers() {
		for (Observer observer : this._observers) {
	         if (observer.dealDamage()) {
	        	 Block block = (Block)observer;
	        	 block.remove();
	        	 if (this.isTurnBear) {
		        	 _noOfBuff --;
		        	 if (_noOfBuff == 0)
							JOptionPane.showConfirmDialog(null, "Bear team is a winner -- Buffaloes are all killed");
	        	 }
	        	 else {
	        		 _noOfBear --;
						if (_noOfBear == 0)
							JOptionPane.showConfirmDialog(null, "Buffalo team is a winner -- Bears are all killed");
	        	 }	        	 
	         }
		}
	}
	
	private void _promptStartGame() {
		int dialogResult = JOptionPane.showConfirmDialog (null, 
				"Would You Like to Start the Game?", 
				"", JOptionPane.YES_NO_OPTION);
		if (dialogResult == JOptionPane.YES_OPTION) {
			//create board			
			for (int x = 0; x < BoardPanel.kVERTICAL_NO; x++) {
				for (int y = 0; y < BoardPanel.kHORIZONTALL_NO; y++) {
					Point pointClick = new Point(x, y);
					this.boardPanel.setContentOfBoard(pointClick, new BoardActionListener(this, pointClick));
				}
			}
			
			this._fBoard.add(this.boardPanel);
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
		
		this._bossBear = (BearBoss) unitFactory.getBear(TypeBear.TypeBearBoss);
		Point pointBearBoss = this._bossBear.getCurrentPoint();
		Block blockBear = this.boardPanel.block[pointBearBoss.x][pointBearBoss.y];
		blockBear.store(this._bossBear);
		
		bear = unitFactory.getBear(TypeBear.TypeBearLeft);
		Point pointBearLeft = bear.getCurrentPoint();
		Block blockBearLeft = this.boardPanel.block[pointBearLeft.x][pointBearLeft.y];
		blockBearLeft.store(bear);
		this._bossBear.addTeam(bear);
		
		bear = unitFactory.getBear(TypeBear.TypeBearRight);
		Point pointBearRight = bear.getCurrentPoint();
		Block blockBearRight = this.boardPanel.block[pointBearRight.x][pointBearRight.y];
		blockBearRight.store(bear);
		this._bossBear.addTeam(bear);
	}
	
	private void _createBuffaloTeam() {
		AbstractFactory unitFactory = FactoryCreator.getFactory(TypeUnit.TypeBuff);
		Buff buff = null;
		
		this._bossBuff = (BuffBoss) unitFactory.getBuffalo(TypeBuff.TypeBuffBoss);
		Point pointBuff = this._bossBuff.getCurrentPoint();
		Block blockBuff = this.boardPanel.block[pointBuff.x][pointBuff.y];
		blockBuff.store(this._bossBuff);
		
		buff = unitFactory.getBuffalo(TypeBuff.TypeBuffLeft);
		Point pointBuffLeft = buff.getCurrentPoint();
		Block blockBuffLeft = this.boardPanel.block[pointBuffLeft.x][pointBuffLeft.y];
		blockBuffLeft.store(buff);
		this._bossBuff.addTeam(buff);
		
		buff = unitFactory.getBuffalo(TypeBuff.TypeBuffRight);
		Point pointBuffRight = buff.getCurrentPoint();
		Block blockBuffRight = this.boardPanel.block[pointBuffRight.x][pointBuffRight.y];
		blockBuffRight.store(buff);
		this._bossBuff.addTeam(buff);
	}
	
	private void _restoreBoardState(BoardState stateToRestore){
		
		isTurnBear = stateToRestore._isBearTurn;
		
		for (int x = 0; x < BoardPanel.kVERTICAL_NO; x++) {
			for (int y = 0; y < BoardPanel.kHORIZONTALL_NO; y++) {
				boardPanel.block[x][y].remove();
				
				boardPanel.block[x][y].isHighlight = stateToRestore._boardTiles[x][y]._highlighted;
				
				if(stateToRestore._boardTiles[x][y]._unitData != null){
					if(stateToRestore._boardTiles[x][y]._unitData._isBear){
						AbstractFactory unitFactory = FactoryCreator.getFactory(TypeUnit.TypeBear);
						Bear bear = null;
						bear = unitFactory.getBear(TypeBear.valueOf(TypeBear.class, "Type" + stateToRestore._boardTiles[x][y]._unitData._unitType));
						Block blockBear = this.boardPanel.block[x][y];
						bear.setHp( stateToRestore._boardTiles[x][y]._unitData._unitHp);
						bear.setDamage(stateToRestore._boardTiles[x][y]._unitData._unitDamage);
						bear.setCurrentPoint(new Point(x, y));
						blockBear.store(bear);
					}
					
					if(stateToRestore._boardTiles[x][y]._unitData._isBuff){
						AbstractFactory unitFactory = FactoryCreator.getFactory(TypeUnit.TypeBuff);
						Buff buff = null;
						buff = unitFactory.getBuffalo(TypeBuff.valueOf(TypeBuff.class, "Type" + stateToRestore._boardTiles[x][y]._unitData._unitType));
						Block blockBuff = this.boardPanel.block[x][y];
						buff.setHp( stateToRestore._boardTiles[x][y]._unitData._unitHp);
						buff.setDamage(stateToRestore._boardTiles[x][y]._unitData._unitDamage);
						buff.setCurrentPoint(new Point(x, y));
						blockBuff.store(buff);
					}
				}
			}
		}
	}
	
	public void _undoMoves(){
		
		//Remove the last 3 states, if there are 3 to remove. Otherwise leave 1 state.
		for(int i = 0; i < _turnsToUndo; i++){
			if(_previousBoardStates.size() > 1){
				_previousBoardStates.remove(_previousBoardStates.size()-1);
			}
		}
		
		//If there's a state in the list, then restore it.
		if(!_previousBoardStates.isEmpty()) _restoreBoardState(_previousBoardStates.get(_previousBoardStates.size()-1));
	}
	
	public void _saveBoardStateToFile()
	{
		BoardState boardState = _previousBoardStates.get(_previousBoardStates.size()-1);
		String newline = System.getProperty("line.separator");
		
		 try {
			OutputStream os = new FileOutputStream("board.txt");
			
			//Record if it was the bears turn
			os.write(Boolean.toString(this.isTurnBear).getBytes());
			os.write( newline.getBytes());
	
			//Then save the rest of the board
			for (int x = 0; x < BoardPanel.kVERTICAL_NO; x++) {
				for (int y = 0; y < BoardPanel.kHORIZONTALL_NO; y++) {
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
	
	public void _loadBoardStateFromFile()
	{
		BoardState loadedState = new BoardState(BoardPanel.kVERTICAL_NO, BoardPanel.kHORIZONTALL_NO);
		
		 try {
		   BufferedReader is = new BufferedReader(new FileReader ("board.txt"));
		   
		   String line;
		   
		   line = is.readLine();
			   
		   boolean wasBearTurn = Boolean.parseBoolean(line);
		   loadedState._isBearTurn = wasBearTurn;
		   
			for (int x = 0; x < BoardPanel.kVERTICAL_NO; x++) {
				for (int y = 0; y < BoardPanel.kHORIZONTALL_NO; y++) {
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
	
	//Spawn an obstacle that user can't click on
	public void spawnObstacles(){
		Random rand = new Random();
		
		//set 5 random blocks
		//haven't implement exclude the starting position of the bear and buff
		for(int i = rand.nextInt(5); i>0; i--){
			int x = rand.nextInt(boardPanel.kVERTICAL_NO);
			int y = rand.nextInt(boardPanel.kHORIZONTALL_NO);
			
			Point pointClick = new Point(x, y);
			this.boardPanel.setObstacleOfBoard(pointClick, new BoardActionListener(this, pointClick));
			//set the button block enabled to be false
		}
		
	}
}

