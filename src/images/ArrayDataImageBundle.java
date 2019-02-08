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

	private int width, height, queryX, queryY;
	private DataInterface getValInterface;
	private BufferedImage img;
	private ColorInterpolator ci;

	private double[][] dataDouble = null;
	private int[][] dataInt = null;
	private double[] doubleMinMax;
	private String dataName;

	private String dataType;

	public ArrayDataImageBundle() {}

	public static class ArrayDataImageFactory
	{
		public static ArrayDataImageBundle getBundle(
				double[][] data, 
				String name, Color[] colors,
				double naDouble, int naInt, Color naColor)
		{
			double[] minMax = Sequences.minMax(data, naDouble);
			ColorInterpolator ci = new ColorInterpolator(colors, minMax[0], minMax[1], naDouble, naColor);
			return getBundle(data, name, ci, naDouble, naInt, naColor);
		}
		
		public static ArrayDataImageBundle getBundle(
				double[][] data, 
				String name, ColorInterpolator ci,
				double naDouble, int naInt, Color naColor)
		{
			ArrayDataImageBundle out = new ArrayDataImageBundle();
			out.dataName = name;
			out.na_double = naDouble;
			out.na_color = naColor;
			out.setCi(ci);	
			out.setDouble(data);
			return out;
		}
		
		public static ArrayDataImageBundle getBundle(
				int[][] data, 
				String name, Color[] colors, 
				double naDouble, int naInt, Color naColor)
		{
			int[] minMax = Sequences.minMax(data, naInt);
			ColorInterpolator ci = new ColorInterpolator(colors, minMax[0], minMax[1], naDouble, naColor);
			return getBundle(data, name, ci, naDouble, naInt, naColor);
		}
		
		public static ArrayDataImageBundle getBundle(
				int[][] data, 
				String name, ColorInterpolator ci,
				double naDouble, int naInt, Color naColor)
		{
			ArrayDataImageBundle out = new ArrayDataImageBundle();
			out.dataName = name;
			out.na_double = naDouble;
			out.na_int = naInt;
			out.na_color = naColor;
			out.setCi(ci);	
			out.setInt(data);
			return out;
		}

		/** Get an image bundle for 1D int data. */
		public static ArrayDataImageBundle getBundle(
				int[] data, 
				String name, ColorInterpolator ci, 
				double naDouble, int naInt, Color naColor)
		{
			int[][] out = new int[1][data.length];
			for (int y = 0; y < out[0].length; y++) { out[0][y] = data[y]; }
			return getBundle(out, name, ci, naDouble, naInt, naColor);
		}
		
		/** Get an image bundle for 1D double data. */
		public static ArrayDataImageBundle getBundle(
				double[] data, 
				String name, ColorInterpolator ci, 
				double naDouble, int naInt, Color naColor)
		{
			double[][] out = new double[1][data.length - 1];
			for (int y = 0; y < out[0].length; y++) { out[0][y] = data[y]; }
			return getBundle(out, name, ci, naDouble, naInt, naColor);
		}

		/** Get image bundle with default na values. */
		public static ArrayDataImageBundle getBundle(
				int[][] data, 
				String name, Color[] colors) 
		{ return getBundle(data, name, colors, Double.MIN_VALUE, Integer.MIN_VALUE, Color.gray); }
		
		/** Get image bundle with default na values. */
		public static ArrayDataImageBundle getBundle(
				double[][] data, 
				String name, Color[] colors) 
		{ return getBundle(data, name, colors, Double.MIN_VALUE, Integer.MIN_VALUE, Color.gray); }
		
		public static ArrayDataImageBundle getVerticalGradientBundle(
				ArrayDataImageBundle bundle, int nSteps)
		{
			/* In case of integer data, the number of color steps shouldn't be more than
			 * the range of values used. */
			if (bundle.dataInt != null)
			{ 
				int min = (int) bundle.getMinValue();
				int max = (int) Math.ceil(bundle.getMaxValue());
				nSteps = Math.min(
						(int)bundle.getMaxValue() - (int)bundle.getMinValue() + 1, 
						nSteps);
				int[] datInt = Sequences.intSequence(min, max, nSteps);
				return getBundle(datInt, bundle.dataName, bundle.ci, bundle.na_double, bundle.na_int, bundle.na_color);
			}

			double[] data = Sequences.doubleIntervals(
					bundle.getMinValue(), bundle.getMaxValue(), nSteps);
			return getBundle(data, bundle.dataName, bundle.ci, bundle.na_double, bundle.na_int, bundle.na_color);
		}
		
		public static ArrayDataImageBundle getRandomBundle(int nRows, int nCols)
		{
			int nIntColors = 10;
			return getRandomBundle(nRows, nCols, nIntColors);
		}

		public static ArrayDataImageBundle getRandomBundle(int nRows, int nCols, int nIntColors)
		{ return getRandomBundle(nRows, nCols, nIntColors, ColorUtils.HEAT_COLORS); }

		public static ArrayDataImageBundle getRandomBundle(int nRows, int nCols, int nIntColors, Color[] colors)
		{
			Random r = new Random();
			int[][] dat2 = new int[nCols][nRows];
			for (int i = 0; i < dat2.length; i++) for (int j = 0; j < dat2[0].length; j++) dat2[i][j] = r.nextInt(nIntColors);
			return getBundle(dat2, "random data", colors);
		}
	}

	
	
	
	/**
	 * Note: the input coordinates are in terms of the data array size,
	 * not the displayed size of the image.
	 * @param dataArrayX column number of the original data array
	 * @param dataArrayY row number of the original data array
	 * @return
	 */
	public String getDataArrayVal(int dataArrayY, int dataArrayX) { 
		queryX = dataArrayX; queryY = dataArrayY;
		return getValInterface.getValue(); }

	private void setDouble(double[][] data)
	{
		dataType = "double";
		this.dataDouble = data;
		this.width = data.length;
		this.height = data[0].length;
		setImage(this.dataDouble);
		getValInterface = () -> getDoubleVal();
		doubleMinMax = Sequences.minMax(data, na_double);
	}

	private void setInt(int[][] data)
	{
		dataType = "int";
		this.dataInt = data;
		this.width = data.length;
		this.height = data[0].length;
		setImage(this.dataInt);
		getValInterface = () -> getIntVal();
		int[] intMinMax = Sequences.minMax(data, na_int);
		doubleMinMax = new double[] { (double) intMinMax[0], (double) intMinMax[1] };
	}

	private void setImage(double[][] array)
	{
		this.dataType = "double";
		img = new BufferedImage(width, height, rgbType);
		for (int y = 0; y < img.getHeight(); y++) for (int x = 0; x < img.getWidth(); x++)
			img.setRGB(x, y, getCi().getColor(array[x][y]));
	}

	private void setImage(int[][] array)
	{
		this.dataType = "int";
		img = new BufferedImage(width, height, rgbType);
		for (int y = 0; y < height; y++) for (int x = 0; x < width; x++)
			img.setRGB(x, y, getCi().getColor(array[x][y]));
	}

	private String getDoubleVal() { return String.format(mouseClickArrayDblFormat, 
			dataDouble[queryX][queryY]);}
	private String getIntVal() { return String.format("%d", dataInt[queryX][queryY]); }

	public double getMinValue() { return doubleMinMax[0]; }
	public double getMaxValue() { return doubleMinMax[1]; }

	public StretchyClickyIcon createIcon(boolean proportionate) 
	{ return new StretchyClickyIcon(img, proportionate); }
	
	public StretchyClickyIcon createIcon(boolean proportionate, int iconFixedWidth) 
	{ 
		StretchyClickyIcon icon = new StretchyClickyIcon(img, proportionate);
		if (iconFixedWidth > 0) icon.setFixedWidth(iconFixedWidth);
		return icon;
	}

	public ColorInterpolator getCi() { return ci; }
	public void setCi(ColorInterpolator ci) { this.ci = ci; }
	public Object getName() { return dataName; }
	public String getDataType() { return this.dataType; }


	@Deprecated
	public static ArrayDataImageBundle createRandomPackage(int nRows, int nCols)
	{
		int nIntColors = 10;
		return createRandomPackage(nRows, nCols, nIntColors);
	}

	@Deprecated
	public static ArrayDataImageBundle createRandomPackage(int nRows, int nCols, int nIntColors)
	{ return createRandomPackage(nRows, nCols, nIntColors, ColorUtils.HEAT_COLORS); }

	@Deprecated
	public static ArrayDataImageBundle createRandomPackage(int nRows, int nCols, int nIntColors, Color[] colors)
	{
		Random r = new Random();
		int[][] dat2 = new int[nCols][nRows];
		for (int i = 0; i < dat2.length; i++) for (int j = 0; j < dat2[0].length; j++) dat2[i][j] = r.nextInt(nIntColors);
		return new ArrayDataImageBundle(dat2, colors, "random data");
	}


	@Deprecated
	public ArrayDataImageBundle(int[] datInt, ColorInterpolator ci2, String name) {
		this.setCi(ci2);
		int[][] out = new int[1][datInt.length];
		for (int i = 0; i < out[0].length; i++) { out[0][i] = datInt[i]; }
		setInt(out);
	}

	@Deprecated
	public ArrayDataImageBundle(double[] data, ColorInterpolator ci, String name)
	{
		this.dataName = name;
		this.setCi(ci);
		double[][] out = new double[data.length - 1][1];
		for (int i = 0; i < out.length; i++) { out[i][0] = data[i]; }
		setDouble(out);
	}

	@Deprecated
	public ArrayDataImageBundle(int[][] data, Color[] colors, Color naColor, int naInt, String name)
	{
		this.dataName = name;
		this.na_color = naColor;
		this.na_int = naInt;
		int[] minMax = Sequences.minMax(data, naInt);
		ColorInterpolator ci = new ColorInterpolator(colors, minMax[0], minMax[1], naInt, naColor);
		this.setCi(ci);	setInt(data);
	}

	@Deprecated
	public ArrayDataImageBundle(double[][] data, Color[] colors, Color naColor, double naDouble, String name)
	{
		this.dataName = name;
		this.na_double = naDouble;
		this.na_color = naColor;
		double[] minMax = Sequences.minMax(data, naDouble);
		ColorInterpolator ci = new ColorInterpolator(colors, minMax[0], minMax[1], na_double, na_color);
		this.setCi(ci);	setDouble(data);
	}

	@Deprecated
	public ArrayDataImageBundle(int[][] data, Color[] colors, String name) 
	{ this(data, colors, Color.gray, Integer.MIN_VALUE, name); }

	@Deprecated
	public ArrayDataImageBundle(double[][] data, Color[] colors, Color na_color, String name) 
	{	this(data, colors, na_color, Double.MIN_VALUE, name); }

	@Deprecated
	public ArrayDataImageBundle(double[][] data, ColorInterpolator ci)
	{ this.setCi(ci); setDouble(data); }

	@Deprecated
	public ArrayDataImageBundle(int[][] data, ColorInterpolator ci)
	{
		this.dataType = "int";
		this.setCi(ci);
		this.dataInt = data;
		this.width = data[0].length;
		this.height = data.length;
		setImage(this.dataInt);
		getValInterface = () -> getIntVal();
	}
	
	@Deprecated
	public static ArrayDataImageBundle createGradientBundle(ArrayDataImageBundle bundle, int nSteps)
	{
		/* In case of integer data, the number of color steps shouldn't be more than
		 * the range of values used. */
		if (bundle.dataInt != null)
		{ 
			int min = (int) bundle.getMinValue();
			int max = (int) Math.ceil(bundle.getMaxValue());
			nSteps = Math.min(
					(int)bundle.getMaxValue() - (int)bundle.getMinValue() + 1, 
					nSteps);
			int[] datInt = Sequences.intSequence(min, max, nSteps);
			return new ArrayDataImageBundle(datInt, bundle.ci, bundle.dataName);
		}

		double[] data = Sequences.doubleIntervals(
				bundle.getMinValue(), bundle.getMaxValue(), nSteps);
		return new ArrayDataImageBundle(data, bundle.ci, bundle.dataName);
	}

	public String getDataArrayVal(int[] mouseCoord) { return this.getDataArrayVal(mouseCoord[1], mouseCoord[0]); }

}