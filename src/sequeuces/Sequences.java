package sequeuces;

import org.apache.commons.lang3.ArrayUtils;

import cern.jet.random.Binomial;
import cern.jet.random.Normal;
import cern.jet.random.Poisson;
import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;
import search.Binary;
import umontreal.ssj.probdist.NormalDist;
import umontreal.ssj.rng.RandomStream;

public class Sequences {



	public static String printSequence(double[] d, String fmt)
	{ 
		String out = "";
		for (double s : d) out += String.format(fmt + " ", s);
		System.out.println(out);
		return out;
	}

	public static String printSequence(int[] d, String fmt)
	{
		String out = "";
		for (int s : d) out += String.format(fmt + " ", s);
		System.out.println(out);
		return out;
	}

	public static void main(String[] args) {

		demoDoubleSequences();
		demoIntSequences();
	}

	public static void demoDoubleSequences()
	{
		System.out.print("double intervals: ");
		printSequence(spacedIntervals(0.0, 50.0, 6), "%.2f");
		System.out.print("double intervals: ");
		printSequence(spacedIntervals(0.0, -50.0, 6), "%.2f");
		System.out.print("double intervals: ");
		printSequence(spacedIntervals(0.0, 0.0, 6), "%.2f");
	}

	public static void demoIntSequences()
	{
		System.out.print("int intervals:  ");
		printSequence(spacedIntervals(0, -51, 6), "%d");
		System.out.print("int intervals: ");
		printSequence(spacedIntervals(0, 51, 6), "%d");
		System.out.print("int intervals: ");
		printSequence(spacedIntervals(49, 51, 6), "%d");
	}



	


	/**
	 * 
	 * @param x
	 * @param min
	 * @param max
	 * @return
	 */
	public static double[] recenterAndNormalize(double[] x, double min, double max)
	{
		double[] minmax = minMax(x);
		double[] out = new double[x.length];

		double range = minmax[1] - minmax[0];
		double range2 = max - min;

		double ratio = range2 / range;

		if (minmax[0] < 0 || minmax[1] < 0) throw new IllegalArgumentException("All elements must be nonnegative.");
		if (range == 0) //throw new IllegalArgumentException("the array must have more than 1 distinct value.");
			//			out = VectorTools.addConstantToVector(x, min);
			return x;
		else		
			for (int i = 0; i < x.length; i++)
			{
				out[i] = min + ratio * ((x[i] - minmax[0]));
			}
		return out;
	}

	/** Normalize the elements of an array of doubles so that their sum is one. */
	public static double[] normalizeSum(double[] x)
	{
		double sum = 0d;
		for (double d : x) {
			if (d < 0) throw new IllegalArgumentException("All elements of the array must be nonnegative.");
			sum += d;
		}
		if (sum > 0)
		{
			for (int i = 0; i < x.length; i++) x[i] /= sum;
			return x;
		}
		else throw new IllegalArgumentException("There must be at least one positive element in the sequence.");
	}

	//	/** Normalize the elements of an array of doubles so that their sum is one. */
	//	public static double[] normalizeSum(int[] x)
	//	{
	//		double[] out = new double[x.length];
	//		double sum = 0d;
	//		for (double d : x) {
	//			if (d < 0) throw new IllegalArgumentException("All elements of the array must be nonnegative.");
	//			sum += d;
	//		}
	//		if (sum > 0)
	//		{
	//			for (int i = 0; i < x.length; i++) out[i] = (double)x[i] / sum;
	//			return out;
	//		}
	//		else throw new IllegalArgumentException("There must be at least one positive element in the sequence.");
	//	}

	//	/** Evenly spaced intervals.
	//	 * @param min lower limit for the set of intervals
	//	 * @param max upper limit for the set of intervals
	//	 * @param nIntervals number of intervals to calculate
	//	 * @return a set of endpoints for intervals, length is nIntervals + 1
	//	 */
	//	public static double[] intervals(double min, double max, int nIntervals)
	//	{
	//		double[] seq = sequence(min, max, nIntervals + 1);
	//		if (seq[0] > seq[seq.length - 1]) ArrayUtils.reverse(seq);
	//		return seq;
	//	}


	/** Evenly spaced intervals.
	 * @param min lower limit for the set of intervals
	 * @param max upper limit for the set of intervals
	 * @param nBreaks number of intervals to calculate
	 * @return a set of endpoints for intervals, length is nIntervals + 1
	 */
	public static double[] spacedIntervals(double min, double max, int nBreaks)
	{
		double[] out = new double[nBreaks + 1];
		out[nBreaks] = max;

		double interval = (max - min) / nBreaks;
		for (int i = 0; i < nBreaks; i++) out[i] = min + i * interval;

		if (out[0] > out[out.length - 1]) ArrayUtils.reverse(out);
		return out;
	}

	public static int[] spacedIntervals(int min, int max, int nBreaks)
	{
		int[] out = new int[nBreaks];
		double interval = ((double) (max - min)) / ((double) (nBreaks - 1));
		double sum = 0.0;
		out[0] = min;
		for (int i = 1; i < nBreaks - 1; i++)
		{
			sum += interval;
			out[i] = min + (int) sum;
		}
		out[nBreaks - 1] = max;
		if (out[0] > out[out.length - 1]) ArrayUtils.reverse(out);
		return out;
	}

	/**
	 * 
	 * @param intervalMins The 
	 * @param values
	 * @param key
	 * @param toAdd
	 */
	public static void incrementInInterval(double[] intervalMins, double[] values, double key, double toAdd)
	{
		/* Find the index to which to add the quantity: */
		int index = Binary.indexOfLessThanKey(intervalMins, key);
		//		int index = Binary.indexOfLessThanOrEqual(intervalMins, key);
		values[index] += toAdd;
	}

	public static void incrementInInterval(double[] intervalMins, int[] values, double key)
	{
		/* Find the index to which to add the quantity: */
		int index = Binary.indexOfLessThanKey(intervalMins, key);
		//		int index = Binary.indexOfLessThanOrEqual(intervalMins, key);
		values[index]++;
	}

	/**
	 * 
	 * @param min
	 * @param max
	 * @param intervalWidth width for intervals.  Adjusted as needed to make evenly spaced intervals between min ana max.
	 * @return
	 */
	public static double[] spacedIntervals(double min, double max, double intervalWidth)
	{
		double tol = 0.01;
		double[] out;

		int nIntervals = (int)(Math.abs(max - min) / intervalWidth);
		double remainder = ((max - min) % intervalWidth);
		double diff = Math.abs(intervalWidth - remainder);

		if (!(remainder < tol | diff < tol)) nIntervals++;

		out = new double[nIntervals + 1];

		for(int i = 0; i < nIntervals; i++)
		{
			out[i] = min + i * intervalWidth;
		}
		out[nIntervals] = max;
		return out;
	}

	public static int[] sequence(int start, int end) 
	{
		if (start == end) return new int[] {start}; 

		int length = Math.abs(end - start) + 1;
		int[] out = new int[length];
		int sign;
		if (end > start) sign = 1; else sign = -1;

		int incr = 0;
		for (int i = 0; i < length; i++)
		{
			out[i] = start + incr;
			incr += sign;
		}
		return out;
	}

	public static double sum(double[] seq) { double sum = 0d; for(double d : seq) sum += d; return sum; }
	public static int sum(int[] seq) { int sum = 0; for(int i : seq) sum += i; return sum; }
	
	public static int[] weightedRandomIntSequence(double[] weights, int sum, RandomEngine re)
	{
		Uniform unif = new Uniform(re);
		int[] out = new int[weights.length];
		int diff2;
		/* First multiply and round to the nearest int: */
		for (int i = 0; i < weights.length; i++) out[i] = (int)((double)sum * weights[i]);
		
		/* Check whether there are too many or too few items in the sequeunce: */
		int diff = sum - Sequences.sum(out);

		if (diff > 0)
			for (int i = 0; i < diff; i++) 
			{
				/* Choose a random index and incrememnt. */
				int index = unif.nextIntFromTo(0, weights.length - 1);
				out[index]++;
			}
		else if (diff < 0)
			while ((diff2 = Sequences.sum(out)) < 0)
				for (int i = 0; i < -diff2; i++)
				{
					/* Choose a random index and decrement. */
					int index = unif.nextIntFromTo(0, weights.length - 1);
					out[index] = Math.max(0, out[index] - 1);
				}
		return out;
	}

	public static int[] vectorOfPoissons(int[] lambda, RandomEngine re)
	{
		int[] out = new int[lambda.length];
		for(int i = 0; i < lambda.length; i++) out[i] = (new Poisson(lambda[i], re)).nextInt();
		return out;
	}

	public static int[] vectorOfPoissons(double[] lambda, RandomEngine re)
	{
		int[] out = new int[lambda.length];
		for(int i = 0; i < lambda.length; i++)	out[i] = (new Poisson(lambda[i], re)).nextInt();
		return out;
	}

	public static int[] vectorOfBinomials(int[] n, double[] p, RandomEngine re)
	{
		int[] out = new int[n.length];

		for(int i = 0; i < n.length; i++)
		{
			if (n[i] == 0 || p[i] == 0) out[i] = 0;
			else out[i] = (new Binomial(n[i], p[i], re)).nextInt();
		}
		return out;
	}

	public static int[] vectorOfBinomials(int n, double p, int nElements, RandomEngine re)
	{
		return vectorOfBinomials(repeatedInts(n, nElements), repeatedDoubles(p, nElements), re);
	}

	public static int[] vectorOfPoissons(double lambda, int nElements, RandomEngine re)
	{
		return vectorOfPoissons(repeatedDoubles(lambda, nElements), re);
	}

	public static int[] repeatedInts(int value, int nElements)
	{
		int[] out = new int[nElements];
		for(int i = 0; i < nElements; i++) out[i] = value;
		return out;
	}

	public static double[] repeatedDoubles(double value, int nElements)
	{
		double[] out = new double[nElements];
		for(int i = 0; i < nElements; i++) out[i] = value;
		return out;		
	}

	public static double[] vectorOfDoubleUnifs(int n, double low, double high, RandomEngine re)
	{
		Uniform unif = new Uniform(re);
		double[] out = new double[n];

		for (int i = 0; i < n; i++)
		{
			out[i] = unif.nextDoubleFromTo(low, high);
		}

		return out;
	}

	public static int[] vectorOfIntUnifs(int n, int low, int high, RandomEngine re)
	{
		Uniform unif = new Uniform(re);
		int[] out = new int[n];

		for (int i = 0; i < n; i++)
		{
			out[i] = unif.nextIntFromTo(low, high);
		}

		return out;
	}



	/** Minimum and maximum values of an array
	 * 
	 * @param array
	 * @param naVal ignore entries with this value
	 * @return
	 */
	public static double[] minMax(double[][] array, double naVal)
	{
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;

		for (double[] d1 : array) for (double d2 : d1)
		{
			if (d2 != naVal)
			{
				if (d2 < min) min = d2;
				if (d2 > max) max = d2;
			}
		}
		return new double[] {min, max};
	}

	/** Minimum and maximum values of an array
	 * 
	 * @param array
	 * @param naVal ignore entries with this value
	 * @return {min(x), max(x)}
	 */
	public static int[] minMax(int[][] array, int naVal)
	{
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;

		for (int[] d1 : array) for (int d2 : d1)
		{
			if (d2 != naVal)
			{
				if (d2 < min) min = d2;
				if (d2 > max) max = d2;
			}
		}
		return new int[] {min, max};
	}

	/** Minimum and maximum values of an array
	 * @param x array to find min and max for, does not need to be sorted
	 * @return {min(x), max(x)}
	 */
	public static double[] minMax(double[] x)
	{
		double min1 = Double.MAX_VALUE;
		double max1 = Double.MIN_VALUE;
		for(double d : x) { if (d < min1) min1 = d; if (d > max1) max1 = d; }
		return new double[] {min1, max1};
	}
	
	/** Return an array of doubles corresponding to densities of the Normal distribution.
	 * @param x values of x at which the Normal pdf is evaluated.
	 * @param mean mean of the distribution
	 * @param sd standard deviation of the distribution
	 * @return Normal densities calculated at the values of x. */
	public static double[] normalDensities(int[] x, double mean, double sd, RandomEngine re)
	{	
		double[] d = new double[x.length]; Normal n = new Normal(mean, sd, re);
		for (int i = 0; i < x.length; i++) d[i] = n.pdf((double)x[i]);
		return d;
	}

	/** Return an array of doubles corresponding to densities of the Normal distribution.
	 * @param x values of x at which the Normal pdf is evaluated.
	 * @param mean mean of the distribution
	 * @param sd standard deviation of the distribution
	 * @return Normal densities calculated at the values of x. */
	public static double[] normalDensities(double[] x, double mean, double sd, RandomEngine re)
	{	
		double[] d = new double[x.length]; Normal n = new Normal(mean, sd, re);
		for (int i = 0; i < x.length; i++) d[i] = n.pdf(x[i]);
		return d;
	}

	/** Return an array of doubles corresponding to densities of the Normal distribution.
	 * @param x values of x at which the Normal probability density function is evaluated.
	 * @param mean mean of the distribution
	 * @param sd standard deviation of the distribution
	 * @return Normal densities calculated at the values of x. */
	public static double[] normalDensities(double[] x, double mean, double sd, RandomStream rs)
	{  
		double[] d = new double[x.length]; NormalDist n = new NormalDist(mean, sd);
		for (int i = 0; i < x.length; i++) d[i] = n.density(x[i]);
		return d;
	}

	/** Return an array of doubles corresponding to densities of the Normal distribution.
	 * @param x values of x at which the Normal probability density function is evaluated.
	 * @param mean mean of the distribution
	 * @param sd standard deviation of the distribution
	 * @return Normal densities calculated at the values of x. */
	public static double[] normalDensities(int[] x, double mean, double sd, RandomStream rs)
	{   
		double[] d = new double[x.length]; NormalDist n = new NormalDist(mean, sd);
		for (int i = 0; i < x.length; i++) d[i] = n.density(x[i]);
		return d;
	}

	/** Return an array of doubles proportional to densities of the Normal distribution.
	 * Values are normalized so that the sum is one.
	 * @param x values of x at which the Normal pdf is evaluated.
	 * @param mean mean of the distribution
	 * @param sd standard deviation of the distribution
	 * @return Normal densities calculated at the values of x. */
	public static double[] normalizedNormalDensitiesSequence(int[] x, double mean, double sd, RandomEngine re)
	{
		double[] densities = normalDensities(x, mean, sd, re);
		return normalizeSum(densities);
	}

}
