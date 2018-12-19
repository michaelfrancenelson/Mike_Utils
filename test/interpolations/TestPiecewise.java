package interpolations;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestPiecewise {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void TestLinearInterpolation_and_Slope()
	{
		double x1 = 0;
		double x2 = 2;
		double y1 = 0;
		double y2 = 3;
		double x = 0.5;

		double slope = Piecewise.slope(x1, x2, y1, y2);
		assertEquals(Piecewise.linearInterpolation(x, x1, y1, slope),
				Piecewise.linearInterpolation(x, x2, y2, slope), 0.0000001);

		assertEquals(Piecewise.linearInterpolation(x, x1, x2, y1, y2), 0.75, 0.00000001);
		
		x1 = 2; x2 = 0;
		assertEquals(Piecewise.linearInterpolation(x, x1, x2, y1, y2), 3 * 0.75, 0.00000001);
	}

	@Test
	public void testPiecewiseLinearInterpolation()
	{
		double[] xPts = new double[] {0d, 1d, 2d, 3d};
		double[] yPts = new double[] {0d, 1d, 3d, 6d};
		double x = 0.5;
		
		double expected = 0.5;
		double tol = 0.00000001;
		assertEquals(Piecewise.piecewiseLinearInterpolation(x, xPts, yPts), expected, tol);
		
		x = 1d;	expected = 1d; 
		assertEquals(Piecewise.piecewiseLinearInterpolation(x, xPts, yPts), expected, tol);
		
		x = 1.5; expected = 2d;
		assertEquals(Piecewise.piecewiseLinearInterpolation(x, xPts, yPts), expected, tol);
		
		x = 2.75; expected = 3d + (3d / 4d) * 3d;
		assertEquals(Piecewise.piecewiseLinearInterpolation(x, xPts, yPts), expected, tol);
		
		/* Test for correct exception thrown: */
		x = 10; 
		thrown.expect(IllegalArgumentException.class);
		Piecewise.piecewiseLinearInterpolation(x, xPts, yPts);
	}
	
	
}
