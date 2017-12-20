import java.awt.Point;
import java.util.ArrayList;

/**
 * @author Kevin Guerra kevingnet@gmail.com
 * Class to hold cell information such as location, state and neighbors
 */
public class Cell {
	//immutable location
	final private Point location;
	private CellState state = CellState.DEAD;
	//avoid null pointer, though it will later be set
	private ArrayList<Point> neighbors = new ArrayList<>(0);

	Cell(Point location) {
		this.location = new Point(location);
		this.state = CellState.DEAD;
	}

	Cell(Point location, CellState id) {
		this.location = new Point(location);
		this.state = id;
	}

	Cell(Cell cp) {
		this.location = new Point(cp.location);
		this.state = cp.state;
		// we don't deep copy the neighbors, since we just need them as references
		this.neighbors = cp.neighbors;
	}

	ArrayList<Point> getNeighbors() {
		return this.neighbors;
	}

	void setNeighbors(ArrayList<Point> cps) {
		this.neighbors = cps;
	}

	public Point getLocation() {
		return this.location;
	}

	void revive() {
		this.state = CellState.ALIVE;
	}

	void kill() {
		this.state = CellState.DEAD;
	}

	boolean isAlive() {
		return this.state == CellState.ALIVE;
	}

	boolean isDead() {
		return this.state == CellState.DEAD;
	}

	@Override
	public String toString() {
		return this.location.toString() + " state:" + this.state.toString();
	}
}
