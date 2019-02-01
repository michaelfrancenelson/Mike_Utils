package swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;

import images.ArrayDataImageBundle;

public class StretchyClickyMapAndLegendPanel extends JPanel
{
	/** */ private static final long serialVersionUID = 5932759068993864983L;

	boolean left;
	ArrayDataImageBundle bundle;
	GridBagLayout layout;
	GridBagConstraints c;
	int legendWidth = 150;

	private StretchyClickyDataJLabel mapLabel, legendLabel;

	public StretchyClickyMapAndLegendPanel(ArrayDataImageBundle bundle, boolean left)
	{
		this.left = left;
		this.bundle = bundle;
		buildMaps();
		buildLayout();
	}

	private void buildMaps()
	{
		this.mapLabel = new StretchyClickyDataJLabel(bundle, true);
		this.legendLabel = new StretchyClickyDataJLabel(ArrayDataImageBundle.createGradientBundle(bundle, legendWidth), legendWidth);
	}

	public void update(ArrayDataImageBundle bundle)
	{
		this.bundle = bundle;
		mapLabel.updateBundle(this.bundle);
		legendLabel.updateBundle(ArrayDataImageBundle.createGradientBundle(bundle, legendWidth));
	}

	private void buildLayout()
	{
		layout = new GridBagLayout();
		this.setLayout(layout);
		c = new GridBagConstraints();
		c.gridy = 0; c.gridx = 0;

		c.weightx = 1; c.weighty = 1;

		c.weightx = 0; 
		c.fill = GridBagConstraints.BOTH;
		if (left)
			c.gridx = 0;
		else
			c.gridx = 1;
		add(Box.createHorizontalGlue(), c);
		add(Box.createHorizontalStrut(legendWidth), c);
		add(legendLabel, c);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		if (left)
			c.gridx = 1;
		else c.gridx = 0;
		add(mapLabel, c);
	}

	public static void main(String[] args) 
	{
		demo_01();
		demo_update();
	}

	public static void demo_update()
	{
		JFrame frame;
		frame = new JFrame("Updating map demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1600, 1400));

		ArrayDataImageBundle bundle = ArrayDataImageBundle.createRandomPackage(12, 17);
		StretchyClickyMapAndLegendPanel map = new StretchyClickyMapAndLegendPanel(bundle, true);

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
				second = LocalTime.now().getSecond();
				map.update(
						ArrayDataImageBundle.createRandomPackage(
								r.nextInt(50) + 10, r.nextInt(50) + 10,
								r.nextInt(50) + 1, colorList.get(r.nextInt(3))));
			}
		}
	}

	public static void demo_01()
	{
		JFrame frame;

		frame = new JFrame("Clicky stretchy drawing on icon example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1600, 1400));

		ArrayDataImageBundle bundle = ArrayDataImageBundle.createRandomPackage(12, 17);
		StretchyClickyMapAndLegendPanel map = new StretchyClickyMapAndLegendPanel(bundle, true);

		frame.add(map);
		frame.setVisible(true);
	}

//	public void paint(Graphics g)
//	{
//		super.paint(g);
//	}
}
