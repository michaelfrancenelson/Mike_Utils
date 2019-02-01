package images;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import sequeuces.Sequences;
import swing.ColorUtils;
import swing.StretchyClickyIcon;

public class ArrayDataImageBundle 
{
	@FunctionalInterface
	private interface DataInterface { String getValue(); }

	private int    rgbType = BufferedImage.TYPE_3BYTE_BGR;
	private String mouseClickArrayDblFormat = "%.2f";
	private double na_double = Double.MIN_VALUE;
	private int    na_int    = Integer.MIN_VALUE;
	private Color  na_color  = Color.gray;
//	public static int    rgbType = BufferedImage.TYPE_3BYTE_BGR;
//	public static String mouseClickArrayDblFormat = "%.2f";
//	public static double NA_DOUBLE = Double.MIN_VALUE;
//	public static int    NA_INT    = Integer.MIN_VALUE;
//	public static Color  NA_COLOR  = Color.gray;

	private int width, height, queryColumn, queryRow;
	private DataInterface getValInterface;
	private BufferedImage img;
	private ColorInterpolator ci;

	private double[][] dataDouble = null;
	private int[][] dataInt = null;
	private double[] doubleMinMax;
	private String dataName;
	
	public String getVal(int col, int row) { 
		queryColumn = col; queryRow = row;
		return getValInterface.getValue(); }

	public ArrayDataImageBundle(double[] data, ColorInterpolator ci, String name)
	{
		this.dataName = name;
		this.setCi(ci);
		double[][] out = new double[data.length - 1][1];
		for (int i = 0; i < out.length; i++) { out[i][0] = data[i]; }
		setDouble(out);
	}

	private void setDouble(double[][] data)
	{
		this.dataDouble = data;
		this.width = data[0].length;
		this.height = data.length;
		setImage(this.dataDouble);
		getValInterface = () -> getDoubleVal();
		doubleMinMax = Sequences.minMax(data, na_double);
	}

	private void setInt(int[][] data)
	{
		this.dataInt = data;
		this.width = data[0].length;
		this.height = data.length;
		setImage(this.dataInt);
		getValInterface = () -> getIntVal();
		int[] intMinMax = Sequences.minMax(data, na_int);
		doubleMinMax = new double[] { (double) intMinMax[0], (double) intMinMax[1] };
	}

	public ArrayDataImageBundle(int[][] data, Color[] colors, String name) 
	{ this(data, colors, Color.gray, Integer.MIN_VALUE, name); }
	public ArrayDataImageBundle(double[][] data, Color[] colors, Color na_color, String name) 
	{	this(data, colors, na_color, Double.MIN_VALUE, name); }
//	public ArrayDataImageBundle(int[][] data, Color[] colors, String name) { this(data, colors, NA_COLOR, NA_INT, name); }
//	public ArrayDataImageBundle(double[][] data, Color[] colors, String name) { this(data, colors, NA_COLOR, NA_DOUBLE, name); }

	public ArrayDataImageBundle(int[][] data, Color[] colors, Color naColor, int naInt, String name)
	{
		this.dataName = name;
		this.na_color = naColor;
		this.na_int = naInt;
		int[] minMax = Sequences.minMax(data, naInt);
		ColorInterpolator ci = new ColorInterpolator(colors, minMax[0], minMax[1], naInt, naColor);
		this.setCi(ci);	setInt(data);
	}

	public ArrayDataImageBundle(double[][] data, Color[] colors, Color naColor, double naDouble, String name)
	{
		this.dataName = name;
		this.na_double = naDouble;
		this.na_color = naColor;
		double[] minMax = Sequences.minMax(data, naDouble);
		ColorInterpolator ci = new ColorInterpolator(colors, minMax[0], minMax[1], na_double, na_color);
		this.setCi(ci);	setDouble(data);
	}

	public ArrayDataImageBundle(double[][] data, ColorInterpolator ci)
	{ this.setCi(ci); setDouble(data); }

	public ArrayDataImageBundle(int[][] data, ColorInterpolator ci)
	{
		this.setCi(ci);
		this.dataInt = data;
		this.width = data[0].length;
		this.height = data.length;
		setImage(this.dataInt);
		getValInterface = () -> getIntVal();
	}

	public ArrayDataImageBundle(int[] datInt, ColorInterpolator ci2) {
		this.setCi(ci2);
		int[][] out = new int[datInt.length][1];
		for (int i = 0; i < out.length; i++) { out[i][0] = datInt[i]; }
		setInt(out);
	}

	private void setImage(double[][] array)
	{
		img = new BufferedImage(array[0].length, array.length, rgbType);
		for (int row = 0; row < img.getHeight(); row++) for (int col = 0; col < img.getWidth(); col++)
			img.setRGB(col, row, getCi().getColor(array[row][col]));
	}

	private void setImage(int[][] array)
	{
		img = new BufferedImage(width, height, rgbType);
		for (int row = 0; row < height; row++) for (int col = 0; col < width; col++)
			img.setRGB(col, row, getCi().getColor(array[row][col]));
	}

	private String getDoubleVal() { return String.format(mouseClickArrayDblFormat, dataDouble[queryRow][queryColumn]);}
	private String getIntVal() { return String.format("%d", dataInt[queryRow][queryColumn]); }

	public double getMinValue() { return doubleMinMax[0]; }
	public double getMaxValue() { return doubleMinMax[1]; }

	public StretchyClickyIcon createIcon(boolean proportionate) { return new StretchyClickyIcon(img, proportionate); }
	public StretchyClickyIcon createIcon(boolean proportionate, int iconFixedWidth) { 
		StretchyClickyIcon icon = new StretchyClickyIcon(img, proportionate);
		if (iconFixedWidth > 0) icon.setFixedWidth(iconFixedWidth);
		return icon;
	}

	public static ArrayDataImageBundle createGradientBundle(ArrayDataImageBundle bundle, int nSteps)
	{
		/* In case of integer data, the number of color steps shouldn't be more than
		 * the range of values used. */
		if (bundle.dataInt != null)
		{ 
			int min = (int) bundle.getMinValue();
			int max = (int) Math.ceil(bundle.getMaxValue());
			nSteps = Math.max((int)bundle.getMaxValue() - (int)bundle.getMinValue(), nSteps);
			int[] datInt = Sequences.intSequence(min, max, nSteps);
			return new ArrayDataImageBundle(datInt, bundle.ci);
		}

		double[] data = Sequences.doubleIntervals(
				bundle.getMinValue(), bundle.getMaxValue(), nSteps);
		return new ArrayDataImageBundle(data, bundle.ci, bundle.dataName);
	}

	public ColorInterpolator getCi() { return ci; }
	public void setCi(ColorInterpolator ci) { this.ci = ci; }

	public static ArrayDataImageBundle createRandomPackage(int nRows, int nCols)
	{
		int nIntColors = 10;
		return createRandomPackage(nRows, nCols, nIntColors);
	}
	
	public static ArrayDataImageBundle createRandomPackage(int nRows, int nCols, int nIntColors)
	{
//		Random r = new Random();
		return createRandomPackage(nRows, nCols, nIntColors, ColorUtils.HEAT_COLORS);
//		int nIntColors = 10;
//		int[][] dat2 = new int[nRows][nCols];
//		for (int i = 0; i < dat2.length; i++) for (int j = 0; j < dat2[0].length; j++) dat2[i][j] = r.nextInt(nIntColors);
//		return new ArrayDataImageBundle(dat2, ColorUtils.HEAT_COLORS, "random data");
	}

	public static ArrayDataImageBundle createRandomPackage(int nRows, int nCols, int nIntColors, Color[] colors)
	{
		Random r = new Random();
//		int nIntColors = 10;
		int[][] dat2 = new int[nRows][nCols];
		for (int i = 0; i < dat2.length; i++) for (int j = 0; j < dat2[0].length; j++) dat2[i][j] = r.nextInt(nIntColors);
		return new ArrayDataImageBundle(dat2, colors, "random data");
	}

	public Object getName() { return dataName; }
}
