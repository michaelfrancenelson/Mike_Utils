package arrays;

public class AddArrays {

	public static void addTwoDouble2DArrays(double[][] a1, double[][] a2)
	{
		if(a1.length != a2.length || a1[0].length != a2[0].length) throw new IllegalArgumentException("Arrays are not the same size.");
		
		for(int row = 0; row < a1.length; row++) for(int col = 0; col < a1[0].length; col++)
		{
			a1[row][col] += a2[row][col];
		}
	}
	
}
