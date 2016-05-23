package controller.listener;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import model.board.Block;
import model.unit.Bear;
import model.unit.Buff;
import model.unit.Unit;
import view.BroadPanel;
import controller.system.*;

public class BoardActionListener implements ActionListener {
	
	GameEngineController _controller;
	Point _point;
	
	public BoardActionListener(GameEngineController aController, Point aPoint) {
		this._controller = aController;
		this._point = aPoint;
	}
	
	private void _handleObserverSubject(BroadPanel aBoardPanel, List<Point> aPoints) {
		this._controller.clearObserver();
		for (Point aPoint : aPoints) {
			if (this._controller.isScope(aPoint)) {
				Block possibleBlock = aBoardPanel.block[aPoint.x][aPoint.y];
				possibleBlock.setSubject(_controller);	
			}						
		}
	}
	
	private void _reloadBlockAndUnit(BroadPanel aBoardPanel, Point aOldPoint, Point aNewPoint, Unit aUnit) {
		//remove highlight
		this._controller.highlightMoveRanges(false);
		
		//remove old block
		Block oldBlock = aBoardPanel.block[aOldPoint.x][aOldPoint.y];
		oldBlock.remove();
		
		//set new point of unit && draw unit
		Block newBlock = aBoardPanel.block[aNewPoint.x][aNewPoint.y];									
		aUnit.setCurrentPoint(aNewPoint);									
		newBlock.store(aUnit);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {	
		BroadPanel boardPanel = this._controller.boardPanel;
		Block block =  boardPanel.block[this._point.x][this._point.y];
		
		if (this._controller.isTurnBear) {
			//check to show move range
			Bear bear = block.getBear();
			if (bear != null) {
				Point pointUnit = bear.getCurrentPoint();
				if (pointUnit.x == this._point.x && pointUnit.y == this._point.y) {
					this._controller.currentBear = bear;
					
					List<Point> list = bear.getMoveRange();
					_handleObserverSubject(boardPanel, list);					
					this._controller.highlightMoveRanges(true);					
					return;
				}	
			}
			//move unit
			if (this._controller.currentBear != null) {
				bear = this._controller.currentBear; 
				Point oldPoint = bear.getCurrentPoint();
				//check not to press enemy
				if (!block.isStoreBuff) {
					_reloadBlockAndUnit(boardPanel, oldPoint, this._point, bear);
					
					//attack enemy in range
					this._controller.attackEnemyInRange();
					this._controller.isTurnBear = false;
				}
			}
		}
		else {
			//check to show move range
			Buff buff = block.getBuff();
			if (buff != null) {
				Point pointUnit = buff.getCurrentPoint();
				if (pointUnit.x == this._point.x && pointUnit.y == this._point.y) {
					this._controller.currentBuff = buff;
					
					List<Point> list = buff.getMoveRange();
					_handleObserverSubject(boardPanel, list);	
					this._controller.highlightMoveRanges(true);
					return;
				}
			}
			//move unit
			if (this._controller.currentBuff != null) {
				buff = this._controller.currentBuff;
				Point oldPoint = buff.getCurrentPoint();
				if (!block.isStoreBear) {
					_reloadBlockAndUnit(boardPanel, oldPoint, this._point, buff);
					
					//attack enemy in range
					this._controller.attackEnemyInRange();
					this._controller.isTurnBear = true;
				}
			}
		}
	}
}
