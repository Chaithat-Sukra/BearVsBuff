package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import model.board.Block;

public class BroadPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2742699278387491732L;
	public static final int kVERTICAL_NO = 7;	
	public static final int kHORIZONTALL_NO = 7;
	
	public Block block[][] = new Block[kVERTICAL_NO][kHORIZONTALL_NO];;
	
	public BroadPanel() {
		this.setLayout(new GridLayout(kVERTICAL_NO, kHORIZONTALL_NO, 1, 1));
		this.setBackground(Color.black);		
	}
	
	public void setContentOfBoard (Point aPoint, ActionListener aListener) {
		this.block[aPoint.x][aPoint.y] = new Block(new JPanel(), aPoint, aListener);
		this.add(this.block[aPoint.x][aPoint.y].getPanelBlock());
		
		//for testing
		Label label = new Label(String.valueOf(aPoint.x) + String.valueOf(aPoint.y));
		this.block[aPoint.x][aPoint.y].getPanelBlock().add(label);
	}
}
