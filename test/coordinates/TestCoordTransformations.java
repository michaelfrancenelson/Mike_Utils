package coordinates;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestCoordTransformations {

	
	@Test
	public void testCoordsTo1DIndex()
	{
		int nCol = 12;
		
		int row = 5;
		int col = 5;
		int expected = 65;
		
		assertEquals(CoordTransformations.coordsTo1DIndex(row, col, nCol), expected);
		
		/* increasing the column coord increases the index by 1. */
		col++; expected++;
		assertEquals(CoordTransformations.coordsTo1DIndex(row, col, nCol), expected);
		
		/* increasing the row coord increases the index by nCol */
		row++;
		expected += nCol;
		assertEquals(CoordTransformations.coordsTo1DIndex(row, col, nCol), expected);
	}
	
	
	@Test 
	public void testCoordsFrom1DIndex()
	{
		int nColumns;
		int index;
		int[] coords;
		
		nColumns  = 10;
		index = 5;
		coords = CoordTransformations.coordsFrom1DIndex(index, nColumns);
		assertEquals(0, coords[0]);
		assertEquals(5, coords[1]);
		
		/* Incrementing the index increments the column coord: */
		index++;
		coords = CoordTransformations.coordsFrom1DIndex(index, nColumns);
		assertEquals(0, coords[0]);
		assertEquals(6, coords[1]);
		
		/* Incrementing the index by nColumns increments the row coord: */
		index += nColumns;
		coords = CoordTransformations.coordsFrom1DIndex(index, nColumns);
		assertEquals(1, coords[0]);
		assertEquals(6, coords[1]);
	}
	
	@Test //TODO
	public void testWrap2D() {
//		fail("Not yet implemented");
	}

}
