package sampling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;

public class TestWeightedRandomSample {


	RandomEngine re;
	int seed = 12345;
	private Uniform unif;

	@Before
	public void setup() {
		re = new MersenneTwister(seed);
		unif = new Uniform(re);
//		rg = RandomSource.create(RandomSource.MT, seed); 
	}

	@Test
	public void testSample() {


		double[] weights = new double[] {0, 1, 0, 0};

		for (int test = 0; test < 100; test++)
			assertEquals(1, WeightedRandomSample.sample(unif, weights));

		weights = new double[] {1, 1, 1, 1};
		int[] counts = new int[weights.length];

		int nSamples = (int) 1e+4;
		int expected = nSamples / weights.length;
		for (int test = 0; test < 100; test++)
		{
			counts = new int[weights.length];
			for (int n = 0; n < nSamples; n++)
				counts[WeightedRandomSample.sample(unif, weights)] ++;

			int diff = Math.abs(counts[0] - expected);
			assertTrue(diff < nSamples * 0.1);
		}
		
		weights = new double[] {1, 3, 5, 1};

		double[] expected2 = new double[] {0.1, 0.3, 0.5, 0.1};
		
		
		for (int test = 0; test < 100; test++)
		{
			counts = new int[weights.length];	
			for (int n = 0; n < nSamples; n++)
				counts[WeightedRandomSample.sample(unif, weights)] ++;
			for (int i = 0; i < weights.length; i++)
				assertEquals((double)counts[i] / nSamples, expected2[i], 0.05);
		}

	}		

	@Test
	public void testInverseSample()
	{
		double[] weights = new double[] {0, 1, 0, 0};

		for (int test = 0; test < 100; test++)
			assertEquals(1, WeightedRandomSample.sample(unif, weights));

		weights = new double[] {1, 0, 1, 1};
		for (int test = 0; test < 100; test++)
			assertEquals(1, WeightedRandomSample.inverseSample(unif, weights));

		double[] invertedWeights = new double[weights.length];


		weights = new double[] {1, 2, 3, 4};
		invertedWeights = new double[] {4, 3, 2, 1};
		double sum = 0.0;  for (double w : invertedWeights) sum += w;
		
		int[] counts;

		int nSamples = (int) 1e+4;
		
		for (int test = 0; test < 100; test++)
		{
			counts = new int[weights.length];	
			for (int n = 0; n < nSamples; n++)
				counts[WeightedRandomSample.inverseSample(unif, weights)] ++;

			for (int i = 0; i < weights.length; i++)
				assertEquals((double)(counts[i]) / (double)nSamples, (invertedWeights[i] / sum), 0.05);
		}
	}




}
