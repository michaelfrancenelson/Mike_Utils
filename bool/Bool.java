package bool;

public class Bool {

	public static boolean parseBool(String in)
	{
		if (in.contains("F") | in.contains("f")) return false;
		else return true;
	}
	
	public static boolean parseBool(int in)
	{
		if (in == 0) return false;
		else return true;
	}
	
	
	public static boolean[][] intToBooleanArray(int[][] input)
	{
		boolean[][] output = new boolean[input.length][input[0].length];
		
		for (int i = 0; i < input.length; i++) for (int j = 0; j < input[0].length; j++)
			output[i][j] = parseBool(input[i][j]);
		return output;
	}
}
