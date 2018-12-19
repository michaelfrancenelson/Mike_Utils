package discrete;

import cern.jet.random.Binomial;
import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;

public class DiscreteTools {

	
	/** Probabilistically round a double to a neighboring integer.
	 * @param in a decimal number to be rounded to an integer.
	 * @param unif a {@link cern.jet.random.Uniform} distribution.
	 * @return an integer resulting from the sum of the integer portion of the input double and a bernoulli 
	 * trial using the decimal portion of the input double as the probability of success*/
	public static int bernoulliRound(double in, RandomEngine re)
	{
		Uniform unif = new Uniform(re);
		if (unif.nextDouble() < in % 1) return (int)in + 1;
		else return (int)in;
	}

	public static int[] bernoulliRound(double[]in, RandomEngine re)
	{
		int[] out = new int[in.length];
		for (int i = 0; i < in.length; i++) out[i] = bernoulliRound(in[i], re);
		return out;
	}
	
	/** Get the number of successes and failures in a (pseudo)random binomial process.
	 * @param n 
	 * @param p
	 * @param re
	 * @return index 0 = number successes, index 1 = number of failures	 */
	public static int[] binomialPair(int n, double p, RandomEngine re)
	{
		if (n < 1) throw new IllegalArgumentException("Number must be greater than 0, you entered: " + n);
		
		
		int[] out = new int[2];
		out[0] = new Binomial(n, p, re).nextInt();
		out[1] = n - out[0];
		return out;
	}
	
	/** Generate a random multinomial vector using a repeated binomial method (i.e. the method from R).<br>
	 *  https://github.com/SurajGupta/r-source/blob/master/src/nmath/rmultinom.c
	 * @param n number of items to place in the k bins;
	 * @param prob the probability of each of the bins.  This must be normalized so that they sum to 1.  Normalization si not checked.
	 * @param re a RandomEngine.
	 * @return vector in which the indices correspond to bins.  The sum is n. */
	public static int[] rMultinom(int n, double[] prob, RandomEngine re)
	{
		int[] rN = new int[prob.length];
		int remaining = n;
		double pp;
		double p_tot = 1d;
		
		for (int k = 0; k < prob.length - 1; k++)
		{
			pp = prob[k] / p_tot;
			if (pp > 0d && remaining > 0)
			{
				if (pp == 1)
				{
					rN[k] = remaining;
					remaining = 0;
				}
				else
				{
					rN[k] = (new Binomial(remaining, pp, re).nextInt());
					remaining -= rN[k];
				}
			}
			if(remaining == 0) break;
			p_tot -= prob[k];
		}
		rN[prob.length - 1] = remaining;
		return rN;
	}
	
}
