package swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

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

	private void buildLayout()
	{
		layout = new GridBagLayout();
		this.setLayout(layout);
		c = new GridBagConstraints();
		c.gridy = 0; c.gridx = 0;

		c.weightx = 1; c.weighty = 1;

		c.weightx = 0; 
		c.fill = GridBagConstraints.BOTH;
//		c.fill = GridBagConstraints.NONE;
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
	}

	public static void demo_01()
	{
		JFrame frame;
//		StretchyClickyDataJLabel label1, label2;

		frame = new JFrame("Clicky stretchy drawing on icon example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1600, 1400));

		ArrayDataImageBundle bundle = ArrayDataImageBundle.createRandomPackage(12, 17);

		StretchyClickyMapAndLegendPanel map = new StretchyClickyMapAndLegendPanel(bundle, true);

		frame.add(map);
		frame.setVisible(true);

	}

//	@Override
	public void paint(Graphics g)
	{
//		int height = mapLabel.getHeight();
//		int width = mapLabel.getWidth();
//		legendLabel.setPreferredSize(new Dimension(legendWidth, height));
//		legendLabel.icon.setSize(new Dimension(legendWidth, height));
//		mapLabel.setPreferredSize(new Dimension(width, height));
//		legendLabel.setSize(new Dimension(legendWidth, height));
//		mapLabel.setSize(new Dimension(width, height));
		super.paint(g);
	}


}
