package images;

import java.awt.image.BufferedImage;

public class ArrayImagePackage 
{
	@FunctionalInterface
	private interface DataInterface { String getValue(); }

	public static int rgbType = BufferedImage.TYPE_3BYTE_BGR;
	public static String mouseClickArrayDblFormat = "%.2f";

	private int width, height, queryColumn, queryRow;

	private DataInterface getValInterface;

	private BufferedImage img;
	private ColorInterpolator ci;

	private double[][] dataDouble = null;
	private int[][] dataInt = null;

	public String getVal() { return getValInterface.getValue(); }

	public ArrayImagePackage(double[][] data, ColorInterpolator ci)
	{
		this.ci = ci;
		this.dataDouble = data;
		this.width = data[0].length;
		this.height = data.length;
		setImage(this.dataDouble);
		getValInterface = () -> getDoubleVal();
	}

	public ArrayImagePackage(int[][] data, ColorInterpolator ci)
	{
		this.ci = ci;
		this.dataInt = data;
		this.width = data[0].length;
		this.height = data.length;
		setImage(this.dataInt);
		getValInterface = () -> getIntVal();
	}

	private void setImage(double[][] array)
	{
		img = new BufferedImage(array[0].length, array.length, rgbType);
		for (int row = 0; row < img.getHeight(); row++) for (int col = 0; col < img.getWidth(); col++)
			img.setRGB(col, row, ci.getColor(array[row][col]));
	}

	private void setImage(int[][] array)
	{
		img = new BufferedImage(width, height, rgbType);
		for (int row = 0; row < height; row++) for (int col = 0; col < width; col++)
			img.setRGB(col, row, ci.getColor(array[row][col]));
	}

	private String getDoubleVal() { return String.format(mouseClickArrayDblFormat, dataDouble[queryRow][queryColumn]);}
	private String getIntVal() { return String.format("%d", dataInt[queryRow][queryColumn]); }

	public void setQueryCoords(double relativeX, double relativeY)
	{
		queryColumn = Math.min(width - 1, (int) ((double) (width) * relativeX));
		queryRow = Math.min(height - 1, (int) ((double) (height) * relativeY));
	}

	public String getQueryX() { return "" + queryColumn; }
	public String getQueryY() { return "" + queryRow; }
}
