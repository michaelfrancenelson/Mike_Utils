/**
 * @(#)StretchIcon.java	1.0 03/27/12
 */
package swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import images.ArrayDataImageBundle;

/**
 * An extension of the StretchIcon from: <br> 
 * {@link https://tips4java.wordpress.com/2012/03/31/stretch-icon/}
 *
 *  <br> 
 *  With the added functionalities: <br> 
 *  process map mouse clicks to the corresponding pixel of the original image. <br>
 *  draw 
 *   
 *   
 *   <br> Original Description from Darryl: <br>
 * An <CODE>Icon</CODE> that scales its image to fill the component area,
 * excluding any border or insets, optionally maintaining the image's aspect
 * ratio by padding and centering the scaled image horizontally or vertically.
 * <P>
 * The class is a drop-in replacement for <CODE>ImageIcon</CODE>, except that
 * the no-argument constructor is not supported.
 * <P>
 * As the size of the Icon is determined by the size of the component in which
 * it is displayed, <CODE>StretchIcon</CODE> must only be used in conjunction
 * with a component and layout that does not depend on the size of the
 * component's Icon.
 * 
 * 
 * @version 1.0 03/27/12
 * @author Darryl
 */
public class StretchyClickyIcon extends ImageIcon
{

	/* Constructors at the end of the file. */
	/** */ private static final long serialVersionUID = 1L;

	private double aspectRatio;
	protected boolean constantWidth;
	private int width = 0;

	protected int currentOriginX;
	protected int currentOriginY;
	protected int currentImageWidth;
	protected int currentImageHeight; 
	private int currentComponentWidth, currentComponentHeight;

	private double clickedImageRatioX, clickedImageRatioY;
	private int clickedImageColumn, clickedImageRow;
	private int clickedSourceDataColumn, clickedSourceDataRow;

	protected int originalImageWidth, originalImageHeight;
	private int currentDataPixelWidth;






	public StretchyClickyIcon() {}

	/**
	 * Determines whether the aspect ratio of the image is maintained.
	 * Set to <code>false</code> to allow the image to distort to fill the component.
	 */
	protected boolean fixedImageAspectRatio = true;
	private boolean verbose;
	private double compAspectRatio;
	private int clickedComponentColumn;
	private int clickedComponentRow;
	private double clickedComponentRatioX;
	private double clickedComponentRatioY;

	public void setVerbose(boolean b) { this.verbose = b; }

	/** Calculate the aspect ratio of the original icon image. */
	private void setImageAspectRatio()
	{
		Image img = getImage(); aspectRatio = getAspectRatio(img.getWidth(null), img.getHeight(null));
		originalImageHeight = img.getHeight(null);
		originalImageWidth = img.getWidth(null);

	}

	public static void main(String[] args) 
	{
		//		franceDemo();
		randomDataDemo();
	}

	public static void randomDataDemo()
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1000, 700));

		StretchyClickyIcon icon1 = ArrayDataImageBundle.createRandomPackage(11, 18).createIcon(true);
		StretchyClickyIcon icon2 = ArrayDataImageBundle.createRandomPackage(11, 18).createIcon(false);
		StretchyClickyIcon icon3 = ArrayDataImageBundle.createRandomPackage(11, 18).createIcon(false);
		icon3.setConstantWidth(68);
		JLabel label1 = new JLabel(icon1);
		JLabel label2 = new JLabel(icon2);
		JLabel label3 = new JLabel(icon3);

		frame.setLayout(new GridLayout());
		frame.add(label1);
		frame.add(label2);
		frame.add(label3);
		frame.setVisible(true);

	}
	public static void franceDemo()
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1000, 1002));

		StretchyClickyIcon icon1 = new StretchyClickyIcon("testData/france.jpg");
		icon1.setFixedWidth(500);
		StretchyClickyIcon icon2 = new StretchyClickyIcon("testData/france.jpg");

		JLabel label1 = new JLabel(icon1);
		JLabel label2 = new JLabel(icon2);

		frame.setLayout(new GridLayout());
		frame.add(label1);
		frame.add(label2);
		frame.setVisible(true);
	}

	public void setFixedWidth(int width) { this.width = width; this.constantWidth = true; this.fixedImageAspectRatio = false; }

	/**
	 * Paints the icon.  The image is reduced or magnified to fit the component to which
	 * it is painted.
	 * <P>
	 * If the proportion has not been specified, or has been specified as <code>true</code>,
	 * the aspect ratio of the image will be preserved by padding and centering the image
	 * horizontally or vertically.  Otherwise the image may be distorted to fill the
	 * component it is painted to.
	 * <P>
	 * If this icon has no image observer,this method uses the <code>c</code> component
	 * as the observer.
	 *
	 * @param c the component to which the Icon is painted.  This is used as the
	 *          observer if this icon has no image observer
	 * @param g the graphics context
	 * @param x not used.
	 * @param y not used.
	 *
	 * @see ImageIcon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
	 */
	@Override
	public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
		Image image = getImage(); 
		ImageObserver io = getImageObserver();

		if (image == null) { return; }
		Insets insets = ((Container) c).getInsets();
		x = insets.left; y = insets.top;




		currentComponentWidth = c.getWidth() - x - insets.right;
		currentComponentHeight = c.getHeight() - y - insets.bottom;
		currentImageWidth = image.getWidth(c);
		currentImageHeight = image.getHeight(c);

		int newComponentWidth = currentComponentWidth;
		int newComponentHeight = currentComponentHeight;

		int newImageX = x, newImageY = y;
		compAspectRatio = (double) currentComponentWidth / (double) currentComponentHeight;

		/* If keeping the original aspect ratio. */
		if (fixedImageAspectRatio) {
			if (aspectRatio < compAspectRatio)
			{
				newComponentWidth = (int) ((double) currentComponentHeight * aspectRatio);
			} else {
				newComponentHeight = (int) ((double) currentComponentWidth / aspectRatio);
			}
		}

		if (constantWidth)
		{ 
			if (verbose) System.out.println("fixed width of " + width);
			newComponentWidth = width;
			this.currentImageWidth = width;
		}

		newImageX += (currentComponentWidth - newComponentWidth) / 2;
		newImageY += (currentComponentHeight - newComponentHeight) / 2;
		if (verbose)
		{
			System.out.println("component aspect ratio: " + compAspectRatio);
			System.out.println("image aspect ratio: " + aspectRatio);
			System.out.println("old comp. width  = " + currentComponentWidth);
			System.out.println("new comp. width  = " + newComponentWidth);
			System.out.println("old comp height = " + currentComponentHeight);
			System.out.println("new comp. height = " + newComponentHeight);
		}

		currentOriginX = newImageX;
		currentOriginY = newImageY;
		this.currentImageHeight = newComponentHeight;
		this.currentImageWidth = newComponentWidth;
		this.currentDataPixelWidth = currentImageWidth / originalImageWidth;
		g.drawImage(image, newImageX, newImageY, newComponentWidth, newComponentHeight, io == null ? c : io);
	}

	public int getImageDataNRows() { return originalImageHeight; }
	public int getImageDataNCols() { return originalImageWidth; }

	private double boundedRatio (int relativeCoord, int dimLength)
	{
		return Math.max(
				0.0,
				Math.min(
						1.0,
						(double) relativeCoord / (double) dimLength));
	}

	public void setMouseClick(MouseEvent arg0)
	{
		clickedComponentColumn = arg0.getX();
		clickedComponentRow = arg0.getY();
		clickedImageColumn = clickedComponentColumn - getX();
		clickedImageRow = clickedComponentRow - getY();

		clickedComponentRatioX = boundedRatio(clickedComponentColumn, currentComponentWidth);
		clickedComponentRatioY = boundedRatio(clickedComponentRow, currentComponentHeight);
		clickedImageRatioX = boundedRatio(clickedImageColumn, currentImageWidth);
		clickedImageRatioY = boundedRatio(clickedImageRow, currentImageHeight);

		clickedSourceDataColumn = Math.min(
				originalImageWidth - 1,
				(int) (clickedImageRatioX * (double) originalImageWidth));
		clickedSourceDataRow = Math.min(
				originalImageHeight - 1,
				(int) (clickedImageRatioY * (double) originalImageHeight));
	}
	
	public int componentRowFromDataRow(int dataRow)
	{
		double rowRatio = boundedRatio(dataRow, originalImageHeight);
		int displayImageRow = currentOriginY + (int) ((double) currentImageHeight * rowRatio);
		return displayImageRow;
	}
	public int componentColumnFromDataCol(int dataColumn)
	{
		double rowRatio = boundedRatio(dataColumn, originalImageWidth);
		int displayImageColumn = currentOriginX + (int) ((double) currentImageWidth * rowRatio);
		return displayImageColumn;
	}
	
	public int getDataPixelWidth() { return currentDataPixelWidth;}
	
	public void printComponentClick()
	{	System.out.println(String.format("Component mouse click at (%d, %d)", 
			clickedComponentColumn, clickedComponentRow));
	}

	public void printDisplayImageClick()
	{	System.out.println(String.format("Displayed image mouse click at (%d, %d)", 
			clickedImageColumn, clickedImageRow));
	}

	public void printComponentClickRatio()
	{ System.out.println(String.format("Component mouse click at %.2f%% of component width.", 
			100.0 * clickedComponentRatioX));
	System.out.println(String.format("Component mouse click at %.2f%% of component height.", 
			100.0 * clickedComponentRatioY));
	}

	public void printDisplayImageClickRatio()
	{
		System.out.println(String.format("Icon image mouse click at %.2f%% of image width.", 
				100.0 * clickedImageRatioX));
		System.out.println(String.format("Icon image mouse click at %.2f%% of image height.",
				100.0 * clickedImageRatioY));
	}

	public void printDataClick()
	{
		System.out.println(String.format("Source mouse click at (%d, %d)", 
				clickedSourceDataColumn, clickedSourceDataRow));
	}

	public int getClickedDataColumn() { return clickedSourceDataColumn; }
	public int getClickedDataRow() { return clickedSourceDataRow; }

	public int getDisplayedColumn(int dataColumn)
	{
		double xProportion =
				((double) dataColumn + 0.5) / (double) originalImageWidth;

		int proportionalX = (int)((double)currentImageWidth * xProportion);
		if (verbose) printDisplayImageDimensions();
		return proportionalX;
	}

	void printDisplayImageDimensions()
	{
		System.out.println("icon image display width: " + currentImageWidth);
		System.out.println("icon image display height: " + currentImageHeight);
		System.out.println("icon image origin X " + currentOriginX);
		System.out.println("icon image origin Y " + currentOriginY);
	}

	void printDisplayImageCornerCoords()
	{
		int x0 = currentOriginX, x1 = currentOriginX + currentImageWidth;
		int y0 = currentOriginY, y1 = currentOriginY + currentImageHeight;
		System.out.println(String.format("image coords: (%d, %d), (%d, %d), (%d, %d), (%d, %d)",
				x0, y0, x0, y1, x1, y0, x1, y1));
	}
	void printSourceImageDimensions()
	{
		System.out.println(String.format("source image height: %d, source image width: %d",
				originalImageHeight, originalImageWidth));	

	}

	private double getAspectRatio(int width, int height) { return (double) width / (double) height; }
	public void setConstantWidth(int width) { this.width = width; this.constantWidth = true; this.fixedImageAspectRatio = false;}
	public int getImageHeight() { return currentImageHeight; };
	public int getImageWidth() { return currentImageWidth; }
	public int getX() { return currentOriginX; }; 
	public int getY() { return currentOriginY; }

	public Graphics getIconGraphics() { return getImage().getGraphics(); }

	/**
	 * Overridden to return 0.  The size of this Icon is determined by
	 * the size of the component.
	 * 
	 * @return 0
	 */
	@Override
	public int getIconWidth() {
		return 0;
	}

	/**
	 * Overridden to return 0.  The size of this Icon is determined by
	 * the size of the component.
	 *
	 * @return 0
	 */
	@Override
	public int getIconHeight() {
		return 0;
	}

	/**
	 * Creates a <CODE>StretchIcon</CODE> from an array of bytes with the specified behavior.
	 *
	 * @param  imageData an array of pixels in an image format supported by
	 *             the AWT Toolkit, such as GIF, JPEG, or (as of 1.3) PNG
	 * @param proportionate <code>true</code> to retain the image's aspect ratio,
	 *        <code>false</code> to allow distortion of the image to fill the
	 *        component.
	 *
	 * @see ImageIcon#ImageIcon(byte[])
	 */
	public StretchyClickyIcon(byte[] imageData, boolean proportionate) {
		super(imageData); this.fixedImageAspectRatio = proportionate; setImageAspectRatio();
	}

	/**
	 * Creates a <CODE>StretchIcon</CODE> from an array of bytes.
	 *
	 * @param  imageData an array of pixels in an image format supported by
	 *             the AWT Toolkit, such as GIF, JPEG, or (as of 1.3) PNG
	 * @param  description a brief textual description of the image
	 *
	 * @see ImageIcon#ImageIcon(byte[], java.lang.String)
	 */
	public StretchyClickyIcon(byte[] imageData, String description) {
		super(imageData, description); setImageAspectRatio();
	}

	/**
	 * Creates a <CODE>StretchIcon</CODE> from an array of bytes with the specified behavior.
	 *
	 * @see ImageIcon#ImageIcon(byte[])
	 * @param  imageData an array of pixels in an image format supported by
	 *             the AWT Toolkit, such as GIF, JPEG, or (as of 1.3) PNG
	 * @param  description a brief textual description of the image
	 * @param proportionate <code>true</code> to retain the image's aspect ratio,
	 *        <code>false</code> to allow distortion o.f the image to fill the
	 *        component.
	 *
	 * @see ImageIcon#ImageIcon(byte[], java.lang.String)
	 */
	public StretchyClickyIcon(byte[] imageData, String description, boolean proportionate) {
		super(imageData, description); this.fixedImageAspectRatio = proportionate; setImageAspectRatio();
	}

	/**
	 * Creates a <CODE>StretchIcon</CODE> from the image.
	 *
	 * @param image the image
	 *
	 * @see ImageIcon#ImageIcon(java.awt.Image)
	 */
	public StretchyClickyIcon(Image image) { super(image); setImageAspectRatio(); }

	/**
	 * Creates a <CODE>StretchIcon</CODE> from the image with the specified behavior.
	 * 
	 * @param image the image
	 * @param proportionate <code>true</code> to retain the image's aspect ratio,
	 *        <code>false</code> to allow distortion of the image to fill the
	 *        component.
	 * 
	 * @see ImageIcon#ImageIcon(java.awt.Image) 
	 */
	public StretchyClickyIcon(Image image, boolean proportionate) {
		super(image); setImageAspectRatio(); this.fixedImageAspectRatio = proportionate; 
	}

	/**
	 * Creates a <CODE>StretchIcon</CODE> from the image.
	 * 
	 * @param image the image
	 * @param  description a brief textual description of the image
	 * 
	 * @see ImageIcon#ImageIcon(java.awt.Image, java.lang.String) 
	 */
	public StretchyClickyIcon(Image image, String description) { super(image, description); setImageAspectRatio(); }

	/**
	 * Creates a <CODE>StretchIcon</CODE> from the image with the specified behavior.
	 *
	 * @param image the image
	 * @param  description a brief textual description of the image
	 * @param proportionate <code>true</code> to retain the image's aspect ratio,
	 *        <code>false</code> to allow distortion of the image to fill the
	 *        component.
	 *
	 * @see ImageIcon#ImageIcon(java.awt.Image, java.lang.String)
	 */
	public StretchyClickyIcon(Image image, String description, boolean proportionate) {
		super(image, description); setImageAspectRatio(); this.fixedImageAspectRatio = proportionate; }

	/**
	 * Creates a <CODE>StretchIcon</CODE> from the specified file.
	 *
	 * @param filename a String specifying a filename or path
	 *
	 * @see ImageIcon#ImageIcon(java.lang.String)
	 */
	public StretchyClickyIcon(String filename) { super(filename); setImageAspectRatio(); }

	/**
	 * Creates a <CODE>StretchIcon</CODE> from the specified file with the specified behavior.
	 * 
	 * @param filename a String specifying a filename or path
	 * @param proportionate <code>true</code> to retain the image's aspect ratio,
	 *        <code>false</code> to allow distortion of the image to fill the
	 *        component.
	 *
	 * @see ImageIcon#ImageIcon(java.lang.String)
	 */
	public StretchyClickyIcon(String filename, boolean proportionate) {
		super(filename); setImageAspectRatio(); this.fixedImageAspectRatio = proportionate; }

	/**
	 * Creates a <CODE>StretchIcon</CODE> from the specified file.
	 *
	 * @param filename a String specifying a filename or path
	 * @param  description a brief textual description of the image
	 *
	 * @see ImageIcon#ImageIcon(java.lang.String, java.lang.String)
	 */
	public StretchyClickyIcon(String filename, String description) { super(filename, description); setImageAspectRatio(); }

	/**
	 * Creates a <CODE>StretchIcon</CODE> from the specified file with the specified behavior.
	 * 
	 * @param filename a String specifying a filename or path
	 * @param  description a brief textual description of the image
	 * @param proportionate <code>true</code> to retain the image's aspect ratio,
	 *        <code>false</code> to allow distortion of the image to fill the
	 *        component.
	 *
	 * @see ImageIcon#ImageIcon(java.awt.Image, java.lang.String)
	 */
	public StretchyClickyIcon(String filename, String description, boolean proportionate) {
		super(filename, description); setImageAspectRatio(); this.fixedImageAspectRatio = proportionate; }

	/**
	 * Creates a <CODE>StretchIcon</CODE> from the specified URL.
	 *
	 * @param location the URL for the image
	 *
	 * @see ImageIcon#ImageIcon(java.net.URL)
	 */
	public StretchyClickyIcon(URL location) { super(location); setImageAspectRatio(); }

	/**
	 * Creates a <CODE>StretchIcon</CODE> from the specified URL with the specified behavior.
	 * 
	 * @param location the URL for the image
	 * @param proportionate <code>true</code> to retain the image's aspect ratio,
	 *        <code>false</code> to allow distortion of the image to fill the
	 *        component.
	 *
	 * @see ImageIcon#ImageIcon(java.net.URL)
	 */
	public StretchyClickyIcon(URL location, boolean proportionate) {
		super(location); setImageAspectRatio(); this.fixedImageAspectRatio = proportionate; }

	/**
	 * Creates a <CODE>StretchIcon</CODE> from the specified URL.
	 *
	 * @param location the URL for the image
	 * @param  description a brief textual description of the image
	 *
	 * @see ImageIcon#ImageIcon(java.net.URL, java.lang.String)
	 */
	public StretchyClickyIcon(URL location, String description) { super(location, description); setImageAspectRatio(); }

	/**
	 * Creates a <CODE>StretchIcon</CODE> from the specified URL with the specified behavior.
	 * 
	 * @param location the URL for the image
	 * @param  description a brief textual description of the image
	 * @param proportionate <code>true</code> to retain the image's aspect ratio,
	 *        <code>false</code> to allow distortion of the image to fill the
	 *        component.
	 *
	 * @see ImageIcon#ImageIcon(java.net.URL, java.lang.String)
	 */
	public StretchyClickyIcon(URL location, String description, boolean proportionate) {
		super(location, description); setImageAspectRatio(); this.fixedImageAspectRatio = proportionate; }


}


