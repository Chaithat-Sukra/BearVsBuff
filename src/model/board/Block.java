package model.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import model.unit.Unit;
import model.unit.Bear;
import model.unit.Buff;

import controller.listener.BoardActionListener;
import controller.system.GameEngineController;
import utility.observer.Observer;

public class Block extends Observer {	
	public boolean isStoreBear = false;
	public boolean isStoreBuff = false;
	public boolean isHighlight = false;
	
	private Point _point;
	private JPanel _pnBlock;
	public JButton _btnBlock;		
	private Bear _bear;
	private Buff _buff;
	
	public Block(JPanel aPanel, Point aPoint, BoardActionListener aListener) {
		this._pnBlock =  aPanel;
		this._point = aPoint;
		
		this._btnBlock = this._getButtonFromResource(null, 100, 100);
		this._btnBlock.addActionListener(aListener);
		
		aPanel.add(this._btnBlock);
		
		this._btnBlock.setEnabled(false);
		this._btnBlock.setIcon(null);
		this._btnBlock.setBackground(null);
		this._btnBlock.setOpaque(true);
		this._btnBlock.setBorderPainted(false);
		this._btnBlock.repaint();
	}

	public void store(Unit aUnit) {
		Image img = null;
		if (aUnit instanceof Bear) {
			isStoreBear = true;
			
			this._bear = (Bear)aUnit;	
			img = Toolkit.getDefaultToolkit().getImage(this._bear.getImage());				
		}
		if (aUnit instanceof Buff) {
			isStoreBuff = true;
			
			this._buff = (Buff)aUnit;	
			img = Toolkit.getDefaultToolkit().getImage(this._buff.getImage());				
		}
		ImageIcon imgIconCard = new ImageIcon(img);
		this._btnBlock.setIcon(imgIconCard);
		this._btnBlock.setEnabled(true);
	}
	
	public void remove() {
		this._bear = null;
		this._buff = null;
		this.isHighlight = false;
		this.isStoreBear = false;
		this.isStoreBuff = false;
		this._btnBlock.setEnabled(false);
		this._btnBlock.setIcon(null);
		this._btnBlock.setBackground(null);
		this._btnBlock.setOpaque(true);
		this._btnBlock.setBorderPainted(false);
		this._btnBlock.repaint();
	}
	
	public void setHighlight(boolean aHighlight) {
		this.isHighlight = aHighlight;
		
		if (this.isHighlight) {
			if (!this.isStoreBear && !this.isStoreBuff) {
				this._btnBlock.setEnabled(true);
				this._btnBlock.setBackground(Color.red);
			}	
			else {
				this._btnBlock.setEnabled(false);
			}
		}
		else {
			this._btnBlock.setEnabled(true);
			this._btnBlock.setBackground(null);
		}

		this._btnBlock.setOpaque(true);
		this._btnBlock.setBorderPainted(false);
		this._btnBlock.repaint();
	}
	
	public JPanel getPanelBlock() {
		return _pnBlock;
	}

	public void setPanelBlock(JPanel _pnBlock) {
		this._pnBlock = _pnBlock;
	}

	public JButton getBtnBlock() {
		return _btnBlock;
	}

	public void setBtnBlock(JButton _btnBlock) {
		this._btnBlock = _btnBlock;
	}
	
	private JButton _getButtonFromResource(String aResource, int aWidth, int aHeight) {
		JButton btn = new JButton();
		btn.setPreferredSize(new Dimension(aWidth, aHeight));
		btn.setEnabled(false);
		return btn;
	}

	public Point getPoint() {
		return _point;
	}
	public void setPoint(Point _point) {
		this._point = _point;
	}
	
	public Bear getBear() {
		return _bear;
	}

	public void setBear(Bear _bear) {
		this._bear = _bear;
	}

	public Buff getBuff() {
		return _buff;
	}

	public void setBuff(Buff _buff) {
		this._buff = _buff;
	}

	@Override
	public void updateHighlight(boolean aHighlight) {
		this.setHighlight(aHighlight);
	}

	@Override
	public void setSubject(GameEngineController aSubject) {
		this.subject = aSubject;
		this.subject.attachObserver(this);
	}

	@Override
	public boolean dealDamage() {
		if (this.subject.isTurnBear) {
			if (this.isStoreBuff) {
				Buff buff = this.getBuff(); 
				if (buff.deal(this.subject.currentBear.getDamage())) {
					return true;							
				}				
			}	
			return false;
		}
		else {
			if (this.isStoreBear) {
				Bear bear = this.getBear();
				if (bear.deal(this.subject.currentBuff.getDamage())) {
					return true;							
				}				
			}
			return false;
		}
	}
}
