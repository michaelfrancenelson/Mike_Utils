package _swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import images.ArrayDataImageBundle;
import images.ArrayDataImageBundle.ArrayDataImageFactory;
import sequeuces.Sequences;

public class StretchyClickyMapAndLegendPanel extends JPanel
{
	/** */ protected static final long serialVersionUID = 5932759068993864983L;
	protected boolean              left;
	protected ArrayDataImageBundle bundle;
	protected int                  legendWidth;

	protected int      nLegendTicks = 5;
	protected int      nLegendSteps = 300;
//	private boolean    isBoolean;
	
	protected double[] legendBreaksDouble;
	protected int[]    legendBreaksInt;
	protected double   legendTicksBuffer = 0.02;
	
	protected Double   legendTextOffsetX, legendTextOffsetY;
	
	Font  legendFont; 
	Color legendFontColor;
	
	protected StretchyClickyDataJLabel mapLabel, legendLabel;

//	public void setBoolean(boolean b) { 
//		this.isBoolean = b; }
	
	
	public void setHoverPane(JTextPane pane)
	{
		mapLabel.setHoverPane(pane);
		legendLabel.setHoverPane(pane);
	}
	
	public static StretchyClickyMapAndLegendPanel factory(
			ArrayDataImageBundle bundle, boolean left, Font font, Color fontColor)
	{ return factory(bundle, left, font, fontColor, 300, 5, 100, 0.02, null, null); }
	
	public static StretchyClickyMapAndLegendPanel factory(
			ArrayDataImageBundle bundle, boolean left, Font font, Color fontColor,
			int nLegendSteps, int nLegendTicks, int legendWidth, double legendTicksBuffer,
			Double legendTextOffsetX, Double legendTextOffsetY)
	{
		StretchyClickyMapAndLegendPanel s = new StretchyClickyMapAndLegendPanel();
		s.left = left;
		s.legendFont = font;
		s.legendFontColor = fontColor;
		s.bundle = bundle;
		s.nLegendSteps = nLegendSteps;
		s.nLegendTicks = nLegendTicks;
		s.legendWidth = legendWidth;
		s.legendTicksBuffer = legendTicksBuffer;
		s.legendTextOffsetX = legendTextOffsetX;
		s.legendTextOffsetY = legendTextOffsetY;
		s.buildMaps();
		s.buildLayout();
		return s;		
	}

	public void setPointColors(Color[] colors, int min) { mapLabel.buildPointColorInterpolator(colors, min); }
	
	protected void buildMaps()
	{
		this.mapLabel = StretchyClickyDataJLabel.factory(bundle, true);
		this.legendLabel = StretchyClickyDataJLabel.factory(
				ArrayDataImageFactory.getVerticalGradientBundle(
						bundle, nLegendSteps), 
				legendWidth, legendTextOffsetX, legendTextOffsetY);
		legendLabel.setMatchIcon(mapLabel.icon);
	}

	public void setLegendLabels(Color color, Font font)
	{ this.legendFontColor = color; this.legendFont = font; setLegendLabels(); }
	
	public void setLegendLabels()
	{
		int nRows  = ((StretchyClickyIcon)legendLabel.getIcon()).getImageDataNRows();
		int nTicks = nLegendTicks;
		int offset = (int) (legendTicksBuffer * ((double)nRows));
		
		int start = 0 + offset;
		int end   = nRows - 1 - offset;
			
		int[] ticksRows = Sequences.spacedIntervals(start, end, nTicks);
		
//		if (isBoolean)	legendLabel.drawTextOnIcon(
//				ticksRows, new int[] {0}, new String[] {"false",  "true"},
//				legendFontColor, legendFont);
//		else
		legendLabel.drawTextValuesOnIcon(ticksRows, 0, legendFontColor, legendFont); 
	}
	
	public void reset(ArrayDataImageBundle bundle)
	{ update(bundle); mapLabel.deletePoints(); }
	
	public void update(ArrayDataImageBundle bundle)
	{
		this.bundle = bundle;
		mapLabel.updateBundle(this.bundle);
		legendLabel.updateBundle(
				ArrayDataImageFactory.getVerticalGradientBundle(
						bundle, legendWidth), mapLabel.icon);
		setLegendLabels();
	}

	protected void buildLayout()
	{
		this.setLayout(new GridBagLayout());
		GridBagConstraints   c;

		c = new GridBagConstraints();
		c.gridy = 0; c.gridx = 0; c.weightx = 1; c.weighty = 1;

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0; 
		if (left) c.gridx = 0; else c.gridx = 1;
		add(Box.createHorizontalGlue(), c);
		add(Box.createHorizontalStrut(legendWidth), c);
		add(legendLabel, c);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		if (left) c.gridx = 1; else c.gridx = 0;
		add(mapLabel, c);
	}

	public void drawPointsOnMap(int[] dataArrayRows, int[] dataArrayColumns, Color color, double scale)
	{ mapLabel.drawPointsOnIcon(dataArrayColumns, dataArrayRows, color, scale); }
	
	public void drawPointsOnMap(int[] dataArayRows, int[] dataArrayColumns, int[] colCodes, double scale)
	{ mapLabel.drawPointsOnIcon(dataArayRows, dataArrayColumns, colCodes, scale); }
	
	public void erasePointsOnmap() { mapLabel.hidePoints(); }

	public static void main(String[] args) 
	{
//		demo_01();
		demo_02();
//		demo_update();
	}

//	@Override	public void paint(Graphics g) { super.paint(g);	}
	
	public static void demo_update()
	{
		JFrame frame = StretchyClickyDataJLabel.getFrame("updating map demo");
		
		ArrayDataImageBundle bundle = ArrayDataImageFactory.getRandomBundle(12, 17);
		StretchyClickyMapAndLegendPanel map =
				StretchyClickyMapAndLegendPanel.factory(bundle, true, frame.getFont(), Color.black);

		frame.add(map);
		frame.setVisible(true);

		long interval = 1;
		long second = LocalTime.now().getSecond();

		List<Color[]> colorList = new ArrayList<>();
		colorList.add(ColorUtils.GRAYS);
		colorList.add(ColorUtils.GREENS);
		colorList.add(ColorUtils.TOPO_COLORS);
		colorList.add(ColorUtils.HEAT_COLORS);

		Random r = new Random();

		while (true)
		{
			if (LocalTime.now().getSecond() - second > interval)
			{
				second =          LocalTime.now().getSecond();
				int nRow =        r.nextInt(50) + 10;
				int nCol =        r.nextInt(50) + 10;
				int nColors =     r.nextInt(75) + 20;
				int whichColors = r.nextInt(colorList.size());

				map.update(ArrayDataImageFactory.getRandomBundle(
								nRow, nCol, nColors, colorList.get(whichColors)));
				map.setLegendLabels(Color.black, new Font("serif", 1, 26));
			}
		}
	}

	public static void demo_01()
	{
		JFrame frame = StretchyClickyDataJLabel.getFrame("Draw on map demo");
		
		ArrayDataImageBundle bundle = ArrayDataImageFactory.getRandomBundle(12, 17, 200);
		StretchyClickyMapAndLegendPanel map = StretchyClickyMapAndLegendPanel.factory(
				bundle, true, frame.getFont(), Color.black,
				300, 5, 100, 0.05, 
				-0.5, null);
		map.setLegendLabels(Color.black, frame.getFont());
		frame.add(map); frame.setVisible(true);
	}
	public static void demo_02()
	{
		JFrame frame = StretchyClickyDataJLabel.getFrame("Draw on map demo");
		
		ArrayDataImageBundle bundle = ArrayDataImageFactory.getRandomBundle(12, 17, 2);
		bundle.setBoolean(true);
		StretchyClickyMapAndLegendPanel map = 
				StretchyClickyMapAndLegendPanel.factory(
				bundle, true, frame.getFont(), Color.black,
				300, 5, 100, 0.05, 
				-0.5, null);
		map.setLegendLabels(Color.black, frame.getFont());
		frame.add(map); frame.setVisible(true);
	}
}
