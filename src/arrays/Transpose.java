package arrays;

public class Transpose {


	public static int[][] transpose(int[][] dat)
	{
		int width = dat.length;
		int height = dat[0].length;
		int[][] out = new int[height][width];
		for (int i = 0; i < width; i++) for (int j = 0; j < height; j++) out[j][i] = dat[i][j];
		return out;
	}

	public static double[][] transpose(double[][] dat)
	{
		int width = dat.length;
		int height = dat[0].length;
		double[][] out = new double[height][width];
		for (int i = 0; i < width; i++) for (int j = 0; j < height; j++) out[j][i] = dat[i][j];
		return out;
	}

}
