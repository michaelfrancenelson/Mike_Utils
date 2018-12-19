package interpolations;

import search.Binary;

public class Piecewise {

	/** Get the value of y corresponding to an input x given two coordinates, assuming a linear function.
	 * 
	 * @param x value of x at which to calculate the unknown
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @return
	 */
	public static double linearInterpolation(double x, double x1, double x2, double y1, double y2)
	{
		if(x2 == x1) throw new IllegalArgumentException("The two points may not have the same x-coordinate.");
		double slope = slope(x1, x2, y1, y2);
		return linearInterpolation(x, x1, y1, slope);
	}
	
	public static double linearInterpolation(double x, double x1, double y1, double slope)
	{
		return slope * (x - x1) + y1;
	}
	
	public static double piecewiseLinearInterpolation(double x, double[] xPts, double[] yPts)
	{
		if (x < xPts[0] || x > xPts[xPts.length - 1]) throw new IllegalArgumentException("Requested point is outside the range of the piecewise function.");
		/* Determine which subinterval the point lies on: */
		int index = Binary.indexOfLessThanKey(xPts, x) - 1;
		return linearInterpolation(x, xPts[index], xPts[index + 1], yPts[index], yPts[index + 1]);
	}
	
	public static double slope(double x1, double x2, double y1, double y2)
	{
		if(x2 == x1) throw new IllegalArgumentException("The two points may not have the same x-coordinate.");
		return (y2 - y1) / (x2 - x1);
	}
	
}



