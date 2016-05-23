package controller.system;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import controller.listener.BoardActionListener;
import view.BoardFrame;
import view.BroadPanel;
import model.board.Block;
import model.unit.*;
import utility.abstractFactory.AbstractFactory;
import utility.abstractFactory.FactoryCreator;
import utility.abstractFactory.BearFactory.TypeBear;
import utility.abstractFactory.BuffaloFactory.TypeBuff;
import utility.abstractFactory.FactoryCreator.TypeUnit;
import utility.observer.Observer;

public class GameEngineController {

	public BroadPanel boardPanel = new BroadPanel();
	public boolean isTurnBear;
	
	private BearBoss _bossBear;
	private BuffBoss _bossBuff;
	public Bear currentBear;
	public Buff currentBuff;
	
	private BoardFrame _fBoard;	
	
	int _noOfBuff = 3;
	int _noOfBear = 3;
	
	private List<Observer> _observers = new ArrayList<Observer>();
	
	public GameEngineController() {
		this.isTurnBear = true;
		
		this._fBoard = new BoardFrame();  	
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
		if (aPoint.x < BroadPanel.kVERTICAL_NO && aPoint.y < BroadPanel.kHORIZONTALL_NO && aPoint.x >= 0 && aPoint.y >= 0)
			return true;
		return false;
	}
	
	public void attachObserver(Observer aObserver) {
		this._observers.add(aObserver);
	}
	
	public void clearObserver() {
		this._observers.clear();
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
			for (int x = 0; x < BroadPanel.kVERTICAL_NO; x++) {
				for (int y = 0; y < BroadPanel.kHORIZONTALL_NO; y++) {
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
}
