package netCDF;

import java.io.IOException;

import arrays.Transpose;
import ucar.nc2.NetcdfFile;

public class NetCDFUtils {
	
	/** Get a 2D array of ints from a netCDF variable.
	 * 
	 * @param ncfile
	 * @param variable
	 * @param transpose
	 * @return
	 */
	public static int[][] get2DIntArray(NetcdfFile ncfile, String variable, boolean transpose)
	{
		try {
			
			int[][] out = (int[][]) ncfile.findVariable (variable).read().copyToNDJavaArray();
			
			if (transpose)
			return Transpose.transpose(out);
			else return out; 
		} catch (IOException e) {
			throw new IllegalArgumentException("Problem finding variable " + variable + " in the netcdf file.");
		}
	}
	
	/** Get a 2D array of double sfrom a netCDF variable.
	 * 
	 * @param ncfile
	 * @param variable
	 * @param transpose
	 * @return
	 */
	public static double[][] get2DDoubleArray(NetcdfFile ncfile, String variable, boolean transpose)
	{
		try {
			double[][] out = (double[][]) ncfile.findVariable (variable).read().copyToNDJavaArray();
			if (transpose)
				return Transpose.transpose(out);
			else return out; 
		} catch (IOException e) {
			throw new IllegalArgumentException("Problem finding variable " + variable + " in the netcdf file.");
		}
	}

}
