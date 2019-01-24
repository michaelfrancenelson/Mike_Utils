package images;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class ClickableJLabel extends JLabel
{
	private StretchIcon icon;

	/**	 */	private static final long serialVersionUID = 1L;
	ArrayImagePackage ap;
	private int clickedX, clickedY;
	MouseListener mmm = new MouseListener() {
		int imgOriginX, imgOriginY, imgWidth, imgHeight;
		@Override public void mouseClicked(MouseEvent arg0) { 
			clickedX = arg0.getX(); clickedY = arg0.getY();
			imgOriginX = icon.getX(); imgOriginY = icon.getY();

			imgWidth = icon.getImageWidth();
			imgHeight = icon.getImageHeight();

			System.out.println(this.getClass().getName());
			printClickedVal();
			System.out.println();

			//			printClickLocation();
			//			printImageOrigin();
			//			printImageDimensions();
			//			printImageCoords();
			//			printImageRel();
		}
		
		@Override public void mouseEntered(MouseEvent arg0) {}
		@Override public void mouseExited(MouseEvent arg0) {}
		@Override public void mousePressed(MouseEvent arg0) {}
		@Override public void mouseReleased(MouseEvent arg0) {}

		void printClickedVal()
		{
			String[] query = getClickedValue();
			System.out.println("Query at " + ap.getQueryX() + ", " + ap.getQueryY() + ": " + query[2]);
		}

		int getImgX() { return Math.min(imgWidth, Math.max(0, clickedX - imgOriginX)); }
		int getImgY() { return Math.min(imgHeight, Math.max(0, clickedY - imgOriginY)); }

		double relX() { return (double) getImgX() / (double) imgWidth;}
		double relY() { return (double) getImgY() / (double) imgHeight;}

		//		void printImageCoords() { System.out.println(String.format(
		//				"Coords within image: (%d, %d)", getImgX(), getImgY()));}
		//		void printImageRel() { System.out.println(String.format(
		//				"Relative position within image: (%2.2f, %2.2f)",
		//				relX(), relY()));}
		//		void printImageDimensions()	{System.out.println("image height: " + imgHeight + ", img width: " + imgWidth);	}
		//		void printImageOrigin()	{ System.out.println("image origin at (" + icon.getX() + ", " + icon.getY() + ")"); }
		//		void printClickLocation() { System.out.println("mouse click at (" +	clickedX + ", " + clickedY + ")"); }

		String[] getClickedValue()
		{
			ap.setQueryCoords(relX(), relY());
			return new String[] {ap.getQueryX(), ap.getQueryY(), ap.getVal()};
		}
	};

	public ClickableJLabel(ArrayImagePackage pkg)
	{
		ap = pkg;
		icon = ap.getIcon();
		setIcon(icon);
		addMouseListener(mmm);
	}

	/** simple test case with simulated data */
	public static void main(String[] args) {
		Random r = new Random();

		int width = 15, height = 10;

		int nIntColors = 15;

		double[][] dat1 = new double[20][30];
		double[][] dat2 = new double[height][width];

		int[][] dat3 = new int[21][15];
		int[][] dat4 = new int[height][width];

		for (int i = 0; i < dat1.length; i++) for (int j = 0; j < dat1[0].length; j++) dat1[i][j] = r.nextDouble();
		for (int row = 0; row < height; row++) for (int col = 0; col < width; col++) dat2[row][col] = col;

		for (int i = 0; i < dat3.length; i++) for (int j = 0; j < dat3[0].length; j++) dat3[i][j] = r.nextInt(nIntColors);
		for (int row = 0; row < height; row++) for (int col = 0; col < width; col++) dat4[row][col] = row;

		ColorInterpolator ci1 = new ColorInterpolator(new Color[] {Color.black, Color.blue, Color.red, Color.yellow}, 0.0, 1.0, -9999, Color.gray);
		ColorInterpolator ci2 = new ColorInterpolator(new Color[] {Color.black, Color.green}, 0, width, -9999, Color.gray);
		ColorInterpolator ci3 = new ColorInterpolator(new Color[] {Color.black, Color.blue, Color.red}, 0, nIntColors, -9999, Color.gray);
		ColorInterpolator ci4 = new ColorInterpolator(new Color[] {Color.black, Color.green}, 0, height, -9999, Color.gray);

		ArrayImagePackage ap1 = new ArrayImagePackage(dat1, ci1);
		ArrayImagePackage ap2 = new ArrayImagePackage(dat2, ci2);
		ArrayImagePackage ap3 = new ArrayImagePackage(dat3, ci3);
		ArrayImagePackage ap4 = new ArrayImagePackage(dat4, ci4);


		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1200, 1500));
		frame.setLayout(new GridLayout(2, 2));

		ClickableJLabel lab = new ClickableJLabel(ap1);
		ClickableJLabel lab2 = new ClickableJLabel(ap2);
		ClickableJLabel lab3 = new ClickableJLabel(ap3);
		ClickableJLabel lab4 = new ClickableJLabel(ap4);

		frame.add(lab);
		frame.add(lab2);
		frame.add(lab3);
		frame.add(lab4);
		//		frame.setResizable(true);
		frame.setVisible(true);

	}

	@Override public StretchIcon getIcon() { return icon; }
}
