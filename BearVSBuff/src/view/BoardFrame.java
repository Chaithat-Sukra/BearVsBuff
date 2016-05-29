package view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import controller.listener.BoardActionListener;
import controller.listener.FrameActionListener;

public class BoardFrame extends JFrame {

	public JButton undoButton = new JButton("Undo");
	public JButton saveButton = new JButton("Save");
	public JButton loadButton = new JButton("Load");

	private static final long serialVersionUID = 1L;

	public BoardFrame(FrameActionListener aListener) {
		//Create a toolbar and 3 button which are save, load and undo
		JToolBar tools = new JToolBar();
		tools.setFloatable(false);
        this.add(tools, BorderLayout.PAGE_START);
		tools.setFloatable(false);
        tools.add(undoButton);
        tools.add(saveButton);
        tools.add(loadButton);
        tools.addSeparator();
        
        //create the action listener
        undoButton.addActionListener(aListener);
        saveButton.addActionListener(aListener);
        loadButton.addActionListener(aListener);
        
        //set action command
		undoButton.setActionCommand("1");
		saveButton.setActionCommand("2");
		loadButton.setActionCommand("3");
		
		setBounds(0, 0, 800, 800);
		setTitle("Bear VS Buffalo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}	
}
