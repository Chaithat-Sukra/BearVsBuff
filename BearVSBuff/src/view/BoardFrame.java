package view;

import javax.swing.JFrame;

public class BoardFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BoardFrame() {
		setBounds(0, 0, 800, 800);
		setTitle("Bear VS Buffalo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}	
}
