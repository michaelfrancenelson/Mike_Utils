package images;

import java.awt.Color;

public class ImageUtils {

	public static Color gray;

	public static Color[] grays, blues, greens, reds;
	public static Color[] booleanColors;
	public static Color[] redBlueGradient;
	public static Color[] redYellowGreenGradient;

	static 
	{
		gray = Color.GRAY;
		grays = new Color[] {Color.BLACK, Color.WHITE};
		blues = new Color[] {new Color(0, 0, 10), Color.BLUE, new Color(200, 200, 255)};
		greens = new Color[] {new Color(0, 10, 0), new Color(0, 255, 0), new Color(200, 255, 200)};
		reds = new Color[] {new Color(10, 0, 0), new Color(255, 0, 0), new Color(255, 200, 200)};
		redBlueGradient =  new Color[] {Color.RED, Color.BLUE};
		redYellowGreenGradient = new Color[] {Color.RED, Color.YELLOW, Color.GREEN};
		booleanColors = new Color[] {Color.BLACK, Color.WHITE};
	}
	//
	//	public static BufferedImage addHarvestTypeToImg(BufferedImage img, int[][] currentHarvest)
	//	{
	//		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
	//
	//		for (int row = 0; row < currentHarvest.length; row++) for (int col = 0; col < currentHarvest[0].length; col++)
	//		{
	//			int val = currentHarvest[row][col];
	//			if (val == 1)
	//				out.setRGB(col, row, ModelGUI.primaryHarvCol);
	//			else if (val == 2)
	//				out.setRGB(col, row, ModelGUI.secondaryHarvCol);
	//			else 
	//				out.setRGB(col, row, img.getRGB(col, row));
	//		}	
	//		return out;
	//	}

}
