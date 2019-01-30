//package swing;
//
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.Graphics;
//import java.awt.GridLayout;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.util.Random;
//
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//
//import images.ArrayDataImageBundle;
//import images.ColorInterpolator;
//
//public class ClickableJLabel extends JLabel
//{
//	/**	 */	private static final long serialVersionUID = 1L;
//
//	protected StretchyClickyIcon icon;
//	protected String name = "Array";
//	private int imgOriginX, imgOriginY, imgWidth, imgHeight;
//	protected double adjX, adjY;
//
//	protected ArrayDataImageBundle ap;
//	private int clickedX, clickedY;
//
//	private boolean drawPoints = false, drawText = false;
//	private int[] pointsX = new int[0], pointsY = new int[0], textX = new int[0], textY = new int[0];
//
//	private Color pointsColor, stringColor;
//
//	private int pointsSize;
//	protected Font font;
//	private String[] text = new String[0];
//	
//	protected boolean proportionate;
//	
//	private MouseListener mmm = new MouseListener() {
//		@Override public void mouseClicked(MouseEvent arg0) { 
//			clickedX = arg0.getX(); clickedY = arg0.getY();
//
//			System.out.println(this.getClass().getName());
//			printClickedVal();
//			System.out.println();
//
//			printClickLocation();
//			printImageOrigin();
//			printImageDimensions();
//			printImageCoords();
//			printImageRel();
//		}
//
//		@Override public void mouseEntered(MouseEvent arg0) {}
//		@Override public void mouseExited(MouseEvent arg0) {}
//		@Override public void mousePressed(MouseEvent arg0) {}
//		@Override public void mouseReleased(MouseEvent arg0) {}
//
//		void printClickedVal()
//		{	String[] query = getClickedValue();
//			System.out.println("Query of " + name + " at (" + ap.getQueryX() + ", " + ap.getQueryY() + "): " + query[2]); }
//	};
//
//	public ClickableJLabel() {addMouseListener(mmm);}
//
//	
//	public void setImage(ArrayDataImageBundle pkg, String name, boolean proportionate)
//	{
//		this.proportionate = proportionate;
//		this.name = name;
//		this.ap = pkg;
//		icon = ap.createIcon(this.proportionate);
//		setIcon(icon);
//	}
//	
//	private void drawStrings(Font font, Color color, int[] textX, int[] textY, String[] text)
//	{
//		this.drawText = true;
//		this.stringColor = color;
//		this.textX = textX;
//		this.textY = textY;
//		this.font = font;
//		this.text = text;
//		repaint();
//	}
//	
//	public void drawPoints(int[] x, int[] y, int size, Color color)
//	{
//		this.drawPoints = true;
//		this.pointsColor = color;
//		this.pointsSize = size;
//		this.pointsX = x;
//		this.pointsY = y;
//		repaint();
//	}
//
//	public void drawValueStrings(Font font, Color color, int textX, int[] textY, double adjX, double adjY)
//	{
//		this.adjX = adjX; this.adjY = adjY;
//		int[] x = new int[textY.length];
//		for (int i = 0; i < x.length; i++) 
//			x[i] = textX;
//		drawValueStrings(font, color, x, textY, adjX, adjY);
//	}
//	
//	public void drawValueStrings(Font font, Color color, int[] textX, int textY, double adjX, double adjY)
//	{
//		this.adjX = adjX; this.adjY = adjY;
//		int[] y = new int[textX.length];
//		for (int i = 0; i < y.length; i++) 
//			y[i] = textY;
//		drawValueStrings(font, color, textX, y, adjX, adjY);
//	}
//
//	public void drawValueStrings(Font font, Color color, int[] textX, int[] textY, double adjX, double adjY)
//	{
//		this.adjX = adjX; this.adjY = adjY;
//		String[] s = new String[textX.length];
//		for (int i = 0; i < textX.length; i++)
//			s[i] = ap.getVal(textX[i], textY[i]);
//		drawStrings(font, color, textX, textY, s);
//	}
//	
//	private void drawStringsOnIconImage(
//			Graphics g, Font font, Color color, int[] textX, int[] textY, String[] text, double adjX, double adjY)
//	{
//		g.setColor(color);
//		g.setFont(font);
//		
//		for (int i = 0; i < textX.length; i++)
//		{
//			int col = textX[i]; int row = textY[i];
//			int x = imageXToGraphicsCoord(col, adjX);
//			int y = imageYToGraphicsCoord(row, adjY);
//			g.drawString(text[i], x, y);
//		}		
//	}
//	
//	private void drawPointsOnIconImage(Graphics g, Color color, int[] imageX, int [] imageY, int size)
//	{
//		g.setColor(color);
//		for (int i = 0; i < imageX.length; i++)
//		{
//			int col = imageX[i]; int row = imageY[i];
//			int x = imageXToGraphicsCoord(col, 0.5);
//			int y = imageYToGraphicsCoord(row, 0.5);
//			g.fillOval(x - size / 2, y - size / 2, size, size);
//		}
//	}
//
//	public void paint(Graphics g) {
//		super.paint(g);
//
//		imgOriginX = icon.getX(); imgOriginY = icon.getY();
//		imgWidth = icon.getImageWidth(); imgHeight = icon.getImageHeight();
//
//		if (drawPoints)
//			drawPointsOnIconImage(g, pointsColor, pointsX, pointsY, pointsSize);
//		if (drawText)
//			drawStringsOnIconImage(g, font, stringColor, textX, textY, text, adjX, adjY);
//	}	
//
//
//	/** simple test case with simulated data */
//	public static void main(String[] args) {
//		Random r = new Random();
//
//		int width = 15, height = 10;
//
//		int nIntColors = 15;
//
//		double[][] dat1 = new double[20][30];
//		double[][] dat2 = new double[height][width];
//
//		int[][] dat3 = new int[21][15];
//		int[][] dat4 = new int[height][width];
//
//		for (int i = 0; i < dat1.length; i++) for (int j = 0; j < dat1[0].length; j++) dat1[i][j] = r.nextDouble();
//		for (int row = 0; row < height; row++) for (int col = 0; col < width; col++) dat2[row][col] = col;
//
//		for (int i = 0; i < dat3.length; i++) for (int j = 0; j < dat3[0].length; j++) dat3[i][j] = r.nextInt(nIntColors);
//		for (int row = 0; row < height; row++) for (int col = 0; col < width; col++) dat4[row][col] = row;
//
//		ColorInterpolator ci1 = new ColorInterpolator(new Color[] {Color.black, Color.blue, Color.red, Color.yellow}, 0.0, 1.0, -9999, Color.gray);
//		ColorInterpolator ci2 = new ColorInterpolator(new Color[] {Color.black, Color.green}, 0, width, -9999, Color.gray);
//		ColorInterpolator ci3 = new ColorInterpolator(new Color[] {Color.black, Color.blue, Color.red}, 0, nIntColors, -9999, Color.gray);
//		ColorInterpolator ci4 = new ColorInterpolator(new Color[] {Color.black, Color.green}, 0, height, -9999, Color.gray);
//
//		ArrayDataImageBundle ap1 = new ArrayDataImageBundle(dat1, ci1);
//		ArrayDataImageBundle ap2 = new ArrayDataImageBundle(dat2, ci2);
//		ArrayDataImageBundle ap3 = new ArrayDataImageBundle(dat3, ci3);
//		ArrayDataImageBundle ap4 = new ArrayDataImageBundle(dat4, ci4);
//
//		JFrame frame = new JFrame();
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setSize(new Dimension(1200, 1500));
//		frame.setLayout(new GridLayout(2, 2));
//
//		ClickableJLabel lab = new ClickableJLabel();
//		ClickableJLabel lab2 = new ClickableJLabel();
//		ClickableJLabel lab3 = new ClickableJLabel();
//		ClickableJLabel lab4 = new ClickableJLabel();
//		
//		lab.setImage(ap1, "Array1", true);
//		lab2.setImage(ap2, "Array1", true);
//		lab3.setImage(ap3, "Array1", true);
//		lab4.setImage(ap4, "Array1", true);
//		
//		
//		lab = new ClickableJLabel(ap1.createIcon(true));
////		ClickableJLabel lab = new ClickableJLabel(ap1);
////		ClickableJLabel lab2 = new ClickableJLabel(ap2);
////		ClickableJLabel lab3 = new ClickableJLabel(ap3);
////		ClickableJLabel lab4 = new ClickableJLabel(ap4);
//
//		
//		
//		
//		int[] x, y, x2, y2, x3, y3;
//
//		x = new int[] {0, 5, 10};
//		y = new int[] {6, 3, 17};
//		x2 = new int[] {1, 4, 6};
//		y2 = new int[] {height / 2, height / 2, height / 2};
//		x3 = new int[] {width / 2, width / 2, width / 2};
//		y3 = new int[] {1, 4, 6};
//		
//		
//		String[] s = new String[] {"A", "b", "123"};
//		Color textColor = Color.yellow;
//		int ptSize = 50;
//		Font font = new Font("Serif", Font.BOLD, ptSize);
//		
//		Color ptColor = Color.green;
//
//		frame.add(lab);
//		frame.add(lab2);
//		frame.add(lab3);
//		frame.add(lab4);
//		frame.setVisible(true);
//
//		lab3.drawStrings(font, textColor, x, y, s);
//		lab.drawValueStrings(font, Color.white, 4, y, 0.8, 0.5);
//		lab.drawValueStrings(font, Color.white, x, 5, 1.2, 0.0);
//		
//		lab.drawPoints(x, y, ptSize, ptColor);
//		lab3.drawPoints(x, y, ptSize, Color.black);
//		
//		lab2.drawPoints(x2, y2, ptSize, Color.pink);
//		lab4.drawPoints(x3, y3, ptSize, Color.red);
//		lab4.drawValueStrings(font, Color.white, x3, y3, 0.5, -0.2);
//	}
//
//	String[] getClickedValue()
//	{
//		ap.setQueryCoords(imageRelX(), imageRelY());
//		return new String[] {ap.getQueryX(), ap.getQueryY(), ap.getVal()};
//	}
//
//	void printImageCoords() { System.out.println(String.format(
//			"Coords within image: (%d, %d)", getImgX(), getImgY()));}
//	void printImageRel() { System.out.println(String.format(
//			"Relative position within image: (%2.2f, %2.2f)",
//			imageRelX(), imageRelY()));}
//	void printImageDimensions()	{System.out.println("image height: " + imgHeight + ", img width: " + imgWidth);	}
//	void printImageOrigin()	{ System.out.println("image origin at (" + icon.getX() + ", " + icon.getY() + ")"); }
//	void printClickLocation() { System.out.println("mouse click at (" +	clickedX + ", " + clickedY + ")"); }
//
//	int getImgX() { return Math.min(imgWidth, Math.max(0, clickedX - imgOriginX));}
//	int getImgY() { return Math.min(imgHeight, Math.max(0, clickedY - imgOriginY)); }
//
//	double imageRelX() { return relativeCoord(getImgX(), imgWidth); } 
//	double imageRelY() { return relativeCoord(getImgY(), imgHeight); } 
//
//	double imageRelX(int imgX) { return relativeCoord(imgX, imgWidth); }
//	double imageRelY(int imgY) { return relativeCoord(imgY, imgHeight); }
//
//	double relativeCoord(int coord, int dim)
//	{ return (double) coord / (double) dim; }
//
//	double relativeCoord(double coord, int dim)
//	{ return coord / (double) dim; }
//
//	int imgCoordToGraphicsCoord(int imgCoord, int imgDim, int graphicsDim, int graphicsOrigin)
//	{
//		double rel = relativeCoord(imgCoord, imgDim);
//		return graphicsOrigin + (int) ((double) graphicsDim * rel);
//	}
//
//	int imageXToGraphicsCoord(int imgX, double adj)
//	{
//		double rel = relativeCoord((double) imgX, ap.getDataWidth()) + adj;
//		return imgOriginX + (int) (rel * (double) imgWidth);
//	}
//
//	int imageYToGraphicsCoord(int imgY, double adj)
//	{
//		double rel = relativeCoord((double) imgY, ap.getDataHeight())  + adj;
//		return imgOriginY + (int) (rel * (double) imgHeight);
//	}
//
//	@Override public StretchyClickyIcon getIcon() { return icon; }
//}
