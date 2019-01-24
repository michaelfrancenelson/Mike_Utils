package images;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

public class ResizingJLabelImage extends JLabel {
	
	
	BufferedImage sourceImg;
	private int clickedX;
	private int clickedY;
	
	
	public ResizingJLabelImage(String label, BufferedImage img)
	{
		setText(label);
		this.sourceImg = img;
		
//		setSize(1, 1);
		
		addMouseListener(new MouseListener() {
			
			@Override public void mouseClicked(MouseEvent arg0) { 
				clickedX = arg0.getX(); clickedY = arg0.getY();
				printClickCoords();
//				getClickedVal(true);
				}
			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mousePressed(MouseEvent arg0) {}
			@Override public void mouseReleased(MouseEvent arg0) {}
		});
		
	}

	private void printClickCoords()
	{
		
	System.out.println("(" + clickedX + ", " + clickedY + ")");;
	}
	
}
