package swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import images.ArrayImagePackage;

public class StretchyJLabel extends JLabel
{
	StretchIcon icon;

	private int iconX, iconY;
	
	public StretchyJLabel(StretchIcon icon)
	{
		this.icon = icon;
		setIcon(icon);
	}
	
	public static void main(String[] args) {
		StretchIcon icon = ArrayImagePackage.getRandomPackage(12, 17).getIcon(true);
		
		
		JPanel pane = new JPanel();
		JFrame frame = new JFrame("Clicky stretchy icon");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(600, 400));
		frame.setLayout(new GridLayout());
//		StretchyJLabel label = new StretchyJLabel(icon);
		JLabel label2 = new JLabel(icon);
		label2.setIcon(icon);
//		pane.add(label);
//		frame.add(pane);
		
		frame.add(label2);
		frame.setVisible(true);
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		iconX = icon.getX(); iconY = icon.getY();
		
		System.out.println("Icon top: " + iconY + ", icon left: " + iconX);
	}
}
