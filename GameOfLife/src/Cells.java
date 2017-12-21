import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

/**
 * @author Kevin Guerra kevingnet@gmail.com
 * Interface to the GameOfLife engine
 */
public interface Cells {

	/**
	 * Resize engine cell grid to window size
	 * @param columns
	 * @param rows
	 */
	void resize(int columns, int rows);

	/**
	 * Get dimensions
	 */
	Dimension getDimension();

	/**
	 * Toggle a cell at given coordinate
	 * @param x
	 * @param y
	 */
	void toggle(int x, int y);

	/**
	 * Get cell state at given coordinate
	 * @param x
	 * @param y
	 */
	boolean getState(int x, int y);

	/**
	 * Clear a cell at given coordinate
	 * @param x
	 * @param y
	 */
	void clear(int x, int y);

	/**
	 * Clear all cells
	 */
	void clear();

	/**
	 * Get point coordinates for all live cells only
	 * @return array of points
	 */
	ArrayList<Point> getAlivePoints();

	/**
	 * Get cells, live or dead
	 * @return array of cells
	 */
	ArrayList<Cell> getCells();

	/**
	 * Step into the next generation of cells
	 */
	void step();

	/**
	 * Toggle teleporting of cells across grid boundaries
	 */
	void toggleTeleport();
}
