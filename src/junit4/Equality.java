package junit4;

import static org.junit.Assert.assertEquals;

public class Equality {

	static final double TOL = 0.0000001;
	
	/** Convenience method for checking that each element of the arrays are equal.*/
	public static void assertEqualsArray(int[] array1, int[] array2) {
		for (int i = 0; i < array1.length; i++) assertEquals(array1[i], array2[i]); }
	/** Convenience method for checking that each element of the arrays are equal.*/
	public static void assertEqualsArray(double[] array1, double[] array2) { 
		for (int i = 0; i < array1.length; i++) assertEquals(array1[i], array2[i], TOL);  }
}
