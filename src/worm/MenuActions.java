package worm;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;

class MenuActions implements ActionListener {
	private JFrame parent;

	public MenuActions(JFrame parent) {
		this.parent=parent;
	}

	/**
	 * Handles the action commands registered in <code>Worm</code>.
	 *
	 * @see Worm.java
	 */
	public void actionPerformed(ActionEvent e) {
		String ac=e.getActionCommand();
		if(ac.equals("load")) {
			JFileChooser chooser = new JFileChooser();
			int returnVal = chooser.showOpenDialog(parent);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println("You chose to open this file: " +
					chooser.getSelectedFile().getName());
			}
		} else if(ac.equals("save")) {
			JFileChooser chooser = new JFileChooser();
			int returnVal = chooser.showSaveDialog(parent);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println("You chose to save this file: " +
					chooser.getSelectedFile().getName());
			}
		} else if(ac.equals("exit")) {
			System.exit(0);
		} else if(ac.equals("about")) {
			JOptionPane.showMessageDialog(parent,"A worm game based on\nMikko's Simple GUI Framework\n(C) Mikko Nummelin, 2009.",
					"About Worm",JOptionPane.INFORMATION_MESSAGE);
		} else {
			System.err.println("Not implemented: "+ac);
		}
	}
}
