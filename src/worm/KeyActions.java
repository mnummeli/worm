package worm;

import java.awt.event.*;
import javax.swing.*;

/**
 * Handles the key commands registered in <code>Worm</code>.
 *
 * @see Worm.java
 */
class KeyActions implements KeyListener {
	private JFrame parent;
	boolean left  = false;
	boolean up    = false;
	boolean right = false;
	boolean down  = false;

	public KeyActions(JFrame parent) {
		this.parent=parent;
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyText(e.getKeyCode()).equals("F10") ||
			e.getKeyText(e.getKeyCode()).equals("Escape")) {
			System.exit(0);
		} else if(e.getKeyText(e.getKeyCode()).equals("Left")) {
			left=true;
			up=right=down=false;
		} else if(e.getKeyText(e.getKeyCode()).equals("Up")) {
			up=true;
			left=right=down=false;
		} else if(e.getKeyText(e.getKeyCode()).equals("Right")) {
			right=true;
			left=up=down=false;
		}  else if(e.getKeyText(e.getKeyCode()).equals("Down")) {
			down=true;
			left=up=right=false;
		} else {
			System.out.println(e);
		}
	}

	public void keyReleased(KeyEvent e) {
		// left=up=right=down=false;
	}

	public void keyTyped(KeyEvent e) {
		// System.out.println(e);
	}	
}
