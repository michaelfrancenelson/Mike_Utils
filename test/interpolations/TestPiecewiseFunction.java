package interpolations;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestPiecewiseFunction {

	double[] piecewiseX;
	double[] piecewiseY;
	
	PiecewiseLinearFunction func;
	
	@Test
	public void test() {
		
		double tol = 0.00000001;
		piecewiseX = new double[] {0d, 0.5, 0.75, 1d};
		piecewiseY = new double[] {0d, 0.5, 0.6, 1d};
		
		func = new PiecewiseLinearFunction(piecewiseX, piecewiseY);
		
		/* Check the vertices: */
		for(int i = 0; i < piecewiseX.length; i++)
			assertEquals(piecewiseY[i], func.getValue(piecewiseX[i]), tol);
		
		/* Check values in the intervals: */
		assertEquals(0.3, func.getValue(0.3), tol);
		assertEquals(0.55, func.getValue(0.625), tol);
		assertEquals(0.8, func.getValue(0.875), tol);
	}
}
