import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Kevin Guerra kevingnet@gmail.com
 *
 */
public class CellsTest {
	private static Cells cells;
	private static String[] lines;
	private static int columns = 8;
	private static int rows = 6;
	private static int columnsLarge = 14;
	private static int rowsLarge = 12;

	private static String[] linesEmpty;
	private static String[] linesWalker;
	private static String[] linesWalker2;
	private static String[] linesBlock;
	private static String[] linesBlock2;
	
	private static String[] linesLargeEmpty;
	private static String[] linesLargeWalker;
	private static String[] linesLargeWalker2;
	private static String[] linesLargeBlock;
	private static String[] linesLargeBlock2;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.print("Creating cells object\n");
		cells = new CellsImpl();
		
		lines = new String[rows];
		lines[0] = "...0....";
		lines[1] = "....0...";
		lines[2] = "..000...";
		lines[3] = "........";
		lines[4] = "........";
		lines[5] = "........";
	
		linesEmpty = new String[rows];
		linesWalker = new String[rows];
		linesWalker2 = new String[rows];
		linesBlock = new String[rows];
		linesBlock2 = new String[rows];
		
		linesLargeEmpty = new String[rowsLarge];
		linesLargeWalker = new String[rowsLarge];
		linesLargeWalker2 = new String[rowsLarge];
		linesLargeBlock = new String[rowsLarge];
		linesLargeBlock2 = new String[rowsLarge];
		
		linesEmpty[0] = "........";
		linesEmpty[1] = "........";
		linesEmpty[2] = "........";
		linesEmpty[3] = "........";
		linesEmpty[4] = "........";
		linesEmpty[5] = "........";
		
		linesWalker[0] = "...0....";
		linesWalker[1] = "....0...";
		linesWalker[2] = "..000...";
		linesWalker[3] = "........";
		linesWalker[4] = "........";
		linesWalker[5] = "........";

		linesWalker2[0] = "........";
		linesWalker2[1] = "..0.0...";
		linesWalker2[2] = "...00...";
		linesWalker2[3] = "...0....";
		linesWalker2[4] = "........";
		linesWalker2[5] = "........";

		linesBlock[0] = "........";
		linesBlock[1] = "..00....";
		linesBlock[2] = "..00....";
		linesBlock[3] = "........";
		linesBlock[4] = "........";
		linesBlock[5] = "........";
		
		linesBlock2[0] = "........";
		linesBlock2[1] = "..00....";
		linesBlock2[2] = "..00....";
		linesBlock2[3] = "........";
		linesBlock2[4] = "........";
		linesBlock2[5] = "........";
		
		
		linesLargeEmpty[0] = "..............";
		linesLargeEmpty[1] = "..............";
		linesLargeEmpty[2] = "..............";
		linesLargeEmpty[3] = "..............";
		linesLargeEmpty[4] = "..............";
		linesLargeEmpty[5] = "..............";
		linesLargeEmpty[6] = "..............";
		linesLargeEmpty[7] = "..............";
		linesLargeEmpty[8] = "..............";
		linesLargeEmpty[9] = "..............";
		linesLargeEmpty[10] = "..............";
		linesLargeEmpty[11] = "..............";
		
		linesLargeWalker[0] = "...0..........";
		linesLargeWalker[1] = "....0.........";
		linesLargeWalker[2] = "..000.........";
		linesLargeWalker[3] = "..............";
		linesLargeWalker[4] = "..............";
		linesLargeWalker[5] = "..............";
		linesLargeWalker[6] = "..............";
		linesLargeWalker[7] = "..............";
		linesLargeWalker[8] = "..............";
		linesLargeWalker[9] = "..............";
		linesLargeWalker[10] = "..............";
		linesLargeWalker[11] = "..............";

		linesLargeWalker2[0] = "..............";
		linesLargeWalker2[1] = "..0.0.........";
		linesLargeWalker2[2] = "...00.........";
		linesLargeWalker2[3] = "...0..........";
		linesLargeWalker2[4] = "..............";
		linesLargeWalker2[5] = "..............";
		linesLargeWalker2[6] = "..............";
		linesLargeWalker2[7] = "..............";
		linesLargeWalker2[8] = "..............";
		linesLargeWalker2[9] = "..............";
		linesLargeWalker2[10] = "..............";
		linesLargeWalker2[11] = "..............";

		linesLargeBlock[0] = "..............";
		linesLargeBlock[1] = "..00..........";
		linesLargeBlock[2] = "..00..........";
		linesLargeBlock[3] = "..............";
		linesLargeBlock[4] = "..............";
		linesLargeBlock[5] = "..............";
		linesLargeBlock[6] = "..............";
		linesLargeBlock[7] = "..............";
		linesLargeBlock[8] = "..............";
		linesLargeBlock[9] = "..............";
		linesLargeBlock[10] = "..............";
		linesLargeBlock[11] = "..............";
		
		linesLargeBlock2[0] = "..............";
		linesLargeBlock2[1] = "..00..........";
		linesLargeBlock2[2] = "..00..........";
		linesLargeBlock2[3] = "..............";
		linesLargeBlock2[4] = "..............";
		linesLargeBlock2[5] = "..............";
		linesLargeBlock2[6] = "..............";
		linesLargeBlock2[7] = "..............";
		linesLargeBlock2[8] = "..............";
		linesLargeBlock2[9] = "..............";
		linesLargeBlock2[10] = "..............";
		linesLargeBlock2[11] = "..............";
		
		cells.resize(columns, rows);
	}

	public static void populate(String[] lines) {
		int i = 0;
		int j = 0;
		for (String line : lines) {
			char[] chars = line.toCharArray();
			for (char c : chars) {
				if ( c == '0') {
					cells.toggle(i, j);
				}
				j++;
			}
			j = 0;
			i++;
		}
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.print("Destroying cells object\n");
		cells = null;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.out.print("\n\nsetUp\n");
		//start with an empty board
		populate(linesEmpty);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		System.out.print("tearDown\n\n");
		//resize to initial size
		cells.resize(columns, rows);
	}

	/**
	 * Test method for {@link Cells#resize(int, int)}.
	 */
	@Test
	public void testResize() {
		System.out.print("testResize\n");
		Dimension dimStart = cells.getDimension();
		System.out.printf("dimStart %s\n", dimStart);
		cells.resize(columnsLarge, rowsLarge);
		Dimension dimEnd = cells.getDimension();
		System.out.printf("dimEnd %s\n", dimEnd);
		cells.resize(columns, rows);
		Dimension dimLast = cells.getDimension();
		System.out.printf("dimLast %s\n", dimLast);
		
		org.junit.Assert.assertNotEquals(dimStart, dimEnd);
		org.junit.Assert.assertEquals(dimStart, dimLast);
		
	}

	/**
	 * Test method for {@link Cells#toggle(int, int)}.
	 */
	@Test
	public void testToggle() {
		System.out.print("testToggle\n");
		int x = 1;
		int y = 2;
		boolean stateStart = cells.getState(x, y);
		System.out.printf("stateStart %s\n", stateStart);
		cells.toggle(x, y);
		boolean stateEnd = cells.getState(x, y);
		System.out.printf("stateEnd %s\n", stateEnd);
		cells.toggle(x, y);
		boolean stateLast = cells.getState(x, y);
		System.out.printf("stateLast %s\n", stateLast);

		org.junit.Assert.assertNotEquals(stateStart, stateEnd);
		org.junit.Assert.assertEquals(stateStart, stateLast);
	}

	/**
	 * Test method for {@link Cells#clear(int, int)}.
	 */
	@Test
	public void testClear() {
		System.out.print("testClear\n");
		populate(linesEmpty);
		ArrayList<Point> pointsStart = cells.getAlivePoints();
		System.out.printf("pointsStart size %d\n", pointsStart.size());
		populate(linesBlock);
		ArrayList<Point> pointsEnd = cells.getAlivePoints();
		System.out.printf("pointsEnd size %d\n", pointsEnd.size());

		cells.clear(1, 2);
		cells.clear(1, 3);
		cells.clear(2, 2);
		cells.clear(2, 3);
		ArrayList<Point> pointsLast = cells.getAlivePoints();
		System.out.printf("pointsLast size %d\n", pointsLast.size());
		
		org.junit.Assert.assertNotEquals(pointsEnd.size(), 0);
		org.junit.Assert.assertEquals(pointsStart.size(), 0);
		org.junit.Assert.assertEquals(pointsLast.size(), 0);
	}

	public void outputCells() {
		Dimension dimensions = cells.getDimension();
		char[] charsTop = new char[dimensions.width + 4];
		char[] charsBottom = new char[dimensions.width + 4];
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
			if (curColumn >= dimensions.width) {
				curColumn = 0;
				System.out.print(" |\r\n");
				printStartBar = true;
			}
		}
		System.out.printf("%s\n", sb);
	}

	/**
	 * Test method for {@link Cells#step()}.
	 */
	@Test
	public void testStepWalker() {
		System.out.print("testStepWalker\n");
		cells.clear();
		populate(linesWalker);
		outputCells();
		boolean c1 = cells.getState(0, 3);
		boolean c2 = cells.getState(1, 4);
		boolean c3 = cells.getState(2, 2);
		boolean c4 = cells.getState(2, 3);
		boolean c5 = cells.getState(2, 4);

		org.junit.Assert.assertTrue(c1); 
		org.junit.Assert.assertTrue(c2); 
		org.junit.Assert.assertTrue(c3); 
		org.junit.Assert.assertTrue(c4); 
		org.junit.Assert.assertTrue(c5); 
		
		cells.step();
		outputCells();
		c1 = cells.getState(1, 2);
		c2 = cells.getState(1, 4);
		c3 = cells.getState(2, 3);
		c4 = cells.getState(2, 4);
		c5 = cells.getState(3, 3);
		
		org.junit.Assert.assertTrue(c1); 
		org.junit.Assert.assertTrue(c2); 
		org.junit.Assert.assertTrue(c3); 
		org.junit.Assert.assertTrue(c4); 
		org.junit.Assert.assertTrue(c5); 
	}

	/**
	 * Test method for {@link Cells#step()}.
	 */
	@Test
	public void testStepBlock() {
		System.out.print("testStepBlock\n");
		
		cells.clear();
		populate(linesBlock);
		
		outputCells();
		cells.step();
		outputCells();
		boolean c1 = cells.getState(1, 2);
		boolean c2 = cells.getState(1, 3);
		boolean c3 = cells.getState(2, 2);
		boolean c4 = cells.getState(2, 3);
		
		org.junit.Assert.assertTrue(c1); 
		org.junit.Assert.assertTrue(c2); 
		org.junit.Assert.assertTrue(c3); 
		org.junit.Assert.assertTrue(c4); 
	}

	/**
	 * Test method for {@link Cells#step()}.
	 */
	@Test
	public void testFastManySteps() {
		cells.clear();
		populate(linesWalker);
		outputCells();
		System.out.print("testFastManySteps\n");
		int iterations = 1000000;
		System.out.println("testFastManySteps steps: " + iterations);
		long startTime = System.currentTimeMillis();
		for (int i=0; i<iterations; ++i) {
			cells.step();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("testFastManySteps execution time: " + (endTime - startTime) + " ms");
	}

}
