package swing;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import images.ArrayImagePackage;

public class ClickyStretchIcon extends StretchIcon
{

	public ClickyStretchIcon() {super();};
	
	
	public static void main(String[] args) {
		ClickyStretchIcon icon = new ClickyStretchIcon(
				 ArrayImagePackage.getRandomPackage(12, 17).getIcon(true).getImage());
		
		
		JPanel pane = new JPanel();
		JFrame frame = new JFrame("Clicky stretchy icon");
		JLabel label = new JLabel(icon);
		pane.add(label);
		frame.add(pane);
		frame.setVisible(true);
		
	}
	
	
}
