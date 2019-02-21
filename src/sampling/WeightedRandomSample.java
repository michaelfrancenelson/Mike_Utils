package sampling;

import search.Binary;
import umontreal.ssj.randvar.UniformGen;
import umontreal.ssj.randvar.UniformIntGen;
import umontreal.ssj.rng.RandomStream;

/** Randomly choose elements from collections with possibly different probabilities associated with each item in the collection. */
public class WeightedRandomSample {
	
	/** Choose an index from an array with the provided probability weights.
	 *  <br> Weights do not need to sum to 1.
	 * 
	 * @param rs
	 * @param weights
	 * @return
	 */
	public static int sample(RandomStream rs, double[] weights)
	{
		double sum = 0.0;
		double[] cumulativeWeights = new double[weights.length];
		for (int i = 0; i < weights.length; i++) 
		{
			sum += weights[i];
			cumulativeWeights[i] = sum;
		}
		/* In case there is zero weight, choose an index uniformly. */
		if (sum <= 0)
		{
			return UniformIntGen.nextInt(rs, 0, weights.length - 1);
		}
		double key = UniformGen.nextDouble(rs, 0.0, sum);
		int index = Binary.insertionIndex(cumulativeWeights, key);

		return index;
	}
	
	/** Choose an index from an array with an inverted version of the provided probability weights. <br>
	 *  Weights do not need to sum to 1. <br>
	 *  To create the values of the inverse weight array, each weight is subtracted 
	 *  from the sum of the min and max elements of the original weight array.
	 * 
	 * @param rs
	 * @param weights
	 * @return
	 */
	public static int inverseSample(RandomStream rs, double[] weights)
	{
		double sum = 0.0;
		double const1 = 0.0, const2 = 0.0;
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		double[] cumulativeWeights = new double[weights.length];

		for (int i = 0; i < weights.length; i++)
		{
			if (weights[i] < min) min = weights[i];
			if (weights[i] > max) max = weights[i];
		}

		const1 = max + min;

		for (int i = 0; i < weights.length; i++) 
		{
			const2 = const1 - weights[i];
			sum += const2;
			cumulativeWeights[i] = sum;
		}

		/* In case there is zero weight, choose an index uniformly. */
		if (sum <= 0) 
			{
			return UniformIntGen.nextInt(rs, 0, weights.length - 1);
			}
		double key = UniformGen.nextDouble(rs, 0, sum);
		int index = Binary.insertionIndex(cumulativeWeights, key);
		
		return index;
	}
	
}
