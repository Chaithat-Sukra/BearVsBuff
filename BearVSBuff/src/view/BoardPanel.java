package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Point;

import javax.swing.JPanel;

import model.board.Block;
import controller.listener.BoardActionListener; 

public class BoardPanel extends JPanel {
	
	/**
	 * 
	 */
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
	
	public void setContentOfBoard (Point aPoint, BoardActionListener aListener) {
		this.block[aPoint.x][aPoint.y] = new Block(new JPanel(), aPoint, aListener);
		this.add(this.block[aPoint.x][aPoint.y].getPanelBlock());
		
		//for testing
		Label label = new Label(String.valueOf(aPoint.x) + String.valueOf(aPoint.y));
		this.block[aPoint.x][aPoint.y].getPanelBlock().add(label);
	}
}
