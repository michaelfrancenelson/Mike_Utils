package calculators;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestGeneral {

	@Test
	public void testWeightedMean() {
		
		double tol = 0.00000001;
		double[] values = new double[] {0d, 5d, 10d};
		double[] weights = new double[] {1d, 1d, 1d};
		int[] weightsInt = new int[] {1, 1, 1};
		
		double expected = 5d;
		assertEquals(General.weightedMean(values, weights), expected, tol);
		assertEquals(General.weightedMean(values, weightsInt), expected, tol);
		
		weights = new double[] {1d, 0, 0};
		weightsInt = new int[] {1, 0, 0};
		expected = 0d;
		assertEquals(General.weightedMean(values, weights), expected, tol);
		assertEquals(General.weightedMean(values, weightsInt), expected, tol);
		
		weights = new double[] {0d, 0, 10d};
		weightsInt = new int[] {0, 0, 10};
		expected = 10d;
		assertEquals(General.weightedMean(values, weights), expected, tol);
		assertEquals(General.weightedMean(values, weightsInt), expected, tol);
		
		weights = new double[] {0d, 10d, 10d};
		weightsInt = new int[] {0, 10, 10};
		expected = 7.5;
		assertEquals(General.weightedMean(values, weights), expected, tol);
		assertEquals(General.weightedMean(values, weightsInt), expected, tol);
		
		
		weights = new double[] {0d, 0d, 0d};
		weightsInt = new int[] {0, 0, 0};
		expected = 5;
		assertEquals(General.weightedMean(values, weights), expected, tol);
		assertEquals(General.weightedMean(values, weightsInt), expected, tol);
		
		
	}

}
