package client.input;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener{

	private static final Mouse instance = new Mouse();
	public static enum STATE{ PRESSED, RELEASED};
	private Mouse.STATE state;
	private Point mouseLocation;
	
	private Mouse(){
	}
	
	public static Mouse getInstance(){
		return instance;
	}
	
	
	public Mouse.STATE getState(){
		return state;
	}
	
	public Point getMouseLocation(){
		return mouseLocation;
	}
	
	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {
	}

	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {
		this.state = Mouse.STATE.PRESSED;
		this.mouseLocation = e.getPoint();
	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
		this.state = Mouse.STATE.RELEASED;
		this.mouseLocation = null;
	}

	@Override
	public void mouseEntered(java.awt.event.MouseEvent e) {
	}

	@Override
	public void mouseExited(java.awt.event.MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

}
