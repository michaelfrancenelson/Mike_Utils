package vectors;

import static org.junit.Assert.assertEquals;
import static vectors.VectorTools.add2Vectors;
import static vectors.VectorTools.vecorElementWiseProducts;
import static vectors.VectorTools.vectorDifference2D;
import static vectors.VectorTools.vectorMin;

import org.junit.Before;
import org.junit.Test;

import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;
import sequeuces.Sequences;

public class TestVectorTools {

	RandomEngine re;
	int seed = 122345985;
	@Before
	public void setup()
	{
		re = new MersenneTwister(seed);
	}
	
	
	@Test
	public void testAddVectors()
	{
		double tol = 0.000000001;
		double[]   v1D = new double[] {-1, 2, -4.5, -100};
		double[]   v2D = new double[] {1, 2, -5.5, 101};
		
		double[]  expD = new double[] {0, 4, -10, 1};
		add2Vectors(v1D, v2D);
		
		int[]      v1I = new int[] {1, 2, -5, 100};
		int[]      v2I = new int[] {-1, 3, -5, -105};
		
		int[]     expI = new int[] {0, 5, -10, -5};
		add2Vectors(v1I, v2I);
		
		for(int i = 0; i < v1D.length; i++)
		{
			assertEquals(expD[i], v1D[i] + v2D[i], tol);
			assertEquals(expI[i], v1I[i] + v2I[i]);
		}
	}
	
	@Test
	public void testSubtractVectors()
	{
		double tol = 0.000000001;
		double[]   v1D = new double[] {-1, 2, -4.5, 101};
		double[]   v2D = new double[] { 1, 2,  5.5, 100};
		
		double[]  expD = new double[] {-2, 0, -10, 1};
		vectorDifference2D(v1D, v2D);
		
		int[]      v1I = new int[] {1, 2, -5, 100};
		int[]      v2I = new int[] {-1, 3, 5, -105};
		
		int[]     expI = new int[] {2, -1, -10, 205};
		vectorDifference2D(v1I, v2I);
		
		for(int i = 0; i < v1D.length; i++)
		{
			assertEquals(expD[i], v1D[i] - v2D[i], tol);
			assertEquals(expI[i], v1I[i] - v2I[i]);
		}
	}

	@Test
	public void testProductVectors()
	{
		double tol = 0.000000001;
		double[]   v1D = new double[] {-1, 2, -4.5, 101};
		double[]   v2D = new double[] { 1, 2,  -2, 100};
		
		double[]  expD = new double[] {-1, 4, 9, 10100};
		vecorElementWiseProducts(v1D, v2D);
		
		int[]      v1I = new int[] {1, 2, -5, 100};
		int[]      v2I = new int[] {-1, 3, -5, -105};
		
		int[]     expI = new int[] {-1, 6, 25, -10500};
		vecorElementWiseProducts(v1I, v2I);
		
		for(int i = 0; i < v1D.length; i++)
		{
			assertEquals(expD[i], v1D[i] * v2D[i], tol);
			assertEquals(expI[i], v1I[i] * v2I[i]);
		}
	}
	
	
	@Test
	public void testVectorMin()
	{
		double tol = 0.00000001;
		
		int[] values1 = Sequences.sequence(-10, 10);
		int[] values2 = Sequences.sequence(-11, 9);
		
		double[] val3 = new double[] {-1, 0, 2.2, 3};
		double[] val4 = new double[] {-1, -1, 2, 3.2};
		double[] exp1 = new double[] {-1, -1, 2, 3};
		
		int min = 1;
		
		int[] test1 = vectorMin(values1, min);
		int[] test2 = vectorMin(values1, values2);
		double[] test3 = vectorMin(val3, val4);
		
		for(int i = 0; i < values1.length; i++)
		{
			if (i > 10) assertEquals(1, test1[i]);
			else assertEquals(i - 10, test1[i]);
			assertEquals(values2[i], test2[i]);
		}

		for(int i = 0; i < exp1.length; i++)
		{
			assertEquals(test3[i], exp1[i], tol);
		}
	}
	
}
