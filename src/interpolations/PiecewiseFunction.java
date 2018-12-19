package interpolations;

import search.Binary;

//TODO probably broken
public class PiecewiseFunction
{
	double[] piecewiseX;
	double[] piecewiseY;
	double[] slopes;
	
	public PiecewiseFunction(double[] piecewiseX, double[] piecewiseY)
	{
		this.piecewiseX = piecewiseX;
		this.piecewiseY = piecewiseY;
		slopes = new double[piecewiseX.length - 1];
		for (int i = 0; i < piecewiseX.length - 1; i++)
		{
			slopes[i] = Piecewise.slope(piecewiseX[i], piecewiseX[i + 1], piecewiseY[i], piecewiseY[i + 1]);
		}
	}
	
	public double getValue(double key)
	{
		if (key < piecewiseX[0] || key > piecewiseX[piecewiseX.length - 1]) throw new IllegalArgumentException("Trying to get a value from outside piecewise function's domain.");		
		int index = Binary.indicesOfInterval(piecewiseX, key)[0];
		
		/* Which slope to use? */
		return Piecewise.linearInterpolation(key, piecewiseX[index], piecewiseY[index], slopes[index]);
	}
}