package worm;

import java.awt.*;
import javax.swing.*;

/**
 * A simple graphical user interface (GUI) generator, which uses an array for
 * looking up menu items and their actions.
 */
public class Worm extends JFrame {

	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					sg.createAndShowGUI();
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		sg.gl=new GameLoop(sg,sg.menuActions,sg.keyActions);
		sg.gl.start();
	}

	private void createAndShowGUI() {
		java.net.URL imageURL = getClass().getResource("/images/icon.jpg");
		if (imageURL != null) {
		    ImageIcon icon = new ImageIcon(imageURL);
		    setIconImage(icon.getImage());
		} else {
			System.err.println("Icon file not found.");
		}

		setTitle("Worm - (C) Mikko Nummelin, 2009");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setJMenuBar(menuBar);

		/* Different menus should be added here */
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);

		/*
		 * Menu components are read from an array structure
		 * items[] defined below.
		 */
		for(int i=0;i<items.length;i++) {
			if(items[i].item!=null) {
				items[i].menu.add(items[i].item);
			} else {
				items[i].menu.add(new JSeparator());
			}
			if(items[i].command!=null) {
				items[i].item.setActionCommand(items[i].command);
				items[i].item.addActionListener(menuActions);
			}
		}

		/*
		 * Setting just one panel into the GUI is an arbitrary choice
		 * as is the size of the panel.
		 */
		panel = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				updateViewSync(g,true);
			}
		};
		panel.setPreferredSize(new Dimension(0x200,0x200));
		panel.setBackground(Color.BLACK);
		addKeyListener(keyActions);
		add(panel);

		/* As small GUI as possible to contain the inside components */
		pack();

		/* Change this to 'true' if you want resizable GUI */
		setResizable(false);

		setVisible(true);
	}
	
	void updateView() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Graphics g=panel.getGraphics();
				updateViewSync(g,false);
			}
		});
	}
	
	void forceUpdateView() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Graphics g=panel.getGraphics();
				updateViewSync(g,true);
			}
		});
	}
	
	private void updateViewSync(Graphics g, boolean force) {
		for(int i=0;i<0x20;i++) {
			for(int j=0;j<0x20;j++) {
				if(force||(gl.screenData[i][j]!=gl.prevScreenData[i][j])) {
					switch(gl.screenData[i][j]) {
					case ' ':
						g.setColor(Color.BLACK);
						g.fillRect(0x10*j,0x10*i,0x10,0x10);
						break;
					case '#':
						g.setColor(Color.RED);
						g.fillRect(0x10*j+1,0x10*i+1,0xe,0xe);
						break;
					case 'h':
						g.setColor(new Color(0x007f00));
						g.fillOval(0x10*j+1,0x10*i+1,0xe,0xe);
						break;
					case 'w':
						g.setColor(Color.GREEN);
						g.fillOval(0x10*j+1,0x10*i+1,0xe,0xe);
						break;
					case 'f':
						g.setColor(Color.CYAN);
						g.fillOval(0x10*j+1,0x10*i+1,0xe,0xe);
						break;
					case 'b':
						g.setColor(Color.BLUE);
						g.fillOval(0x10*j+1,0x10*i+1,0xe,0xe);
						break;
					}
				}
			}
		}
	}
	
	private static Worm sg = new Worm();
	private GameLoop gl;

	/* The central area panel */
	private JPanel panel;

	/* Menu bar */
	private JMenuBar  menuBar    = new JMenuBar();

	/* File menu */
	private JMenu     fileMenu   = new JMenu("File");
	private JMenuItem loadItem   = new JMenuItem("Load");
	private JMenuItem saveItem   = new JMenuItem("Save");
	private JMenuItem exitItem   = new JMenuItem("Exit");

	/* Help menu */
	private JMenu     helpMenu   = new JMenu("Help");
	private JMenuItem aboutItem  = new JMenuItem("About");

	/* Action and key listeners are implemented in separate classes */
	private MenuActions menuActions = new MenuActions(this);
	private KeyActions keyActions = new KeyActions(this);

	/*
	 * Menu item association to action commands. Use null values for
	 * item and action command for menu separators and keep the order.
	 */
	private MenuItemAssoc items[] = {
		new MenuItemAssoc(fileMenu , loadItem  , "load"  ),
		new MenuItemAssoc(fileMenu , saveItem  , "save"  ),
		new MenuItemAssoc(fileMenu , null      , null    ),
		new MenuItemAssoc(fileMenu , exitItem  , "exit"  ),
		new MenuItemAssoc(helpMenu , aboutItem , "about" ),
	};

	private class MenuItemAssoc {
		JMenu menu;
		AbstractButton item;
		String command;

		public MenuItemAssoc(JMenu menu, AbstractButton item, String command) {
			this.menu    = menu;
			this.command = command;
			this.item    = item;
		}
	}
}
