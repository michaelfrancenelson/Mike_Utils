package images;

import java.awt.Color;

import search.Binary;
import search.Binary.IndicesAndRelativePosition;
import sequeuces.Sequences;

public class ColorInterpolator
{
	private Color[] colors;

	private Color naColor = Color.GRAY;
	private int naInt = Integer.MIN_VALUE;
	private double naDouble = Double.MIN_VALUE;

	private double[] breaks;
	
	/** Constructor to use with boolean variables. */
	public ColorInterpolator(Color[] colors)
	{
		this.colors = colors;
		this.breaks = new double[] {0, 1};
	}

	public ColorInterpolator(Color[] colors, int min, int max, int naInt, Color naColor)
	{
		if (min > max) throw new IllegalArgumentException("max must be greater than or equal to min.");
		this.colors = colors;
		this.naInt = naInt;
		this.naColor = naColor;
		this.breaks = Sequences.doubleSequence((double) min, (double) max, colors.length);
	}
	
	public ColorInterpolator(Color[] colors, double min, double max, double naDouble, Color naColor)
	{
		if (min > max) throw new IllegalArgumentException("max must be greater than or equal to min.");

		this.breaks = Sequences.doubleSequence((double) min, (double) max, colors.length);

		this.colors = colors;
		this.naDouble = naDouble;
		this.naColor = naColor;

	}

	public int getColor(int val) 
	{
		if (val == naInt) return naColor.getRGB();
		return getColor((double) val); 
	}

	public int getColor(double val)
	{
		if (val == naDouble) return naColor.getRGB();

		IndicesAndRelativePosition pos = Binary.interpolateRelativePosition(breaks, val);
		int out = interpolateColor(colors[pos.lowIndex], colors[pos.highIndex], pos.relativePosition);
		return out;
	}

	private int interpolateColor(Color c1, Color c2, double proportion)
	{
		double prop1 = 1 - proportion;
		int red = (int) (c1.getRed() * prop1 + c2.getRed() * proportion);
		int green = (int) (c1.getGreen() * prop1 + c2.getGreen() * proportion);
		int blue = (int) (c1.getBlue() * prop1 + c2.getBlue() * proportion);
		int col = (red << 16) | (green << 8) | blue;
		return col;
	}
}

