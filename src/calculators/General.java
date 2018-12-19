package calculators;

public class General {

	public static double weightedMean(double[] values, double[] weights)
	{
		if (values.length != weights.length) throw new IllegalArgumentException("Number of weights and values must be equal.");
		double sumBoth = 0d;
		double sumWeights = 0d;
		for (int i = 0; i < values.length; i++) sumWeights += weights[i];
		if (sumWeights > 0)
			for (int i = 0; i < values.length; i++)
				sumBoth += values[i] * weights[i];
		else
		{
			sumWeights = values.length;
			for (int i = 0; i < values.length; i++)
				sumBoth += values[i];
		}

		return sumBoth / sumWeights;
	}


	public static double weightedMean(double[] values, int[] weights)
	{
		if (values.length != weights.length) throw new IllegalArgumentException("Number of weights and values must be equal.");
		double sumBoth = 0d;
		int sumWeights = 0;
		for (int i = 0; i < values.length; i++) sumWeights += weights[i];

		if (sumWeights > 0)
			for (int i = 0; i < values.length; i++)
				sumBoth += values[i] * (double)weights[i];
		else
		{
			sumWeights = values.length;
			for (int i = 0; i < values.length; i++)
				sumBoth += values[i];
		}

		return sumBoth / (double)sumWeights;
	}

	public static double fahrenheitToCelsius(double degreesF) { return (degreesF - 32) * (5d / 9d); }
	public static double celsiusToFahrenheit(double degreesC) { return (9d / 5d) * degreesC + 32; }

}
