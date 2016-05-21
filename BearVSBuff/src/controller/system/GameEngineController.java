package controller.system;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
	
	int _noOfBuff = 3;
	int _noOfBear = 3;
	
	public GameEngineController() {
		this._isTurnBear = true;
		
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
								JOptionPane.showConfirmDialog(null, "Bear is a winner -- Buffalo is been killed");							
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
								JOptionPane.showConfirmDialog(null, "Buffalo is a winner -- Bear is been killed");
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
}
