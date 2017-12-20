import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

/**
 * @author Kevin Guerra kevingnet@gmail.com
 * Main class, uses swing gui components, a resizable grid for all the cells, and a menu
 */
public class GameOfLife extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1529468642113612216L;
	// Size is roughly the grid dimension 8x6, one invisible cell at each edge
	// plus extra padding equal to the size of a cell
	private static final int BLOCK_SIZE = 14;
	private static final int WIDTH_PADDING = 20;
	private static final int HEIGHT_PADDING = 60;
	private static final Dimension WINDOW_SIZE = new Dimension(160, 170);

	private JMenuBar menuBar;
	private JMenu menuGame;
	private JMenu menuBoard;
	private JMenu menuOptions;
	private JMenuItem menuStep;
	private JMenuItem menuRandomFill;
	private JMenuItem menuClear;
	private JMenuItem menuPlay;
	private JMenuItem menuStop;
	private JMenuItem menuInput;
	private JMenuItem menuSetSpeed;
	private GameGrid gameGrid;

	//game engine interface
	private Cells cells;
	//thread for automatic continues stepping
	private Thread gameThread;

	/**
	 * Build the main window using a JFrame
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.print("Initializing game of life\n");
		JFrame gameWindow = new GameOfLife();
		gameWindow.setTitle("Game of Life");
		// Center window location
		gameWindow.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - gameWindow.getWidth()) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - gameWindow.getHeight()) / 2);
		gameWindow.setSize(WINDOW_SIZE);
		gameWindow.setMinimumSize(WINDOW_SIZE);
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameWindow.setVisible(true);

		//rudimentary cmd ln processing, take the whole string and treat it as cell data to be processed...
		//TODO: better handling and input validation, also handle the casting thing
    for (String s: args) {
    	((GameOfLife) gameWindow).processInput(s);
    }		
	}

	private void resizeWindow(int columns, int rows) {
		int width = ((columns + 2) * BLOCK_SIZE) + WIDTH_PADDING;
		int height = ((rows + 2) * BLOCK_SIZE) + HEIGHT_PADDING;
		Dimension windowSize = new Dimension(width, height);
		setSize(windowSize);
		gameGrid.setSize(windowSize);
		gameGrid.repaint();
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2);
	}
	
	/**
	 * Build menu and game grid
	 */
	public GameOfLife() {
		buildMenu();
		this.cells = new CellsImpl();
		this.gameGrid = new GameGrid(this.cells);
		add(this.gameGrid);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * Reacts to menu selections and runs appropriate command using the game engine
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.menuPlay)) {
			System.out.print("Playing...\n");
			play();
		} else if (e.getSource().equals(this.menuStop)) {
			System.out.print("Stopping...\n");
			stop();
		} else if (e.getSource().equals(this.menuRandomFill)) {
			System.out.print("Random fill grid\n");
			randomFill();
		} else if (e.getSource().equals(this.menuClear)) {
			System.out.print("Clear grid\n");
			clear();
		} else if (e.getSource().equals(this.menuInput)) {
			System.out.print("Input Cells\n");
			input();
		} else if (e.getSource().equals(this.menuSetSpeed)) {
			System.out.print("Set Speed\n");
			setSpeed();
		} else {
			System.out.print("Step one time\n");
			step();
		}
	}

	/*
	 * NOTE: In java it is discouraged to use thread: stop, continue because it
	 * causes dead lock issues under certain conditions, this is why we create a
	 * new thread each time Play is run.
	 */

	private void play() {
		this.menuPlay.setEnabled(false);
		this.menuStop.setEnabled(true);
		this.menuStep.setEnabled(false);
		this.gameThread = new Thread(this.gameGrid);
		this.gameThread.start();
	}

	private void stop() {
		this.menuPlay.setEnabled(true);
		this.menuStop.setEnabled(false);
		this.menuStep.setEnabled(true);
		this.gameThread.interrupt();
	}

	private void step() {
		this.gameGrid.step();
	}

	private void randomFill() {
		this.gameGrid.randomFill();
	}

	private void setSpeed() {
		String currentSpeed = this.gameGrid.getSpeed();
		String speed = JOptionPane.showInputDialog(
				"Please input desired speed as an integer which divides a second as the number of steps n", currentSpeed);
		if (speed != null) {
			this.gameGrid.setSpeed(Integer.parseInt(speed));
		}
	}

	private void clear() {
		this.gameGrid.clear();
	}

	private void input() {
		String cells = JOptionPane.showInputDialog(
				"Please input cells i.e.\n ...\n.0.\n.0.\n String of periods and zeroes separated by n");
		if (cells != null) {
			processInput(cells);
		}
	}
	
	private void processInput(String cells) {
		String lines[] = cells.split("n");
		int maxLength = 0;
		for (String line : lines) {
			if (line.length() > maxLength) {
				maxLength = line.length();
			}
		}
		char[] charsTop = new char[maxLength + 4];
		char[] charsBottom = new char[maxLength + 4];
		Arrays.fill(charsTop, '_');
		Arrays.fill(charsBottom, '-');
		String st = new String(charsTop);
		String sb = new String(charsBottom);
		
		System.out.printf("%s\n", st);
		for (String line : lines) {
			System.out.printf("| %s |\n", line);
		}
		System.out.printf("%s\n", sb);
		
		gameGrid.setVisible(false);
		resizeWindow(maxLength, lines.length);
		gameGrid.setVisible(true);
		this.gameGrid.populate(lines);
	}

	private void buildMenu() {
		this.menuBar = new JMenuBar();
		setJMenuBar(this.menuBar);
		this.menuPlay = new JMenuItem("Play");
		this.menuPlay.addActionListener(this);
		this.menuStop = new JMenuItem("Stop");
		this.menuStop.addActionListener(this);
		this.menuStop.setEnabled(false);
		this.menuStep = new JMenuItem("Step");
		this.menuStep.addActionListener(this);
		this.menuRandomFill = new JMenuItem("Ramdom fill");
		this.menuRandomFill.addActionListener(this);
		this.menuClear = new JMenuItem("Clear");
		this.menuClear.addActionListener(this);
		this.menuSetSpeed = new JMenuItem("Set Speed");
		this.menuSetSpeed.addActionListener(this);
		this.menuInput = new JMenuItem("Input Cells");
		this.menuInput.addActionListener(this);
		this.menuGame = new JMenu("Game");
		this.menuBoard = new JMenu("Board");
		this.menuOptions = new JMenu("Options");
		this.menuGame.add(this.menuStep);
		this.menuGame.add(new JSeparator());
		this.menuGame.add(this.menuPlay);
		this.menuGame.add(this.menuStop);
		this.menuBoard.add(this.menuRandomFill);
		this.menuBoard.add(new JSeparator());
		this.menuBoard.add(this.menuClear);
		this.menuOptions.add(this.menuInput);
		this.menuOptions.add(this.menuSetSpeed);
		this.menuBar.add(this.menuGame);
		this.menuBar.add(this.menuBoard);
		this.menuBar.add(this.menuOptions);
	}
}
