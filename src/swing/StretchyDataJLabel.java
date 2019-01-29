package swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;

import images.ArrayDataImageBundle;

public class StretchyDataJLabel extends JLabel
{
	private static final long serialVersionUID = 4027691138537175088L;
	StretchIcon icon;
	ArrayDataImageBundle bundle;
	boolean verbose = true;
	private int clickedCol, clickedRow;
	private int iconCol, iconRow;

	private MouseListener mmm = new MouseListener() {
		@Override public void mouseClicked(MouseEvent arg0) { 
			clickedCol = arg0.getX(); clickedRow = arg0.getY();
			iconCol = icon.getSourceImageColIndex(clickedCol);
			iconRow = icon.getSourceImageRowIndex(clickedRow);
			if (verbose)
			{	printClickLocation();
			printIconClickCoords();
			printIconQuery();
			}
		}

		@Override public void mouseEntered(MouseEvent arg0) {}
		@Override public void mouseExited(MouseEvent arg0) {}
		@Override public void mousePressed(MouseEvent arg0) {}
		@Override public void mouseReleased(MouseEvent arg0) {}
	};

	public StretchyDataJLabel() { addMouseListener(mmm); }
	public StretchyDataJLabel(ArrayDataImageBundle ap, boolean keepIconProportion)	{ this(ap, keepIconProportion, -9999); }
	public StretchyDataJLabel(ArrayDataImageBundle ap, int iconFixedWidth)	{ this(ap, false, iconFixedWidth); }
	public StretchyDataJLabel(ArrayDataImageBundle ap, boolean keepIconProportion, int iconFixedWidth)
	{	this();	this.bundle = ap;
	setIcon(ap.createIcon(keepIconProportion, iconFixedWidth));
	}

	public String queryIconData() { return bundle.getVal(iconCol, iconRow); }

	public static void main(String[] args) {
		JFrame frame;
		StretchyDataJLabel label1, label2;

		int legendWidth = 78;

		frame = new JFrame("Clicky stretchy icon example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(600, 400));

		label1 = new StretchyDataJLabel(
				ArrayDataImageBundle.createRandomPackage(12, 17),
				true, -9999);
		label1.setBorder(BorderFactory.createLineBorder(Color.RED, 5));

		label2 = new StretchyDataJLabel(
				ArrayDataImageBundle.createGradientBundle(label1.bundle, 10),
				false,
				legendWidth);
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

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		iconCol = icon.getX(); iconRow = icon.getY();
		if (verbose) { icon.printImageCorneCoords(); }
	}

	private void printIconQuery() {System.out.println(queryIconData()); }
	private void printClickLocation() 
	{ System.out.println("mouse click at (" + clickedCol + ", " + clickedRow + ")"); }
	private void printIconClickCoords()
	{	System.out.println("Clicked on (" + iconCol + ", " + iconRow + 
			") of icon image data");
	}

	public void setVerbose(boolean b) { this.verbose = b; }
	private void setIcon(StretchIcon icon) { this.icon = icon; super.setIcon(icon);}
}
