package coordinates;

public class CoordTransformations {

	
	/** Wrap 2D coordinates, creating a torus.
	 * 
	 * @param inputCoords index 0 = input row coordinate, index 1 = input column coordinate
	 * @param nRow number of rows in the 2D world
	 * @param nCol number of columns in the 2D world
	 * @return coordinates wrapped from beyond the edges back onto the grid.
	 */
	public static int[] wrap2DCoordinates(int[] inputCoords, int nRow, int nCol)
	{
		return wrap2DCoordinates(inputCoords[0], inputCoords[1], nRow, nCol);
	}
	
	/** Wrap 2D coordinates, creating a torus.
	 * 
	 * @param inputCoords index 0 = input row coordinate, index 1 = input column coordinate
	 * @param nRow number of rows in the 2D world
	 * @param nCol number of columns in the 2D world
	 * @return coordinates wrapped from beyond the edges back onto the grid.
	 */
	public static int[] wrap2DCoordinates(int row, int col, int nRow, int nCol)
	{
		int rowOut = (row + nRow) % nRow;
		int columnOut = (col + nCol) % nCol;
		return new int[] {rowOut, columnOut};
	}
	
	/** Convert a set of 2D coordinates to a cellID given the height of the grid.
	 * @param coords int array1a: index 0 = column, index 1 = row
	 * @param params
	 * @return cellID	 */
	public static int coordsTo1DIndex(int[] coords, int nColumns){
		return coordsTo1DIndex(coords[0], coords[1], nColumns);
	}
	
	/** Convert a set of 2D coordinates to a cellID given the height of the grid.
	 * @param column
	 * @param row
	 * @param params
	 * @return cellID */
	public static int coordsTo1DIndex(int row, int column, int nColumns){
		return row * nColumns + column;
	}
	
	public static int[] coordsFrom1DIndex(int index, int nColumns)
	{
		int[] out = new int[2];
		out[1] = index % nColumns;
		out[0] = index / nColumns;
		return out;
	}
	
}
