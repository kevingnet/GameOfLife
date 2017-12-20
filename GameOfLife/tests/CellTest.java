import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

public class CellTest {
	Point point = new Point(1, 2);
	Cell cell = null;

	@Before
	public void setUp() throws Exception {
		cell = new Cell(point);
	}

	@Test
	public void testCellState() {
		assertTrue("Cell should have been dead", cell.isDead());
	}

	@Test
	public void testGetLocation() {
		Point p = cell.getLocation();
		assertEquals("Cell location didn't match with initialized location", p, point);
	}

	@Test
	public void testRevive() {
		cell.revive();
		assertTrue("Cell should have been alive", cell.isAlive());
	}

	@Test
	public void testKill() {
		cell.kill();
		assertTrue("Cell should have been dead", cell.isDead());
	}

	@Test
	public void testIsAlive() {
		cell.revive();
		assertTrue("Cell should have been alive", cell.isAlive());
	}

	@Test
	public void testIsDead() {
		cell.kill();
		assertTrue("Cell should have been dead", cell.isDead());
	}
}
