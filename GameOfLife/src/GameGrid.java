import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;

/**
 * @author Kevin Guerra kevingnet@gmail.com
 * Panel upon which to draw the grid, reacts to various events, draws cells, 
 * and interacts directly with engine, manages continues step thread
 */
public class GameGrid extends JPanel implements ComponentListener, MouseListener, Runnable {
	private static final long serialVersionUID = 589879760624008850L;
	//private static final Color cellColor = Color.darkGray;
	private static final Color cellColor = new Color(66, 134, 244);
	
	private static final Color gridColor = Color.lightGray;
	private static final int BLOCK_SIZE = 14;
	private static final int SPEED = 12;
	private int speed = SPEED;

	private Dimension dimensions = null;
	private final Cells cells;
	private boolean running = false;
	private boolean consoleOutput = false;

	/**
	 * Default constructor, add event listeners, set game engine
	 * @param cls: game engine
	 */
	public GameGrid(Cells cls) {
		System.out.print("Initializing game grid\n");
		this.cells = cls;
		addComponentListener(this);
		addMouseListener(this);
	}

	/**
	 * Step into the next generation of fit cells
	 */
	public void step() {
		this.cells.step();
		outputCells();
		repaint();
	}

	/**
	 * Fill grid with cells at random positions
	 */
	public void randomFill() {
		for (int i = 0; i < this.dimensions.height; ++i) {
			for (int j = 0; j < this.dimensions.width; ++j) {
				int rand = 1 + (int)(Math.random() * 7);
				if (rand == 1) {
					this.cells.toggle(i, j);
				}
			}
		}
		repaint();
	}

	/**
	 * Fill grid with cells at given positions
	 */
	public void populate(String lines[]) {
		repaint();
		int i = 0;
		int j = 0;
		for (String line : lines) {
			char[] chars = line.toCharArray();
			//System.out.printf("chars length %d\n", chars.length);
			for (char c : chars) {
				if ( c == '0') {
					System.out.printf("populate (%d, %d)\n", i, j);
					this.cells.toggle(i, j);
				}
				j++;
			}
			j = 0;
			i++;
		}
		repaint();
	}

	/**
	 * Clear grid
	 */
	public void clear() {
		for (int i = 0; i < this.dimensions.height; ++i) {
			for (int j = 0; j < this.dimensions.width; ++j) {
   			this.cells.clear(i, j);
			}
		}
		repaint();
	}

	/**
	 * Set speed
	 */
	public void setSpeed(int sp) {
		speed = sp;
	}

	/**
	 * Teleport cells
	 */
	public void toggleTeleportCells() {
		this.cells.toggleTeleport();
	}

	/**
	 * Toggle console output
	 */
	public void toggleConsoleOutput() {
		this.consoleOutput = !this.consoleOutput;
	}

	/**
	 * Get speed
	 */
	public String getSpeed() {
		Integer s = speed;
		return  s.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * Thread run function for continues stepping
	 * Thread sleeps at SPEED intervals
	 */
	@Override
	public void run() {
		this.running = true;
		step();
		try {
			Thread.sleep(1000 / speed);
			Thread.yield();
			run();
		} catch (InterruptedException ex) {
		}
		this.running = false;
	}

	/**
	 * Toggle cell state at specific x,y coordinate
	 * @param x
	 * @param y
	 */
	private void toggleCell(int x, int y) {
		//System.out.printf("GameGrid.setCell(%d, %d)\n", x, y);
		this.cells.toggle(x, y);
		repaint();
	}

	/**
	 * Set cell state from click location, only for valid coordinates within valid grid
	 * @param e: event
	 */
	private void setCell(MouseEvent e) {
		int x = e.getPoint().y / BLOCK_SIZE - 1;
		int y = e.getPoint().x / BLOCK_SIZE - 1;
		//System.out.printf("MouseEvent (%d, %d)\n", x, y);
		if ((x >= 0) && (y >= 0) && (x < this.dimensions.height) && (y < this.dimensions.width)) {
			toggleCell(x, y);
		}
	}

	/**
	 * Paint horizontal and vertical lines to compose a grid
	 * @param g: Graphics context
	 */
	private void paintGrid(Graphics g) {
		g.setColor(GameGrid.gridColor);
		for (int i = 0; i <= this.dimensions.width; ++i) {
			g.drawLine(((i * BLOCK_SIZE) + BLOCK_SIZE), BLOCK_SIZE, (i * BLOCK_SIZE) + BLOCK_SIZE,
					BLOCK_SIZE + (BLOCK_SIZE * this.dimensions.height));
		}
		for (int i = 0; i <= this.dimensions.height; ++i) {
			g.drawLine(BLOCK_SIZE, ((i * BLOCK_SIZE) + BLOCK_SIZE), BLOCK_SIZE * (this.dimensions.width + 1),
					((i * BLOCK_SIZE) + BLOCK_SIZE));
		}
	}

	/**
	 * Paint all live cells based on default color and block size
	 * @param g: Graphics context
	 */
	private void paintAliveCells(Graphics g) {
		try {
			g.setColor(GameGrid.cellColor);
			ArrayList<Point> points = this.cells.getAlivePoints();
			for (Point c : points) {
				g.fillRoundRect(BLOCK_SIZE + (BLOCK_SIZE * c.y), BLOCK_SIZE + (BLOCK_SIZE * c.x),
						BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
			}
		} catch (ConcurrentModificationException ex) {
			ex.printStackTrace();
		}
	}
	
	public void outputCells() {
		if (this.consoleOutput) {
			char[] charsTop = new char[this.dimensions.width + 4];
			char[] charsBottom = new char[this.dimensions.width + 4];
			Arrays.fill(charsTop, '_');
			Arrays.fill(charsBottom, '-');
			String st = new String(charsTop);
			String sb = new String(charsBottom);
	
			//reset terminal/console for continues viewing...
			System.out.print("\033[H\033[2J");
			System.out.print("Conway's Game of Life\n");
					
			int curColumn = 0;
			System.out.printf("%s\n", st);
			boolean printStartBar = true;
			for (Cell cell: this.cells.getCells()) {
				if (printStartBar) {
					System.out.print("| ");
					printStartBar = false;
				}
				if (cell.isAlive()) {
					System.out.print("0");
				} else {
					System.out.print(".");
				}
				curColumn++;
				if (curColumn >= this.dimensions.width) {
					curColumn = 0;
					System.out.print(" |\r\n");
					printStartBar = true;
				}
			}
			System.out.printf("%s\n", sb);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * Callback so that the grid and cells have a chance to paint
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintAliveCells(g);
		paintGrid(g);
	}

	/**
	 * Helper function to calculate dimensions based on window width and height
	 */
	private void calculateDimensions() {
		this.dimensions = new Dimension(getWidth() / BLOCK_SIZE - 2, getHeight() / BLOCK_SIZE - 2);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 * This event is triggered when the window is resized
	 * It will call engine resize so that the cell grids can be reconfigured, 
	 * including copying any existing cell data
	 */
	@Override
	public void componentResized(ComponentEvent e) {
		calculateDimensions();
		//System.out.printf("componentResized width=%d height=%d \n", this.dimensions.width, this.dimensions.height);
		this.cells.resize(this.dimensions.width, this.dimensions.height);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 * This event will cause the cell to toggle state
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (!this.running) {
			setCell(e);
		}
	}

	// Unused mouse events
	
	@Override
	public void componentMoved(ComponentEvent e) {
		/* unused */ }

	@Override
	public void componentShown(ComponentEvent e) {
		/* unused */ }

	@Override
	public void componentHidden(ComponentEvent e) {
		/* unused */ }

	@Override
	public void mouseClicked(MouseEvent e) {
		/* unused */ }

	@Override
	public void mousePressed(MouseEvent e) {
		/* unused */ }

	@Override
	public void mouseEntered(MouseEvent e) {
		/* unused */ }

	@Override
	public void mouseExited(MouseEvent e) {
		/* unused */ }
}
