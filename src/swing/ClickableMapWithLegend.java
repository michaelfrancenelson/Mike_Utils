package swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import images.ArrayImagePackage;

public class ClickableMapWithLegend extends JPanel{

	public static Font DEFAULT_FONT = new Font("Serif", 1, 25);
	
	
	private boolean left;
	private String name;
	private ClickableJLabel map;
	private ClickableLegend legend;
	private ArrayImagePackage mapPackage;
	private int legendWidth;
	private double legendWeight, mapWeight;

	public ClickableMapWithLegend()
	{
		map = new ClickableJLabel();
		legend = new ClickableLegend();
	}

	
	
	public void setMap(
//			double[][] data, Color[] colors, 
			ArrayImagePackage pkg,
			String name, int legendWidth, 
			double legendWeight, double mapWeight, boolean left)
	{
		this.legendWeight = legendWeight; this.mapWeight = mapWeight;
		this.name = name;
		this.mapPackage = pkg;
		this.legendWidth = legendWidth;
		this.left = left;
		setMap();
//		setMap(mapPackage, name, legendWidth);
	}
	
	public void setMap(
			double[][] data, Color[] colors, 
			String name, int legendWidth, 
			double legendWeight, double mapWeight, boolean left)
	{
		setMap( new ArrayImagePackage(data, colors), name, legendWidth, legendWeight, mapWeight, left);
//		this.legendWeight = legendWeight; this.mapWeight = mapWeight;
//		this.name = name;
//		this.mapPackage = new ArrayImagePackage(data, colors);
//		this.legendWidth = legendWidth;
//		this.left = left;
//		setMap();
//		setMap(mapPackage, name, legendWidth);
	}
	
//	public void setMap(ArrayImagePackage pkg, String name, int legendWidth)C	{
	public void setMap()	{
//		map = new ClickableJLabel();
		map.setImage(mapPackage, name, true);
		legend.setImage(mapPackage, name, legendWidth);	
		setLayout();
	}
	
//	private void setLayout(boolean left, double legWeight, double mapWeight)
	private void setLayout()
	{
		GridBagConstraints gcLeg = new GridBagConstraints();
		GridBagConstraints gcMap = new GridBagConstraints();
		
//		gcLeg.fill = GridBagConstraints.BOTH;
		gcLeg.fill = GridBagConstraints.VERTICAL;
		gcMap.fill = GridBagConstraints.BOTH;
		
		gcLeg.weightx = legendWeight; gcLeg.weighty = 1;
		gcMap.weightx = mapWeight; gcMap.weighty = 1;
		gcMap.gridwidth = 1;
		gcMap.gridheight = 1;
		gcLeg.gridwidth = 1;
		gcLeg.gridheight = 1;
		
		setLayout(new GridBagLayout());
		
		if (left)
		{
			gcLeg.gridx = 0;
			gcMap.gridx = 1;
			add(legend, gcLeg);
			add(map, gcMap);
		}
		else 
		{
			gcLeg.gridx = 1;
			gcMap.gridx = 0;
			add(map, gcMap);
			add(legend, gcLeg);
		}
		
//		setSize(100,  100);
		
//		setWidth(1);
//		setHeight(1);
//		setVisible(true);
		
	}
	
	@Override public void paint(Graphics g)
	{
		legend.setPreferredSize(new Dimension(legendWidth, map.getHeight()));
//		legend.setPreferredSize(new Dimension(legend.getWidth(), map.getHeight()));
		System.out.println(legend.getWidth());
		
		
		super.paint(g);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("ClickableMapWithLegend test case");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1200, 1500));
		frame.setLayout(new GridLayout(1, 1));
//		frame.setLayout(new GridLayout(2, 2));
		
//		ClickableJLabel clabel1 = new ClickableJLabel();
//		clabel1.setImage(ArrayImagePackage.getRandomPackage(100,  200), "abc", true);

//		JPanel panel = new JPanel();
//		panel.setLayout(new GridBagLayout());
//		GridBagConstraints c = new GridBagConstraints();
		
//		c.fill = GridBagConstraints.BOTH;
//		c.gridwidth = 1; c.gridheight = 1;
//		c.weightx = 1; c.weighty = 1;
//		panel.add(clabel1, c);
		
//		frame.add(panel);
//		frame.setVisible(true);

		ClickableMapWithLegend map = new ClickableMapWithLegend();
		map.setMap(
				ArrayImagePackage.getRandomPackage(15, 25, ColorUtils.TERRAIN_COLORS),
				"test", 50,
				1, 5, true);
		
//		JLabel lab1 = new JLabel("lab1");
//		panel.add(lab1);
//		map.setLayout(true, 1.0, 2.0);
		
		
		
		frame.add(map);
//		frame.add(map.legend);
//		frame.add(map.map);
//		frame.pack();
		frame.setVisible(true);
	}
	
	
	
	
}
