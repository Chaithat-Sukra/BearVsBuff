package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.board.Block;
import controller.listener.BoardActionListener; 

public class BoardPanel extends JPanel {
	
	private static final long serialVersionUID = -2742699278387491732L;
	public static final int kVERTICAL_NO = 7;	
	public static final int kHORIZONTALL_NO = 7;
	
	public Block block[][] = new Block[kVERTICAL_NO][kHORIZONTALL_NO];;
	
	
	public BoardPanel() {
		this.setLayout(new GridLayout(kVERTICAL_NO, kHORIZONTALL_NO, 1, 1));
		this.setBackground(Color.black);		
	}
	
	public BoardPanel(Point point) {
		this.setLayout(new GridLayout(kVERTICAL_NO, kHORIZONTALL_NO, point.x, point.y));
		this.setBackground(Color.black);	
	}
	
	public void setContentOfBoard(Point aPoint, BoardActionListener aListener) {
		this.block[aPoint.x][aPoint.y] = new Block(new JPanel(), aPoint, aListener);
		this.add(this.block[aPoint.x][aPoint.y].getPanelBlock());
		
		//for testing
//		Label label = new Label(String.valueOf(aPoint.x) + String.valueOf(aPoint.y));
//		this.block[aPoint.x][aPoint.y].getPanelBlock().add(label);
	}
	
	public void setObstacleOfBoard(Point aPoint, BoardActionListener aListener) {
		//should set new constructor that give the blocking sprite
		//this.block[aPoint.x][aPoint.y] = new Block(new JPanel(), aPoint, aListener);
		
		this.add(this.block[aPoint.x][aPoint.y].getPanelBlock());
		//i change the visibility of the _btnblock to public to gain this acces, not sure if its good idea
		this.block[aPoint.x][aPoint.y]._btnBlock.setEnabled(false);
		
	}
}
