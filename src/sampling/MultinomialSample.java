package sampling;

import cern.jet.random.Binomial;
import cern.jet.random.engine.RandomEngine;
import sequeuces.Sequences;

/**
 *  @author michaelfrancenelson
 *
 *	Sample from a multinomial distribution.  Uses successive Binomial draws. <br>
 *   This is the same method as rmultinom() in R: 
 *   <a href="https://stat.ethz.ch/R-manual/R-devel/library/stats/html/Multinom.html">
 *   https://stat.ethz.ch/R-manual/R-devel/library/stats/html/Multinom.html</a>
 */

public class MultinomialSample {

	/** Generate multinomially-distributed numbers.
	 * 
	 *   This is the same method as rmultinom() in R: <br> 
	 *   <a href="https://stat.ethz.ch/R-manual/R-devel/library/stats/html/Multinom.html">
	 *   https://stat.ethz.ch/R-manual/R-devel/library/stats/html/Multinom.html</a>
	 *   
	 * @param n total number in sample;
	 * @param p vector of probabilities for each category.  Must sum to 1.0.
	 * @param re 
	 * @return Counts of items randomly distributed to the categories.
	 */
	public static int[] sample(int n, double[] p, RandomEngine re)
	{

		int[] out = new int[p.length];
		if (n == 0) return out;
		
		Binomial b = new Binomial(n, p[0], re);
		int remaining = n;
		int count = 0;
		double cumulativeMass = 0.0;

		for (int i = 0; i < p.length; i++)
		{
			double prob = Math.min(1.0, Math.max(0.0, p[i] / (1.0 - cumulativeMass)));
			if (remaining > 0 && prob > 0.0)
			{
				if (prob >= 1.0) count = remaining;
				else count = b.nextInt(remaining, prob);
			}
			else count = 0;
			out[i] = count;
			remaining -= count;
			cumulativeMass += p[i];
		}
		return out;
	}

	/** Generate random, multinomially-distributed variates
	 * 
	 *   This is the same method as rmultinom() in R: <br> 
	 *   <a href="https://stat.ethz.ch/R-manual/R-devel/library/stats/html/Multinom.html">
	 *   https://stat.ethz.ch/R-manual/R-devel/library/stats/html/Multinom.html</a>
	 *   
	 * @param n total number in sample
	 * @param weights relative weights for the categories.  Does not need to sum to 1.0 (will be normalized).
	 * @param re
	 * @return Counts of items randomly distributed to the categories.
	 */
	public static int[] normalizeAndSample(int n, double[] weights, RandomEngine re)
	{
		double[] p = Sequences.normalizeSum(weights);
		return sample(n, p, re);
	}


	/** Generate a multinomial sample with equal probabilities for each category. */
	public static int[] sample(int n, int nCategories, RandomEngine re)
	{
		double[] weights = new double[nCategories];
		double weight = 1.0 / ((double) nCategories);

		for (int i = 0; i < weights.length; i++) {
			weights[i] = weight;
		}
		return sample(n, weights, re);
	}

	public static void _main(String[] args) {
		double[] p, weights;
		int n;
		int[] counts;

		RandomEngine re = new cern.jet.random.engine.MersenneTwister(1);
		n = 10001;

		p = new double[] {0.4, 0.2, 0.3, 0.1};
		counts = sample(n, p, re);
		print(counts);
		printSum(counts);

		System.out.println();
		weights = new double[] {4, 20, 30, 1};
		counts = normalizeAndSample(n, weights, re);
		print(counts);
		printSum(counts);

		System.out.println();
		counts = sample(n, p.length, re);
		print(counts);
		printSum(counts);
	}

	private static void print(int[] ints) { for (int i : ints) System.out.println(i); }
	private static void printSum(int[] ints) { System.out.println("sum = " + Sequences.seqSum(ints)); }
}
