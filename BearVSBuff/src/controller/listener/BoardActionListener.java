package controller.listener;

import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import model.unit.Bear;
import model.unit.Buff;
import view.BroadPanel;
import controller.system.*;

public class BoardActionListener implements ActionListener {
	private BroadPanel _board;
	private Bear _bear;
	private Buff _buff;
	private Point _point;
	
	public BoardActionListener(BroadPanel aBoard, Point aPoint, GameEngineController aController) {
		this._point = aPoint;
		this._board = aBoard;
	}

	private void _deleteBearRangeFromOldPanels() {
		List<Point> list = this._bear.getMoveRange(); 
		for (Point point : list) {
			if (point.x < 7 && point.y < 7 && point.x > 0 && point.y > 0) {
				JButton oldButton = this._board.block[point.x][point.y].getBtnBlock();
				oldButton.setIcon(null);
				oldButton.setBackground(null);
				oldButton.addActionListener(null);
			}
		}	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
//		//check if 
//		
//		if (this._bear != null) {
//			//delete bear from old panel
//			this._deleteBearRangeFromOldPanels();
//			
//			//add bear to new panel
//			this._bear.setCurrentPoint(this._point);			
//			JButton selectedButton = this._board.block[this._point.x][this._point.y].getBtnBlock();
//			String imageName = "resource/" + this._bear.getImage();
//			
//			if (imageName != null) {
//				Image img = Toolkit.getDefaultToolkit().getImage(imageName);
//				ImageIcon imgIcon = new ImageIcon(img);
//				selectedButton.setIcon(imgIcon);					
//			}
//			selectedButton.addActionListener(this._bear.getListener());
//		}
//		if (this._buff != null) {
//			this._buff.setCurrentPoint(_point);
////			this._board.reloadBuff(this._buff);
//		}
	}
}
