package coordinates;

public class CoordinateInt2D {

	public int row, column;
	
	public CoordinateInt2D(int row, int col) {this.row = row; this.column = col; }
	
	public double distanceTo(CoordinateInt2D other)
	{
		return distanceTo(other.row, other.column);
	}
	
	public double distanceTo(int row, int col)
	{
		double rowDist = (double)(this.row - row);
		double colDist = (double)(this.column - col);
		return Math.sqrt(Math.pow(rowDist, 2d) + Math.pow(colDist, 2d));		
	}
}