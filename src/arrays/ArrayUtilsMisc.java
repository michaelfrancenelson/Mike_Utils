package arrays;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ArrayUtilsMisc 
{

	public static <T> double[][] calculteDoubleArray(T[][] arr, Method m, double naVal, Object... args)
	{
		int width = arr.length;
		int height = arr[0].length;
		double[][] out = new double[width][height];	    
		for (int x = 0; x < width; x++) for (int y = 0; y < height; y++)
			try {
				T c = arr[x][y];
				if (c != null)
				{
					
				double val;
				if (m.getParameterCount() > 0) 
					val = (double) m.invoke(c, args);
				else val = (double) m.invoke(c);
				out[x][y] = val;
				}
				else out[x][y] = naVal;
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		return out;
	}

	public static <T> int[][] calculteIntArray(T[][] arr, Method m, int naVal, Object... args)
	{
		int width = arr.length;
		int height = arr[0].length;
		int[][] out = new int[width][height];	    
		for (int x = 0; x < width; x++) for (int y = 0; y < height; y++)
			try {
				T c = arr[x][y];
				if (c != null)
				{
					int val;
					if (m.getParameterCount() > 0) 
						val = (int) m.invoke(c, args);
					else val = (int) m.invoke(c);
					out[x][y] = val;
				}
				else out[x][y] = naVal;
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		return out;
	}
}
