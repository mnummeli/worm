package worm;

import java.util.*;

public class GameLoop extends Thread {
	
	/* Game state variables */
	static final int START=0;
	static final int PLAYING=1;
	static final int PAUSED=2;
	static final int GAME_OVER=3;
	int state=START;
	int score;
	
	public GameLoop(Worm gui, MenuActions menuActions, KeyActions keyActions) {
		this.gui=gui;
		this.menuActions=menuActions;
		this.keyActions=keyActions;
	}
	
	public void run() {
		wormDirection=RIGHT;
		keyActions.right=keyActions.left=keyActions.up=keyActions.down=false;
		while((state==START)||(state==GAME_OVER)) {
			gui.updateStatusPanels();
			synchronized(this) {
				try {
					wait();
				} catch (InterruptedException iex) {}
			}
		}
		cycles=0; score=0; gui.updateStatusPanels();
		rand=new Random(Calendar.getInstance().getTimeInMillis());
		wormNodes=new LinkedList<Coordinates>();
		wormNodes.addFirst(new Coordinates(0x10,0x10));
		initScreenData();
		setNewFruit();
		gui.forceUpdateView();
		while(true) {
			while(state==PAUSED) {
				gui.updateStatusPanels();
				synchronized(this) {
					try {
						wait();
					} catch (InterruptedException iex) {}
				}
				gui.updateStatusPanels();
			}
			try {
				int delayTime=(int)(300*Math.exp(-Math.log(2)*score/30));
				Thread.sleep(delayTime);
			} catch (InterruptedException iex) {}
			copyPrevScreenData();
			setWormDirection();
			moveWorm();
			gui.updateView();
			cycles++;
		}
	}

	private void moveWorm() {
		Coordinates prevHead, newHead;
		prevHead=wormNodes.getFirst();
		if(screenData[prevHead.y][prevHead.x]!='b') {
			screenData[prevHead.y][prevHead.x]='w';
		}
		switch(wormDirection) {
		case RIGHT:
			newHead=new Coordinates(prevHead.x+1,prevHead.y);
			break;
		case UP:
			newHead=new Coordinates(prevHead.x,prevHead.y-1);
			break;
		case LEFT:
			newHead=new Coordinates(prevHead.x-1,prevHead.y);
			break;
		case DOWN:
			newHead=new Coordinates(prevHead.x,prevHead.y+1);
			break;
		default:
			newHead=new Coordinates(prevHead.x,prevHead.y);
			break;
		}
		wormNodes.addFirst(newHead);
		switch(screenData[newHead.y][newHead.x]) {
		case '#':
		case 'w':
			gui.drawGameOver();
			state=GAME_OVER;
			run();
		case 'f':
			lengthen=2;
			screenData[newHead.y][newHead.x]='b';
			score++; gui.updateStatusPanels();
			setNewFruit();
			break;
		case 'b':
			lengthen=3;
			screenData[newHead.y][newHead.x]='w';
			score++; gui.updateStatusPanels();
			setNewFruit();
			break;
		}
		if(screenData[newHead.y][newHead.x]!='b') {
			screenData[newHead.y][newHead.x]='h';
		}
		if(lengthen==0) {
			Coordinates prevTail=wormNodes.removeLast();
			screenData[prevTail.y][prevTail.x]=' ';
		} else {
			lengthen--;
		}
	}
	
	private void setNewFruit() {
		char prevTry='#';
		while(prevTry!=' ') {
			Coordinates newFruitLoc=
				new Coordinates(rand.nextInt(0x20),rand.nextInt(0x20));
			prevTry=screenData[newFruitLoc.y][newFruitLoc.x];
			if(prevTry==' ') {
				screenData[newFruitLoc.y][newFruitLoc.x]='f';
			}
		}
	}

	private void initScreenData() {
		for(int i=0;i<0x20;i++) {
			for(int j=0;j<0x20;j++) {
				if((i<=1)||(i>=0x1e)||(j<=1)||(j>=0x1e)) {
					screenData[i][j]='#';
				} else {
					screenData[i][j]=' ';
				}
			}
		}
		for(int i=2;i<7;i++) {
			screenData[0x9][i]='#';
			screenData[0xa][i]='#';
			screenData[0x15][i]='#';
			screenData[0x16][i]='#';
			screenData[0x9][0x1f-i]='#';
			screenData[0xa][0x1f-i]='#';
			screenData[0x15][0x1f-i]='#';
			screenData[0x16][0x1f-i]='#';
			screenData[i][0x9]='#';
			screenData[i][0xa]='#';
			screenData[i][0x15]='#';
			screenData[i][0x16]='#';
			screenData[0x1f-i][0x9]='#';
			screenData[0x1f-i][0xa]='#';
			screenData[0x1f-i][0x15]='#';
			screenData[0x1f-i][0x16]='#';
		}
		for(int i=0;i<6;i++) {
			screenData[0x0d+i][0x09]='#';
			screenData[0x0d+i][0x0a]='#';
		}
		Iterator<Coordinates> iter=wormNodes.listIterator();
		if(iter.hasNext()) {
			Coordinates c=iter.next();
			screenData[c.y][c.x]='h';
			while(iter.hasNext()) {
				c=iter.next();
				screenData[c.y][c.x]='w';
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
			if(wormDirection!=LEFT) {
				wormDirection=RIGHT;
			}
		} else if(keyActions.up) {
			if(wormDirection!=DOWN) {
				wormDirection=UP;
			}
		} else if(keyActions.left) {
			if(wormDirection!=RIGHT) {
				wormDirection=LEFT;
			}
		} else if(keyActions.down) {
			if(wormDirection!=UP) {
				wormDirection=DOWN;
			}
		}
	}
	
	int cycles;

	private Worm gui;
	private MenuActions menuActions;
	private KeyActions keyActions;

	private final int RIGHT = 0;
	private final int UP    = 1;
	private final int LEFT  = 2;
	private final int DOWN  = 3;
	
	private int wormDirection;
	char[][] screenData=new char[0x20][0x20];
	char[][] prevScreenData=new char[0x20][0x20];
	private LinkedList<Coordinates> wormNodes;
	private Random rand;
	private int lengthen=0;

	class Coordinates {
		public int x,y;
		public Coordinates(int x, int y) {
			this.x=x;
			this.y=y;
		}
	}
}
