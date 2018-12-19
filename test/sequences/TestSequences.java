package sequences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;
import sequeuces.Sequences;
import vectors.VectorTools;



public class TestSequences {

	RandomEngine re;
	Normal normal;
	Uniform unif;
	int seed = 9;
	
	double tol = 0.0000001;
	
	@Before
	public void setup(){
		re = new MersenneTwister(seed);
		unif = new Uniform(re);
	}
	
	
	@Test
	public void testIncrementInInterval()
	{
		double[] mins;
		double[] sumsObs, sumsExp;
		double[] keys;
		double[] toAdd;
		
		mins = new double[] {0, 2.1, 4.2, 6.3, 8.4, 10.5, 10.6};
		sumsObs= new double[mins.length];
		keys = new double[] {0, 2.2, 2.3, 4.2, 4.4, 10.7};
		toAdd = new double[] {1, 1.2, 1.2, 1.3, 1.3, 1.5};
		sumsExp = new double[] {1, 2.4, 2.6, 0, 0, 0, 1.5};
		
		for(int i = 0; i < keys.length; i++)
			Sequences.incrementInInterval(mins, sumsObs, keys[i], toAdd[i]);;
		
		for(int i = 0; i < sumsObs.length; i++)
			assertEquals(sumsExp[i], sumsObs[i], tol);
	}
	
	
	@Test
	public void testDoubleIntervals()
	{
//		double min, max, intervalWidth, tol;
//		int nIntervals;
		
		tol = 0.00000001;
		
		double[] expected1 = new double[] {0, 2.1, 4.2, 6.3, 8.4, 10.5};
		double[] obs1 = Sequences.doubleIntervals(0, 10.5, 5);
		double[] obs2 = Sequences.doubleSpacedIntervals(0, 10.5, 2.1);
		assertEquals(obs1.length, expected1.length);
		assertEquals(obs2.length, expected1.length);
		for(int i = 0; i < obs1.length; i++)
		{
			assertEquals(expected1[i], obs1[i], tol);
			assertEquals(expected1[i], obs2[i], tol);
		}
		
		expected1 = new double[] {0, 2.1, 4.2, 6.3, 8.4, 10.5, 10.6};
		obs1 = Sequences.doubleSpacedIntervals(0, 10.6, 2.1);
		for(int i = 0; i < obs1.length; i++)
		{
			assertEquals(expected1[i], obs1[i], tol);
		}
	}
	
	@Test 
	public void testNormalize()
	{
		double tol = 0.0000001;
		double[] input = new double[] {1, 3, 5};
		double[] output = Sequences.normalize(input, 0, 1);
		double[] expected = new double[] {0, 0.5, 1};
		for(int i = 0; i < expected.length; i++) assertEquals(expected[i], output[i], tol);
		
		output = Sequences.normalize(input, -3, 1);
		expected = new double[] {-3, -1, 1};
		for(int i = 0; i < expected.length; i++) assertEquals(expected[i], output[i], tol);
		
		output = Sequences.normalize(input, 1, 5);
		expected = new double[] {1, 3, 5};
		for(int i = 0; i < expected.length; i++) assertEquals(expected[i], output[i], tol);

		
	}
	
	@Test
	public void testNormalizeSum() {
	
		int length = 1000;
		double[] inputSeq = new double[length];
		double[] outputSeq;
		
		for(int i = 0; i < length - 1; i++)
			inputSeq[i] = unif.nextDoubleFromTo(0.01, 23499.4);
		
		outputSeq = Sequences.normalizeSum(inputSeq);
		assertEquals("what?", Sequences.seqSum(outputSeq), 1d, 0.000001);
		for(double d : outputSeq) assertTrue(d >= 0d);
	}
	
	@Test
	public void testNormalizedNormalDensitiesSequence() {

		int end = 50;
		int start = -50;
		int length = 101;
		int[] seqX = Sequences.intSequence(start, end); 
		double[] seqY = Sequences.normalDensities(seqX, 1d, 1.1, re);
		
		/* Length ok? */
		assertEquals(seqY.length, length);
		/* Symmetrical? */
		assertEquals(seqY[50], seqY[52], 0.0001);
		/* Normalized? */
		assertEquals(Sequences.seqSum(seqY), 1d, 0.000001);
	}
	
	
	@Test
	public void testIntSequence() {
		int length;
		int[] seq;
		
		int[] start = {5, 15, -5, 5}; 
		int[] end = {15, 5, -15, -15};
		
		for(int i = 0; i < start.length; i++)
		{
			length = Math.abs(end[i] - start[i]) + 1;
			seq = Sequences.intSequence(start[i], end[i]);
			assertEquals(seq.length, length); assertEquals(seq[0], start[i]); assertEquals(seq[length - 1], end[i]);
		}
	}
	
	@Test
	public void testWeightedIntSequence() 
	{
		int length = 100;
		int sum = 1000;
		double[] weights = new double[length];
		
		for (int i = 0; i < length; i++) weights[i] = unif.nextDouble();
		weights = Sequences.normalizeSum(weights);
		
		assertEquals("", Sequences.seqSum(weights), 1d, 0.0001);
	
		int[] entries = Sequences.weightedIntSequence(weights, sum, re);
		assertEquals(sum, Sequences.seqSum(entries));
		for (int i : entries) assertTrue(i >= 0);
	
		int[] indices = Sequences.intSequence(-10, 10);
		double mean = 0;
		double sd = 2.5;
		weights = Sequences.normalizedNormalDensitiesSequence(indices, mean, sd, re);
		
		entries = Sequences.weightedIntSequence(weights, sum, re);
		assertEquals(sum, Sequences.seqSum(entries));
		for (int i : entries) assertTrue(i >= 0);
		for (int i : entries) System.out.print(i + " ");
	}
	

	
	@Test
	public void testVectorOfPoissons()
	{
		
		double[] lambdaDouble = new double[] {1.5, 5, 10, 100};
		int[] lambdaInt = new int[] {1, 5, 10, 100};
		
		double tol = 0.02;
		
		int nTests = 10000;
		int[] sumDouble = new int[lambdaDouble.length];
		int[] sumInt = new int[lambdaDouble.length];
		
		for (int test = 0; test < nTests; test++)
		{
			sumDouble = VectorTools.add2Vectors(sumDouble, Sequences.vectorOfPoissons(lambdaDouble, re));
			sumInt = VectorTools.add2Vectors(sumInt, Sequences.vectorOfPoissons(lambdaInt, re));
		}

		for(int i = 0; i < lambdaDouble.length; i++)
		{
			assertEquals((double)sumDouble[i] / (double)nTests, lambdaDouble[i], lambdaDouble[i] * tol);
			assertEquals((double)sumInt[i] / (double)nTests, lambdaInt[i], lambdaInt[i] * tol);
		}
		
		
		
	}
	
	@Test
	public void testVectorOfBinomials()
	{
		
		int nElements = 200;
		int nTests = 10000;
		int low = 1;
		int high = 10000;
		
		double tol = 0.02;
		
		double[] p = Sequences.vectorOfDoubleUnifs(nElements, 0, 1, re);
		int[] n = Sequences.vectorOfIntUnifs(nElements, low, high, re);
		
		int[] sums = new int[nElements];
		for (int test = 0; test < nTests; test++)
		{
			sums = VectorTools.add2Vectors(sums, Sequences.vectorOfBinomials(n, p, re));
		}
		
		for (int i = 0; i < nElements; i++)
		{
			double mean = (double)n[i] * p[i];
			assertEquals(mean, (double)sums[i] / (double)nTests, tol * mean);
		}
	}
	
	@Test
	public void testVectorOfUnifs()
	{
		
		int nElements = 5;
		int lowInt = -5;
		int highInt = 9999;
		double lowDub = -87;
		double highDub = 1.539;
		double tol = 0.01;
		
		double meanInt = (double)(lowInt + highInt) / 2d;
		double meanDub = (lowDub + highDub) / 2d;
		int nTests = 10000;
		
		int[] sumsInt = new int[nElements];
		double[] sumsDub = new double[nElements];
		
		for(int test = 0; test < nTests; test++)
		{
			sumsInt = VectorTools.add2Vectors(sumsInt, Sequences.vectorOfIntUnifs(nElements, lowInt, highInt, re));
			sumsDub = VectorTools.add2Vectors(sumsDub, Sequences.vectorOfDoubleUnifs(nElements, lowDub, highDub, re));
		}

		for(int i = 0; i < nElements; i++)
		{
			assertEquals((double)((double)sumsInt[i] / (double) nTests), meanInt, Math.abs(meanInt * tol));
			assertEquals((double)((double)sumsDub[i] / (double) nTests), meanDub, Math.abs(meanDub * tol));
		}
	}
	
}
