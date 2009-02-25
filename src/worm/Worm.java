/*
 * A simple worm game, graphical user interface (GUI) routines.
 * (C) 2009, Mikko Nummelin
 */

package worm;

import java.awt.*;
import javax.swing.*;

/**
 * A simple graphical user interface (GUI) generator, which uses an array for
 * looking up menu items and their actions.
 */
public class Worm extends JFrame {

	/**
	 * The game entry point. Creates the GUI in event dispatcher thread
	 * and creates game loop as new thread.
	 * @param args	Not in use.
	 */
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
		sg.keyActions.gl=sg.gl;
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
		java.net.URL brickURL = getClass().getResource("/images/brick.jpg");
		if (brickURL != null) {
		    ImageIcon brick = new ImageIcon(brickURL);
		    brickImage=brick.getImage();
		} else {
			System.err.println("Brick image file not found.");
		}

		setTitle("Worm - (C) Mikko Nummelin, 2009");
		setLayout(new BorderLayout());
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

		JPanel gamePanel=new JPanel(new BorderLayout());
		gamePanel.setPreferredSize(new Dimension(0x200,0x220));
		infoPanel=new JPanel(new BorderLayout());
		infoPanel.setPreferredSize(new Dimension(0x100,0x20));
		infoPanel.add(infoLabel=new JLabel(),BorderLayout.CENTER);
		scorePanel=new JPanel(new BorderLayout());
		scorePanel.setPreferredSize(new Dimension(0x80,0x20));
		scorePanel.add(scoreLabel=new JLabel(),BorderLayout.CENTER);
		highScorePanel=new JPanel(new BorderLayout());
		highScorePanel.setPreferredSize(new Dimension(0x80,0x20));
		highScorePanel.add(highScoreLabel=new JLabel(),BorderLayout.CENTER);
		JPanel topPanel=new JPanel(new BorderLayout());
		topPanel.setPreferredSize(new Dimension(0x200,0x20));
		topPanel.add(infoPanel,BorderLayout.WEST);
		topPanel.add(scorePanel,BorderLayout.CENTER);
		topPanel.add(highScorePanel,BorderLayout.EAST);
		gamePanel.add(topPanel,BorderLayout.NORTH);	
		
		/* The game area */
		panel = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				updateViewSync(g,true);
				updateStatusPanelsSync();
				if((gl!=null)&&(gl.state==GameLoop.GAME_OVER)) {
					drawGameOverSync();
				}
			}
		};
		
		panel.setPreferredSize(new Dimension(0x200,0x200));
		panel.setBackground(Color.BLACK);
		addKeyListener(keyActions);
		gamePanel.add(panel,BorderLayout.SOUTH);
		add(gamePanel);

		/* As small GUI as possible to contain the inside components */
		pack();

		/* Change this to 'true' if you want resizable GUI */
		setResizable(false);

		setVisible(true);
	}
	
	/**
	 * Draws "GAME OVER"-text
	 */
	void drawGameOver() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				drawGameOverSync();
			}
		});
	}
	
	private void drawGameOverSync() {
		Graphics g=panel.getGraphics();
		g.setFont(new Font(Font.MONOSPACED,Font.PLAIN,0x20));
		g.setColor(Color.WHITE);
		g.drawString("Game over",0xb8,0x100);
	}
	
	/**
	 * Updates status panels, i.e. when score increases or state changes.
	 */
	void updateStatusPanels() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				updateStatusPanelsSync();
			}
		});
	}
	
	private void updateStatusPanelsSync() {
		switch(gl.state) {
		case GameLoop.START:
		case GameLoop.GAME_OVER:
			infoLabel.setText("Press SPACE to start.");
			break;
		case GameLoop.PLAYING:
			infoLabel.setText("Playing ... press SPACE to pause.");
			break;
		case GameLoop.PAUSED:
			infoLabel.setText("Paused. Press SPACE to continue.");
			break;
		}
		scoreLabel.setText("Score: "+gl.score);
		highScoreLabel.setText("High score: "+gl.highScore);
	}
	
	/**
	 * Updates the view on those parts which have changed.
	 */
	void updateView() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Graphics g=panel.getGraphics();
				updateViewSync(g,false);
			}
		});
	}
	
	/**
	 * Forces update of the whole view.
	 */
	void forceUpdateView() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					Graphics g=panel.getGraphics();
					updateViewSync(g,true);
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void updateViewSync(Graphics g, boolean force) {
		for(int i=0;i<0x20;i++) {
			for(int j=0;j<0x20;j++) {
				if(force||(gl.screenData[i][j]=='b')||
						(gl.screenData[i][j]!=gl.prevScreenData[i][j])) {
					switch(gl.screenData[i][j]) {
					case ' ':
						g.setColor(Color.BLACK);
						g.fillRect(0x10*j,0x10*i,0x10,0x10);
						break;
					case '#':
						/* g.setColor(Color.RED);
						g.fillRect(0x10*j+1,0x10*i+1,0xe,0xe); */
						g.drawImage(brickImage,0x10*j,0x10*i,panel);
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
						g.setColor(bonusFruitColors[gl.cycles%7]);
						g.fillOval(0x10*j+1,0x10*i+1,0xe,0xe);
						break;
					}
				}
			}
		}
	}
	
	private static Worm sg = new Worm();
	private GameLoop gl;
	
	private Color[] bonusFruitColors={Color.RED,Color.ORANGE,Color.YELLOW,
			Color.GREEN,Color.CYAN,Color.BLUE,Color.MAGENTA};
	
	private Image brickImage;

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
	
	private JPanel infoPanel, scorePanel, highScorePanel;
	private JLabel infoLabel, scoreLabel, highScoreLabel;

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
