package discrete;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;
import sequeuces.Sequences;

public class TestDiscreteTools {

	RandomEngine re;
	int seed = 10;

	@Before
	public void setup() {
		re = new MersenneTwister(seed);
	}

	/** This may occasionally fail due to sampling error depending on seed of the RNG. */
	@Test
	public void testBernoulliRound()
	{
		int nTrials = 10000;
		double input = 1.75;
		int truncated = (int) input;
		double prob = input % 1;
		int sum = 0;

		for (int i = 0; i < nTrials; i++) 
			if (DiscreteTools.bernoulliRound(input, re) > truncated) sum++;

		assertEquals((double) sum, (double) nTrials * prob, 0.01 * nTrials);
	}

	@Test
	public void testBinomialPair()
	{
		int[] pair;
		int n = 3002;
		double p = 0.1;
		int tests = 100;

		for(int i = 0; i < tests; i++)
		{
			pair = DiscreteTools.binomialPair(n, p, re);
//			System.out.println();
//			for(int j : pair) System.out.print(j + " ");
			assertEquals(Sequences.sum(pair), n);
			assertEquals(pair[0], (int)(p * (double)n), 0.025 * n);
		}
	}


	@Test
	public void testRMultinom()
	{
		double[] probs = new double[] {0.1, 0.2, 0.3, 0.4};
		int n = 17;
		double[] expected = new double[probs.length];

		int nTests = 10000;

		for(int i = 0; i < probs.length; i++)
		{
			expected[i] = (double)n * probs[i];
		}

		int[] sums = new int[probs.length];

		int[] test;

		for(int i = 0; i < nTests; i++)
		{
			test = DiscreteTools.rMultinom(n, probs, re);
			for(int j = 0; j < probs.length; j++) sums[j] += test[j];
		}

		double[] means = new double[probs.length];

		for(int i = 0; i < probs.length; i++)
			means[i] = (double)sums[i] / (double)nTests;
		for(int i = 0; i < probs.length; i++)
			assertEquals(means[i], expected[i], expected[i] * 0.05);
	}
}
