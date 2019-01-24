package images;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import sequeuces.Sequences;

public class ArrayToImage {
	public static Color gray;
	public static Color[] grayGradient, blueGradient, greenGradient, redGradient;
	public static Color[] booleanColors;
	public static Color[] redBlueGradient;
	public static Color[] redYellowGreenGradient;
	static 
	{
		gray = Color.GRAY;
		grayGradient = new Color[] {Color.BLACK, Color.WHITE};
		blueGradient = new Color[] {new Color(0, 0, 10), Color.BLUE, new Color(200, 200, 255)};
		greenGradient = new Color[] {new Color(0, 10, 0), new Color(0, 255, 0), new Color(200, 255, 200)};
		redGradient = new Color[] {new Color(10, 0, 0), new Color(255, 0, 0), new Color(255, 200, 200)};
		redBlueGradient =  new Color[] {Color.RED, Color.BLUE};
		redYellowGreenGradient = new Color[] {Color.RED, Color.YELLOW, Color.GREEN};
		booleanColors = new Color[] {Color.BLACK, Color.WHITE};
	}

	public static int rgbType = BufferedImage.TYPE_3BYTE_BGR;
	
	
	public static ImagePackage getImage(boolean[][] array, Color[] colors)
	{
		BufferedImage img = new BufferedImage(array[0].length, array.length, rgbType);
		for (int row = 0; row < img.getHeight(); row++) for (int col = 0; col < img.getWidth(); col++)
		{
			if (array[row][col]) img.setRGB(col, row, colors[1].getRGB());
			else img.setRGB(col, row, colors[0].getRGB());
		}
		return new ImagePackage(img, new ColorInterpolator(colors), array);
	}

	/** Create an image using an already created interpolator. */
	public static ImagePackage getImage(int[][] array, ColorInterpolator ci)
	{
		BufferedImage img = new BufferedImage(array[0].length, array.length, rgbType);
		for (int row = 0; row < img.getHeight(); row++) for (int col = 0; col < img.getWidth(); col++)
			img.setRGB(col, row, ci.getColor(array[row][col]));
		return new ImagePackage(img, ci, array);
	}

	public static ImagePackage getImage(double[][] array, ColorInterpolator ci)
	{
//		BufferedImage img = new BufferedImage(array[0].length, array.length, BufferedImage.TYPE_4BYTE_ABGR);
		BufferedImage img = new BufferedImage(array[0].length, array.length, rgbType);

		for (int row = 0; row < img.getHeight(); row++) for (int col = 0; col < img.getWidth(); col++)
		{
			img.setRGB(col, row, ci.getColor(array[row][col]));
		}
		return new ImagePackage(img, ci, array);
	}

	public static ImagePackage gradientImageVertical(double min, double max, int height, int width, Color[] colors)
	{
		ColorInterpolator ci = new ColorInterpolator(colors, min, max, Double.MIN_VALUE, Color.BLACK);
		return gradientImageVertical(min, max, height, width, ci);
	}


	public static void main(String[] args) {
		Color[] colors = new Color[] {Color.white, Color.BLUE, Color.red, Color.BLACK};

		ImagePackage ip = gradientImageVertical(0, 1, 1000, 1000, colors);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(ip.img)));
		frame.pack();
		frame.setPreferredSize(new Dimension(1000, 1000));
		frame.setVisible(true);
	}


	public static ImagePackage gradientImageVertical(double min, double max, int height, int width, ColorInterpolator ci)
	{
		double[] gradient = Sequences.doubleSequence(min, max, height);
		double[][] imgArray = new double[height][width];
		for (int i = 0; i < gradient.length; i++) for (int j = 0; j < width; j++)
				imgArray[i][j] = gradient[i];

		return getImage(imgArray, ci);
	}

	public static BufferedImage annotateImage(BufferedImage img, int[] vals, int[] colors, int[][] annotations)
	{
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

		for (int row = 0; row < annotations.length; row++) for (int col = 0; col < annotations[0].length; col++)
		{
			int val = annotations[row][col];

			int index = Arrays.binarySearch(vals, val);

			if (index >= 0) out.setRGB(col,  row, colors[index]);
			else out.setRGB(col, row, img.getRGB(col, row));
		}	
		return out;
	}

	public static BufferedImage annotateImage(BufferedImage img, int[] vals, Color[] colors, int[][] annotations)
	{
		int[] cols = new int[colors.length];
		for (int i = 0; i < cols.length; i++) cols[i] = colors[i].getRGB();
		return annotateImage(img, vals, cols, annotations);
	}

	/* Overloaded wrappers: */

	public static ImagePackage getImage(int[][] array, Color[] colors, int naValue, Color naColor)
	{
		int[] minMax = arrayMinMax(array, naValue);
		ColorInterpolator ci = new ColorInterpolator(colors, minMax[0], minMax[1], naValue, naColor);
		return getImage(array, ci);
	}

	public static ImagePackage getImage(double[][] array, Color[] colors, double naValue, Color naColor)
	{
		double[] minMax = arrayMinMax(array, naValue);
		ColorInterpolator ci = new ColorInterpolator(colors, minMax[0], minMax[1], naValue, naColor);
		return getImage(array, ci);
	}

	public static ImagePackage getImage(int[][] array, Color[] colors, int minVal, int maxVal, int naVal, Color naColor)
	{
		ColorInterpolator ci = new ColorInterpolator(colors, minVal, maxVal, naVal, naColor);
		return getImage(array, ci);
	}

	public static ImagePackage getImage(double[][] array, Color[] colors, double minVal, double maxVal, double naVal, Color naColor)
	{
		ColorInterpolator ci = new ColorInterpolator(colors, minVal, maxVal, naVal, naColor);
		return getImage(array, ci);
	}	

	/** Convenience wrapper for a BW image from boolean array1a */
	public static ImagePackage getImage(boolean[][] array) { return getImage(array, new Color[] {Color.WHITE, Color.BLACK}); }

	private static double[] arrayMinMax(double[][] array, double naValue)
	{
		/* find the min and max vals: */
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		for (int row = 0; row < array.length; row++) for (int col = 0; col < array[0].length; col++)
		{
			double val = array[row][col];
			if (val != naValue)
			{
				if (val < min) min = val;
				if (val > max) max = val;
			}
		}
		return new double[] {min, max};
	}

	private static int[] arrayMinMax(int[][] array, int naValue)
	{
		/* find the min and max vals: */
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		for (int row = 0; row < array.length; row++) for (int col = 0; col < array[0].length; col++)
		{
			int val = array[row][col];
			if (val != naValue)
			{
				if (val < min) min = val;
				if (val > max) max = val;
			}
		}
		return new int[] {min, max};
	}
}
