import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

/**
 * @author Kevin Guerra kevingnet@gmail.com
 * Cells interface implementation for the GameOfLife engine
 *
 */
public class CellsImpl implements Cells {
	private ArrayList<Cell> cells = new ArrayList<>(0);
	private ArrayList<Cell> cellsBuffer = new ArrayList<>(0);
	private Dimension dimensions = null;
	private int columns = 0;
	private int rows = 0;

	/* (non-Javadoc)
	 * @see Cells#resize(int, int)
	 * Called when the window is resized
	 * save dimensions to make current and recalculate engine cell grid
	 */
	@Override
	public void resize(int c, int r) {
		//System.out.printf("Cells.resize(%d, %d) \n", c, r);
		this.dimensions = new Dimension(c, r);
		recalculate();
	}

	@Override
	public Dimension getDimension() {
		return this.dimensions;
	}

	/* (non-Javadoc)
	 * @see Cells#step()
	 * Calculate next generation of cells
	 * Algorithm:
	 *  - get both cells from the two buffers, both have the exact same location
	 *  - count live neighbors, regardless of state, somewhat optimal depending on state
	 *  - apply game rules: liveCell (2 or 3 liveNeighbors) = live, deadCell (3 liveNeighbors) = live
	 *  - clear cell buffer, by killing all cells
	 *  - swap buffers, readying for next step
	 */
	@Override
	public void step() {
		for (int i = 0; i < this.cells.size(); ++i) {
			Cell cp = this.cells.get(i);
			Cell bcp = this.cellsBuffer.get(i);
			// count neighbor states, location is cached as adjacent cells contain references
			int liveNeighborsCount = getNeighborCount(cp);
			if (cp.isAlive()) {
				if (liveNeighborsCount == 2 || liveNeighborsCount == 3) {
					bcp.revive();
				}
			} else if (cp.isDead()) {
				if (liveNeighborsCount == 3) {
					bcp.revive();
				}
			}
		}
		// clear grid = kill all cells
		// we don't kill them before, since it would cause neighbor miscalculations
		for (Cell cp : this.cells) {
			cp.kill();
		}
		// swap arrays for next iteration
		ArrayList<Cell> tmp = this.cellsBuffer;
		this.cellsBuffer = this.cells;
		this.cells = tmp;
	}

	/**
	 * Recalculate engine cell grid
	 * use double buffer, so as to avoid unnecessary memory allocation and deallocation
	 * Algorithm:
	 *   - save current alive cells into array
	 *   - resize cell buffer and add cells set to dead initially
	 *   - for each cell save neighbor locations
	 *   - clone array, both contain only dead cells
	 *   - copy saved alive cells into cell array
	 *   - save current dimensions
	 *   - restored saved cells into resized array
	 */
	private void recalculate() {
		// save alive cells
		ArrayList<Cell> alive = new ArrayList<>(0);
		for (Cell nc : this.cells) {
			if (nc.isAlive()) {
				alive.add(nc);
			}
		}

		int c = this.dimensions.width;
		int r = this.dimensions.height;
		this.cellsBuffer = new ArrayList<>(c * r);
		// initialize array, identity is set to dead, so first step is recalculated
		Cell cp = null;
		Point p = null;
		for (int i = 0; i < r; ++i) {
			for (int j = 0; j < c; ++j) {
				p = new Point(i, j);
				cp = new Cell(p);
				this.cellsBuffer.add(cp);
			}
		}
		// calculate and set neighbors
		calculateNeighborsStiched();
		
		//clone cell array
		this.cells = new ArrayList<>(c * r);
		for (Cell bcp : this.cellsBuffer) {
			this.cells.add(new Cell(bcp));
		}

		this.columns = c;
		this.rows = r;
		// copy old to current
		for (Cell ocp : alive) {
			Point op = ocp.getLocation();
			cp = this.cells.get((op.x * this.columns) + op.y);
			cp.revive();
		}
	}
	
	//allows teleporting cells from one edge to the opposite one
	private void calculateNeighborsStiched() {
		int c = this.dimensions.width;
		int r = this.dimensions.height;
		int col = 0;
		int row = 0;
		for (Cell ncp : this.cellsBuffer) {
			Point np = ncp.getLocation();
			int i = np.x;
			int j = np.y;

			ArrayList<Point> neighbors = new ArrayList<>(8);
			// go around the cell...
			// top
			row = i - 1;
			col = j;
			if (row < 0) {
				row = r - 1;
			}
			addNeighbor(ncp, row, col, c, neighbors);
			// bottom
			row = i + 1;
			col = j;
			if (row >= r) {
				row = 0;
			}
			addNeighbor(ncp, row, col, c, neighbors);
			// top left
			row = i - 1;
			col = j - 1;
			if (row < 0) {
				row = r - 1;
			}
			if (col < 0) {
				col = c - 1;
			}
			addNeighbor(ncp, row, col, c, neighbors);
			// top right
			row = i - 1;
			col = j + 1;
			if (row < 0) {
				row = r - 1;
			}
			if (col >= c) {
				col = 0;
			}
			addNeighbor(ncp, row, col, c, neighbors);
			// bottom left
			row = i + 1;
			col = j - 1;
			if (row >= r) {
				row = 0;
			}
			if (col < 0) {
				col = c - 1;
			}
			addNeighbor(ncp, row, col, c, neighbors);
			// bottom right
			row = i + 1;
			col = j + 1;
			if (row >= r) {
				row = 0;
			}
			if (col >= c) {
				col = 0;
			}
			addNeighbor(ncp, row, col, c, neighbors);
			// left
			row = i;
			col = j - 1;
			if (col < 0) {
				col = c - 1;
			}
			addNeighbor(ncp, row, col, c, neighbors);
			// right
			row = i;
			col = j + 1;
			if (col >= c) {
				col = 0;
			}
			addNeighbor(ncp, row, col, c, neighbors);
			ncp.setNeighbors(neighbors);
		}
	}

	//cells are confined to the grid without teleporting capability
	private void calculateNeighbors() {
		int c = this.dimensions.width;
		int r = this.dimensions.height;
		int col = 0;
		int row = 0;
		for (Cell ncp : this.cellsBuffer) {
			Point np = ncp.getLocation();
			int i = np.x;
			int j = np.y;

			ArrayList<Point> neighbors = new ArrayList<>(8);
			// go around the cell...
			// top
			row = i - 1;
			col = j;
			if (row >= 0) {
				addNeighbor(ncp, row, col, c, neighbors);
			}
			// bottom
			row = i + 1;
			col = j;
			if (row < r) {
				addNeighbor(ncp, row, col, c, neighbors);
			}
			// top left
			row = i - 1;
			col = j - 1;
			if (col >= 0 && row >= 0) {
				addNeighbor(ncp, row, col, c, neighbors);
			}
			// top right
			row = i - 1;
			col = j + 1;
			if (col < c && row >= 0) {
				addNeighbor(ncp, row, col, c, neighbors);
			}
			// bottom left
			row = i + 1;
			col = j - 1;
			if (col >= 0 && row < r) {
				addNeighbor(ncp, row, col, c, neighbors);
			}
			// bottom right
			row = i + 1;
			col = j + 1;
			if (col < c && row < r) {
				addNeighbor(ncp, row, col, c, neighbors);
			}
			// left
			row = i;
			col = j - 1;
			if (col >= 0) {
				addNeighbor(ncp, row, col, c, neighbors);
			}
			// right
			row = i;
			col = j + 1;
			if (col < c) {
				addNeighbor(ncp, row, col, c, neighbors);
			}
			ncp.setNeighbors(neighbors);
		}
	}
	
	private void addNeighbor(Cell ncp, int row, int col, int width, ArrayList<Point> neighbors) {
		Cell cp = this.cellsBuffer.get((row * width) + col);
		Point np = ncp.getLocation();
		np = cp.getLocation();
		neighbors.add(np);
	}

	/**
	 * Count the number of neighbors that are alive
	 * we use two separate loops depending on state, since for dead cells
	 * we attempt to count the max, whereas for live cells we only need to
	 * know up to four
	 * TODO: optimize by caching or memoizing the counting operation
	 * @param cell: cell in the center of group
	 * @return count of alive neighbors
	 */
	private int getNeighborCount(Cell cell) {
		ArrayList<Point> neighbors = cell.getNeighbors();
		int liveNeighborsCount = 0;
		if (cell.isAlive()) {
			for (Point np : neighbors) {
				Cell ncp = this.cells.get((np.x * this.columns) + np.y);
				if (ncp.isAlive()) {
					liveNeighborsCount++;
					if (liveNeighborsCount == 4) {
						break;
					}
				}
			}
		} else if (cell.isDead()) {
			for (Point np : neighbors) {
				Cell ncp = this.cells.get((np.x * this.columns) + np.y);
				if (ncp.isAlive()) {
					liveNeighborsCount++;
				}
			}
		}
		return liveNeighborsCount;
	}

	/* (non-Javadoc)
	 * @see Cells#toggle(int, int)
	 * Flip cell state
	 */
	@Override
	public void toggle(int x, int y) {
		if (x >= 0 && y >= 0 && x < this.rows && y < this.columns) {
			Cell cp = this.cells.get((x * this.columns) + y);
			if (cp.isDead()) {
				cp.revive();
			} else if (cp.isAlive()) {
				cp.kill();
			}
		} else {
			System.out.print("Cells.toggle invalid coordinate!\n");
		}
	}

	@Override
	public void clear() {
		this.cells.clear();
		this.cellsBuffer.clear();
		this.cells =  new ArrayList<>(0);
		this.cellsBuffer =  new ArrayList<>(0);
		recalculate();
	}

	/* (non-Javadoc)
	 * @see Cells#clear(int, int)
	 * Clear cell 
	 */
	@Override
	public void clear(int x, int y) {
		if (x >= 0 && y >= 0 && x < this.rows && y < this.columns) {
			Cell cp = this.cells.get((x * this.columns) + y);
			cp.kill();
		} else {
			System.out.print("Cells.clear invalid coordinate!\n");
		}
	}

	/* (non-Javadoc)
	 * @see Cells#getState(int, int)
	 * Get cell state
	 */
	@Override
	public boolean getState(int x, int y) {
		if (x >= 0 && y >= 0 && x < this.rows && y < this.columns) {
			Cell cp = this.cells.get((x * this.columns) + y);
			return (cp.isAlive());
		} else {
			System.out.print("Cells.getState invalid coordinate!\n");
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see Cells#get()
	 * Build array with live cell points so cells can be drawn
	 */
	@Override
	public ArrayList<Point> getAlivePoints() {
		ArrayList<Point> points = new ArrayList<>(0);
		for (Cell cp : this.cells) {
			if (cp.isAlive()) {
				points.add(cp.getLocation());
			}
		}
		return points;
	}
	
	/* (non-Javadoc)
	 * @see Cells#get()
	 * Return cells
	 */
	@Override
	public ArrayList<Cell> getCells() {
		return this.cells;
	}
}
