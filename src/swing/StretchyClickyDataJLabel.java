package swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalTime;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;

import images.ArrayDataImageBundle;


/** A JLabel displaying data that can be queried by coordinates in the original
 *  size of the data array. <br>
 *  
 *  Has the following drawing functionality: <br>
 *  
 * @author Home
 *
 */
public class StretchyClickyDataJLabel extends JLabel
{
	private static final long serialVersionUID = 4027691138537175088L;
	public static final Font DEFAULT_FONT = new Font("Serif", Font.BOLD, 50);
	
	StretchyClickyIcon icon;
	ArrayDataImageBundle bundle;
	boolean verbose = false;

	boolean addPoints = false;
	private int[] pointsX, pointsY;
	private Color pointColor;

	boolean addText = false;
	private int[] textX, textY;
	private String[] textLabels;
	private Color textColor = Color.black;
	private Font font = DEFAULT_FONT;
	
	boolean keepIconProportion;
	int iconFixedWidth;
	
	private MouseListener mmm = new MouseListener() {
		@Override public void mouseClicked(MouseEvent arg0) { 
			icon.setMouseClick(arg0);

			if (verbose)
			{
				icon.printComponentClick();
				icon.printDisplayImageClick();
				icon.printComponentClickRatio();
				icon.printDisplayImageClickRatio();
				icon.printDataClick();
			}
			printIconQuery();
		}

		@Override public void mouseEntered(MouseEvent arg0) {}
		@Override public void mouseExited(MouseEvent arg0) {}
		@Override public void mousePressed(MouseEvent arg0) {}
		@Override public void mouseReleased(MouseEvent arg0) {}
	};

	public StretchyClickyDataJLabel() { addMouseListener(mmm); }
	public StretchyClickyDataJLabel(ArrayDataImageBundle ap, boolean keepIconProportion)	{ this(ap, keepIconProportion, -9999); }
	public StretchyClickyDataJLabel(ArrayDataImageBundle ap, int iconFixedWidth)	{ this(ap, false, iconFixedWidth); }
	public StretchyClickyDataJLabel(ArrayDataImageBundle ap, boolean keepIconProportion, int iconFixedWidth)
	{	
		this();	this.bundle = ap; 
		this.keepIconProportion = keepIconProportion;
		this.iconFixedWidth = iconFixedWidth;
//		setIcon(ap.createIcon(this.keepIconProportion, this.iconFixedWidth)); 
		setIcon();
		}

	public void updateBundle(ArrayDataImageBundle bundle)
	{
		this.bundle = bundle;
		setIcon();
	}

	
	private void setIcon()
	{	setIcon(bundle.createIcon(this.keepIconProportion, this.iconFixedWidth)); }
	
	public void printIconQuery()
	{
		System.out.println(String.format(
				"Value of %s at (%d, %d): %s", 
				bundle.getName(), icon.getClickedDataColumn(), icon.getClickedDataRow(),
				queryIconData()));
	}
	
	public String queryIconData() { return bundle.getVal(icon.getClickedDataColumn(), icon.getClickedDataRow()); }

	public static void main(String[] args) {
		demo_draw_points_on_icon();
		demo_update_bundle();
	}

	
	public static void demo_update_bundle()
	{
		JFrame frame;
		StretchyClickyDataJLabel label1;
		
		frame = new JFrame("Clicky stretchy drawing on icon example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1600, 1400));
		
		label1 = new StretchyClickyDataJLabel(
				ArrayDataImageBundle.createRandomPackage(12, 17),
				true, -9999);
		
		frame.add(label1);
		frame.setVisible(true);
		
		Random r = new Random();
		
		long interval = 1;
		long second = LocalTime.now().getSecond();
		while (true)
		{
			if (LocalTime.now().getSecond() - second > interval)
			{
				second = LocalTime.now().getSecond();
				label1.updateBundle(ArrayDataImageBundle.createRandomPackage(12, 17, r.nextInt(10) + 1));
			}
		}
		
	}
	
	public static void demo_draw_points_on_icon()
	{
		JFrame frame;
		StretchyClickyDataJLabel label1, label2;
		
		frame = new JFrame("Clicky stretchy drawing on icon example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1600, 1400));

		label1 = new StretchyClickyDataJLabel(
				ArrayDataImageBundle.createRandomPackage(12, 17),
				true, -9999);
		label1.setBorder(BorderFactory.createLineBorder(Color.RED, 5));

		label2 =  new StretchyClickyDataJLabel(
				ArrayDataImageBundle.createRandomPackage(12, 17),
				true, -9999);
		
		label2.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
		
		frame.setLayout(new GridLayout());
//		frame.setLayout(new GridBagLayout());
//		GridBagConstraints c = new GridBagConstraints();
//		c.fill = GridBagConstraints.BOTH;
//		c.weightx = 1; c.weighty = 1;
//		c.gridy = 0; c.gridx = 0;
		frame.add(label1);
		frame.add(label2);
		frame.setVisible(true);
		
		int[] x = new int[] {1, 4, 6};
		int[] y = new int[] {3, 4, 6};
		String[] labels = new String[] {"A", "b", "cCcCc" };

		label1.points(x, y, Color.BLUE);
		label2.text(x, y, labels, Color.BLACK, StretchyClickyDataJLabel.DEFAULT_FONT);
	}
	
	public void points(int[] x, int y[], Color color)
	{
		this.pointsX = x; this.pointsY = y;
		this.addPoints = true;
		this.pointColor = color;
		repaint();
	}
	
	public void text(int[] x, int[] y, String[] labels, Color color, Font font)
	{	
		this.addText = true;
		this.textColor = color; this.font = font;
		this.textX = x; this.textY = y; this.textLabels = labels;
	}
	public void eraseText() { this.addText = false; repaint(); }
	public void erasePoints() { this.addPoints = false; repaint(); }
	
	private void drawText(Graphics g)
	{
		g.setColor(this.textColor);	
		g.setFont(this.font);
		int pix = icon.getDataPixelWidth();
		
		for (int i = 0; i < textY.length; i++) 
		{
			int displayCol = icon.componentColumnFromDataCol(textY[i]);
//			int displayRow = icon.componentRowFromDataRow(textX[i]);
//			int displayCol = icon.componentColumnFromDataCol(textY[i]) - pix / 2;
			int displayRow = icon.componentRowFromDataRow(textX[i]) + pix;
//			g.fillOval(displayCol, displayRow, pix, pix);
			g.drawString(this.textLabels[i], displayCol, displayRow);
		}
	}
	
	private void drawPoints(Graphics g)
	{
		int pix = icon.getDataPixelWidth();
		g.setColor(this.pointColor);	
		
		for (int i = 0; i < pointsY.length; i++) 
		{
			int displayCol = icon.componentColumnFromDataCol(pointsY[i]);
			int displayRow = icon.componentRowFromDataRow(pointsX[i]);
			g.fillOval(displayCol, displayRow, pix, pix);
		}	
	}
	
	
	public static void demo_01()
	{
		JFrame frame;
		StretchyClickyDataJLabel label1, label2;

		int legendWidth = 78;

		frame = new JFrame("Clicky stretchy icon example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(600, 400));

		label1 = new StretchyClickyDataJLabel( ArrayDataImageBundle.createRandomPackage(12, 17), true, -9999);
		label1.setBorder(BorderFactory.createLineBorder(Color.RED, 5));

		label2 = new StretchyClickyDataJLabel(
				ArrayDataImageBundle.createGradientBundle(label1.bundle, 10),
				false, legendWidth);
		label2.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
		label2.setMaximumSize(new Dimension(legendWidth, Integer.MAX_VALUE));

		frame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1; c.weighty = 1;
		c.gridy = 0; c.gridx = 0;
		frame.add(label1, c);
		//		c.fill = GridBagConstraints.VERTICAL;
		//		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		//		
		c.weightx = 0;

		c.gridx = 1;
		frame.add(Box.createHorizontalGlue(), c);
		frame.add(Box.createHorizontalStrut(legendWidth), c);
		frame.add(label2, c);

		frame.setVisible(true);
	}

	public void paint(Graphics g)
	{
		super.paint(g);
		
		if (verbose) { 
			icon.printSourceImageDimensions();
			icon.printDisplayImageDimensions(); 
			icon.printDisplayImageCornerCoords();
		}
		
		if (addPoints) drawPoints(g);
		if (addText) drawText(g);
	}
	
	
	public void setVerbose(boolean b) { this.verbose = b; }
	private void setIcon(StretchyClickyIcon icon) { this.icon = icon; super.setIcon(icon);}
	public Graphics getIconGraphics() { return icon.getImage().getGraphics(); }
}
