package vectors;

public class VectorTools {

	
	public static double[] addConstantToVector(double[] v1, double c)
	{
		double[] out = new double[v1.length];
		for(int i = 0; i < v1.length; i++) out[i] = v1[i] + c;
		return out;
	}
	
	public static int[] add2Vectors(int[] v1, int[] v2)
	{
		if (v1.length != v2.length) throw new IllegalArgumentException("Vectors are different lengths.");
		int[] out = new int[v1.length];
		for(int i = 0; i < v1.length; i++) out[i] = v1[i] + v2[i];
		return out;
	}
	
	public static double[] add2Vectors(double[] v1, double[] v2)
	{
		if (v1.length != v2.length) throw new IllegalArgumentException("Vectors are different lengths.");
		double[] out = new double[v1.length];
		for(int i = 0; i < v1.length; i++) out[i] = v1[i] + v2[i];
		return out;
	}
	
	/** Subtract every element from the second vector from the corresponding element in the first. */
	public static int[] vectorDifference2D(int[] v1, int[] v2)
	{
		if (v1.length != v2.length) throw new IllegalArgumentException("Vectors are different lengths.");
		int[] out = new int[v1.length];
		for(int i = 0; i < v1.length; i++) out[i] = v1[i] - v2[i];
		return out;
	}
	
	public static double[] vectorDifference2D(double[] v1, double[] v2)
	{
		if (v1.length != v2.length) throw new IllegalArgumentException("Vectors are different lengths.");
		double[] out = new double[v1.length];
		
		for(int i = 0; i < v1.length; i++) out[i] = v1[i] - v2[i];
		return out;
	}
	

	public static double[] vecorElementWiseProducts(double []v1, int[] v2)
	{
		if (v1.length != v2.length) throw new IllegalArgumentException("Vectors are different lengths.");
		double[] out = new double[v1.length];
		for(int i = 0; i < v1.length; i++) out[i] = v1[i] * (double)v2[i];
		return out;
	}
	
	
	public static double[] vecorElementWiseProducts(double []v1, double[] v2)
	{
		if (v1.length != v2.length) throw new IllegalArgumentException("Vectors are different lengths.");
		double[] out = new double[v1.length];
		for(int i = 0; i < v1.length; i++) out[i] = v1[i] * v2[i];
		return out;
	}
	
	public static int[] vecorElementWiseProducts(int []v1, int[] v2)
	{
		if (v1.length != v2.length) throw new IllegalArgumentException("Vectors are different lengths.");
		int[] out = new int[v1.length];
		for(int i = 0; i < v1.length; i++) out[i] = v1[i] * v2[i];
		return out;
	}
	
	public static int[] vectorMin(int[] values, int compare)
	{
		int[] out = new int[values.length];
		for(int i = 0; i < values.length; i++) out[i] = Math.min(values[i], compare);
		return out;
	}
	
	public static double[] vectorMin(double[] values, double compare)
	{
		double[] out = new double[values.length];
		for(int i = 0; i < values.length; i++) out[i] = Math.min(values[i], compare);
		return out;
	}
	
	public static int[] vectorMin(int[] v1, int[] v2)
	{
		if (v1.length != v2.length) throw new IllegalArgumentException("Vectors are different lengths.");
		int[] out = new int[v1.length];
		for(int i = 0; i < v1.length; i++) out[i] = Math.min(v1[i], v2[i]);
		return out;
	}
	
	public static double[] vectorMin(double[] v1, double[] v2)
	{
		if (v1.length != v2.length) throw new IllegalArgumentException("Vectors are different lengths.");
		double[] out = new double[v1.length];
		for(int i = 0; i < v1.length; i++) out[i] = Math.min(v1[i], v2[i]);
		return out;
	}
	
}
