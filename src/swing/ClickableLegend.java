package swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.JFrame;

import images.ArrayDataImageBundle;
import sequeuces.Sequences;

public class ClickableLegend extends ClickableJLabel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int nColors = 200;
	private int nTicks = 7;
	private double[] legendData;
	private int[] tickY;
//	private Color labelColor;
	private int legendWidth;
	
	public static void main(String[] args) {
		
		int nIntColors = 15;
		Color[] rightColors = new Color[] {Color.green, Color.white, Color.blue, Color.red, Color.yellow};
		ClickableLegend leg1, leg2;
		ArrayDataImageBundle pkg1, pkg2;
		Random r = new Random();
		
		Color labCol = Color.black;
		Font font = new Font("Serif", 1, 25);
		
		double[][] dat1 = new double[20][30];
		int[][] dat2 = new int[21][15];

		for (int i = 0; i < dat1.length; i++) for (int j = 0; j < dat1[0].length; j++) dat1[i][j] = r.nextDouble();
		for (int i = 0; i < dat2.length; i++) for (int j = 0; j < dat2[0].length; j++) dat2[i][j] = r.nextInt(nIntColors);

		pkg1 = new ArrayDataImageBundle(dat1, rightColors);
//		pkg2 = new ArrayImagePackage(dat2, rightColors);
		pkg2 = new ArrayDataImageBundle(dat2, ColorUtils.GRAYS);
		
		leg1 = new ClickableLegend(pkg1, "testing", 50);
		leg2 = new ClickableLegend(pkg2, "integer", 75);
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1200, 1500));
		frame.setLayout(new GridLayout(1, 2));
		
		frame.add(leg1);
		frame.add(leg2);

		frame.setVisible(true);
		
		leg1.drawLabels(font, labCol, 0, 0, 0.05);
		leg2.drawLabels(font, labCol, 0, .01, 0.05);
	}

	public ClickableLegend() {}; 
	
	public void drawLabels(Font font, Color labelColor, double adjX, double adjY, double endpointOffset)
	{
		int offset = (int) ((double) (nColors) * endpointOffset);
		tickY = Sequences.intSequence(0 + offset, nColors - 1 - offset, nTicks);
		drawValueStrings(font, labelColor, 0, tickY, adjX, adjY);
	}
	
	private void buildData() 
	{ 
		legendData = Sequences.doubleIntervals(
				ap.getMin(), ap.getMax(), nColors);
		}
	
	public ClickableLegend(ArrayDataImageBundle pkg, String name, int width)
	{
		this.ap = pkg;
		this.name = name;
		this.legendWidth = width;
//		this.font = font;
		setImage(pkg, name, width);
	}
	
	public void setImage(ArrayDataImageBundle pkg, String name, int width)
	{
		this.ap = pkg;
		buildData();
		this.ap = new ArrayDataImageBundle(legendData, ap.getCi());
		this.name = name;
		this.legendWidth = width;
//		this.proportionate = true;
//		this.proportionate = false;
//		icon = ap.getIcon(true);
		icon = ap.createIcon(false);
		icon.setConstantWidth(legendWidth);
		setIcon(icon);
	}
	
	@Override public void paint(Graphics g)
	{
		super.paint(g);
		System.out.println(getWidth());
	}
}
