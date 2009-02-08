package worm;

public class GameLoop extends Thread {
	
	public GameLoop(Worm gui, MenuActions menuActions, KeyActions keyActions) {
		this.gui=gui;
		this.menuActions=menuActions;
		this.keyActions=keyActions;
	}
	
	public void run() {
		initScreenData();
		gui.forceUpdateView();
		while(true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException iex) {}
			copyPrevScreenData();
			setWormDirection();
			// moveWorm();
			gui.updateView();
		}
	}

	/*
	private void moveWorm() {
		checkCollision();
		checkEatFruit();
	}
	*/

	private void initScreenData() {
		for(int i=0;i<0x20;i++) {
			for(int j=0;j<0x20;j++) {
				if((i==0)||(i==0x1f)||(j==0)||(j==0x1f)) {
					screenData[i][j]='#';
				} else {
					screenData[i][j]=' ';
				}
			}
		}
	}

	private void copyPrevScreenData() {
		for(int i=0;i<0x20;i++) {
			for(int j=0;j<0x20;j++) {
				prevScreenData[i][j]=screenData[i][j];
			}
		}
	}

	private void setWormDirection() {
		if(keyActions.right) {
			wormDirection=RIGHT;
		} else if(keyActions.up) {
			wormDirection=UP;
		} else if(keyActions.left) {
			wormDirection=LEFT;
		} else if(keyActions.down) {
			wormDirection=DOWN;
		}
	}

	private Worm gui;
	private MenuActions menuActions;
	private KeyActions keyActions;

	private final int RIGHT = 0;
	private final int UP    = 1;
	private final int LEFT  = 2;
	private final int DOWN  = 3;
	
	private int wormDirection=RIGHT;
	char[][] screenData=new char[0x20][0x20];
	char[][] prevScreenData=new char[0x20][0x20];

	class Coordinates {
		public int x,y;
		public Coordinates(int x, int y) {
			this.x=x;
			this.y=y;
		}
	}
}
