package controller.listener;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.system.GameEngineController;

public class FrameActionListener implements ActionListener{
	
	GameEngineController _controller;
	
	public FrameActionListener(GameEngineController aController) {
		this._controller = aController;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    int action = Integer.parseInt(e.getActionCommand());
	    switch (action) {
	    case 1:
	        _controller._undoMoves();
	        break;
	    case 2:
	    	_controller._saveBoardStateToFile();
	        break;  
	    case 3:
	    	_controller._loadBoardStateFromFile();
	        break;  
	    default:
	        break;
	    }
		
	}
	
}
