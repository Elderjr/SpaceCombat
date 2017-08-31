package client.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class Keyboard implements KeyListener{

	private static final Keyboard instance = new Keyboard();
	private boolean keys[];
	
	private Keyboard(){
		this.keys = new boolean[255];
	}
	
	public static Keyboard getInstance(){
		return instance;
	}
	
	public boolean[] getKeys(){
		return this.keys;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		this.keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.keys[e.getKeyCode()] = false;
	}

}
