package images;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImagePackage
{
	public BufferedImage img;
	public BufferedImage annotatedImage;
	public ColorInterpolator ci;
	public Color naColor;

	public double[][] dataDouble = null;
	public int[][] dataInt = null;
	public boolean[][] dataBool = null;

	public ImagePackage(BufferedImage img, ColorInterpolator ci)
	{
		this.img = img;
		this.ci = ci;
	}

	public ImagePackage(BufferedImage img, ColorInterpolator ci, boolean[][] data)
	{
		this(img, ci);
		this.dataBool = data;
	}

	public ImagePackage(BufferedImage img, ColorInterpolator ci, double[][] data)
	{
		this(img, ci);
		this.dataDouble = data;
	}

	public ImagePackage(BufferedImage img, ColorInterpolator ci, int[][] data)
	{
		this(img, ci);
		this.dataInt = data;
	}
}