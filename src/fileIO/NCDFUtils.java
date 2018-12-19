package fileIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.Variable;

public class NCDFUtils {

	public static void save2DIntArrayToFile(int[][] data, String filename, String varName)
	{
		
		int nRow = data.length;
		int nCol = data[0].length;
		
		int[] cols = new int[nCol]; for(int i = 0; i < nCol; i++) cols[i] = i;
		int[] rows = new int[nRow];	for(int i = 0; i < nRow; i++) rows[i] = i;
		
		NetcdfFileWriter dataFile = null;
		try {
			dataFile = NetcdfFileWriter.createNew(NetcdfFileWriter.Version.netcdf4, filename);
		
			/* Set up the dimensions. */
			Dimension rowCoordinateDimension = dataFile.addDimension(null, "row", nRow);
			Dimension columnCoordinateDimension = dataFile.addDimension(null, "column", nCol);
			
			Variable rowCoordinateVariable = dataFile.addVariable(null, "row", DataType.INT, "row");
			Variable columnCoordinateVariable = dataFile.addVariable(null, "column", DataType.INT, "column");
			
			List<Dimension> dims = new ArrayList<Dimension>();
			dims.add(columnCoordinateDimension);
			dims.add(rowCoordinateDimension);
			
			Variable var = dataFile.addVariable(null, varName, DataType.INT, dims);
			
			dataFile.create();
			
			try {
				dataFile.write(columnCoordinateVariable, Array.factory(cols));
				dataFile.write(rowCoordinateVariable, Array.factory(rows));
				dataFile.write(var, Array.factory(data));
				dataFile.close();
			} catch (InvalidRangeException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void save2DDoubleArrayToFile(double[][] data, String filename, String varName)
	{
		
		int nRow = data.length;
		int nCol = data[0].length;
		
		int[] cols = new int[nCol]; for(int i = 0; i < nCol; i++) cols[i] = i;
		int[] rows = new int[nRow];	for(int i = 0; i < nRow; i++) rows[i] = i;
		
		NetcdfFileWriter dataFile = null;
		try {
			dataFile = NetcdfFileWriter.createNew(NetcdfFileWriter.Version.netcdf3, filename);
		
			/* Set up the dimensions. */
			Dimension rowCoordinateDimension = dataFile.addDimension(null, "row", nRow);
			Dimension columnCoordinateDimension = dataFile.addDimension(null, "column", nCol);
			
			Variable rowCoordinateVariable = dataFile.addVariable(null, "row", DataType.INT, "row");
			Variable columnCoordinateVariable = dataFile.addVariable(null, "column", DataType.DOUBLE, "column");
			
			List<Dimension> dims = new ArrayList<Dimension>();
			dims.add(rowCoordinateDimension);
			dims.add(columnCoordinateDimension);
			
			Variable var = dataFile.addVariable(null, varName, DataType.DOUBLE, dims);
			
			dataFile.create();
			
			try {
				dataFile.write(columnCoordinateVariable, Array.factory(cols));
				dataFile.write(rowCoordinateVariable, Array.factory(rows));
				dataFile.write(var, Array.factory(data));
				dataFile.close();
			} catch (InvalidRangeException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
