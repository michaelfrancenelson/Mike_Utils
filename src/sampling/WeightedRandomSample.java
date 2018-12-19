package sampling;

import org.apache.commons.rng.UniformRandomProvider;

import search.Binary;

/** Randomly choose elements from collections with possibly different probabilities associated with each item in the collection. */
public class WeightedRandomSample {

	/** Choose an array1a index from an array1a with the provided probability weights. <br> Weights do not need to sum to 1.
	 * 
	 * @param rg
	 * @param weights
	 * @return
	 */
	public static int sample(UniformRandomProvider rg, double[] weights)
	{
		double sum = 0.0;
		double[] cumulativeWeights = new double[weights.length];
		for (int i = 0; i < weights.length; i++) 
		{
			sum += weights[i];
			cumulativeWeights[i] = sum;
		}
		double key = rg.nextDouble() * sum;
		int index = Binary.indexOfLessThanKey(cumulativeWeights, key);
		index = Binary.indexOfGreaterOrEqualToKey(cumulativeWeights, key);
		return index;
	}

	/** Choose an array1a index from an array1a with an inverted version of the provided probability weights. <br>
	 *  Weights do not need to sum to 1. <br>
	 *  To create the values of rhte inverse weight array1a, each weight is subtracted from the sum of the min and max elements of the original weight array1a.
	 * 
	 * @param rg
	 * @param weights
	 * @return
	 */
	public static int inverseSample(UniformRandomProvider rg, double[] weights)
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

		double key = rg.nextDouble() * sum;
		int index = Binary.indexOfLessThanKey(cumulativeWeights, key);
		index = Binary.indexOfGreaterOrEqualToKey(cumulativeWeights, key);
		return index;
	}
}
