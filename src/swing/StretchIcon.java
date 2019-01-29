/**
 * @(#)StretchIcon.java	1.0 03/27/12
 */
package swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.ImageObserver;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import images.ArrayDataImageBundle;

/**
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
 * {@link https://tips4java.wordpress.com/2012/03/31/stretch-icon/} 
 * 
 * @version 1.0 03/27/12
 * @author Darryl
 */
public class StretchIcon extends ImageIcon {

	private double aspectRatio;
	protected boolean constantWidth;
	private int width = 0;

	protected int currentOriginX;
	protected int currentOriginY;
	protected int currentImageWidth;
	protected int currentImageHeight; 

	protected int originalImageWidth, originalImageHeight;

	/** */ private static final long serialVersionUID = 1L;

	public StretchIcon() {}

	/**
	 * Determines whether the aspect ratio of the image is maintained.
	 * Set to <code>false</code> to allow the image to distort to fill the component.
	 */
	protected boolean fixedImageAspectRatio = true;
	private boolean verbose;

	public void setVerbose(boolean b) { this.verbose = b; }

	/** Calculate the aspect ratio of the original icon image. */
	private void setImageAspectRatio()
	{
		Image img = getImage(); aspectRatio = getAspectRatio(img.getWidth(null), img.getHeight(null));
		originalImageHeight = img.getHeight(null);
		originalImageWidth = img.getWidth(null);

		System.out.println(img.getHeight(null));
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1000, 1002));

		//		StretchIcon icon = new StretchIcon("testData/france.jpg");
		//		StretchIcon icon2 = new StretchIcon("testData/france.jpg");
		StretchIcon icon2 = ArrayDataImageBundle.createRandomPackage(11, 18).createIcon(true);
		//				icon.fixedImageAspectRatio = false;
		//				icon.constantWidth = true;
		//				icon.width = 100;

		//		icon.setFixedWidth(500);
		//		JLabel label = new JLabel(icon);
		JLabel label2 = new JLabel(icon2);

		frame.setLayout(new GridLayout());
		//		frame.add(label);
		frame.add(label2);
		frame.setVisible(true);

		//		System.out.println(icon.currentOriginX + " " + icon.currentOriginY);
	}

	public void setFixedWidth(int width) { this.width = width; this.constantWidth = true; this.fixedImageAspectRatio = false; }

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
	public StretchIcon(byte[] imageData, boolean proportionate) {
		super(imageData);
		this.fixedImageAspectRatio = proportionate;	setImageAspectRatio();
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
	public StretchIcon(byte[] imageData, String description) {
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
	public StretchIcon(byte[] imageData, String description, boolean proportionate) {
		super(imageData, description);
		this.fixedImageAspectRatio = proportionate; setImageAspectRatio();
	}

	/**
	 * Creates a <CODE>StretchIcon</CODE> from the image.
	 *
	 * @param image the image
	 *
	 * @see ImageIcon#ImageIcon(java.awt.Image)
	 */
	public StretchIcon(Image image) {
		super(image); setImageAspectRatio();
	}

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
	public StretchIcon(Image image, boolean proportionate) {
		super(image); setImageAspectRatio();
		this.fixedImageAspectRatio = proportionate; 
	}

	/**
	 * Creates a <CODE>StretchIcon</CODE> from the image.
	 * 
	 * @param image the image
	 * @param  description a brief textual description of the image
	 * 
	 * @see ImageIcon#ImageIcon(java.awt.Image, java.lang.String) 
	 */
	public StretchIcon(Image image, String description) {
		super(image, description); setImageAspectRatio();
	}

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
	public StretchIcon(Image image, String description, boolean proportionate) {
		super(image, description); setImageAspectRatio();
		this.fixedImageAspectRatio = proportionate;
	}

	/**
	 * Creates a <CODE>StretchIcon</CODE> from the specified file.
	 *
	 * @param filename a String specifying a filename or path
	 *
	 * @see ImageIcon#ImageIcon(java.lang.String)
	 */
	public StretchIcon(String filename) {
		super(filename); setImageAspectRatio();
	}

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
	public StretchIcon(String filename, boolean proportionate) {
		super(filename); setImageAspectRatio();
		this.fixedImageAspectRatio = proportionate;
	}

	/**
	 * Creates a <CODE>StretchIcon</CODE> from the specified file.
	 *
	 * @param filename a String specifying a filename or path
	 * @param  description a brief textual description of the image
	 *
	 * @see ImageIcon#ImageIcon(java.lang.String, java.lang.String)
	 */
	public StretchIcon(String filename, String description) {
		super(filename, description); setImageAspectRatio();
	}

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
	public StretchIcon(String filename, String description, boolean proportionate) {
		super(filename, description); setImageAspectRatio();
		this.fixedImageAspectRatio = proportionate;
	}

	/**
	 * Creates a <CODE>StretchIcon</CODE> from the specified URL.
	 *
	 * @param location the URL for the image
	 *
	 * @see ImageIcon#ImageIcon(java.net.URL)
	 */
	public StretchIcon(URL location) {
		super(location); setImageAspectRatio();
	}

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
	public StretchIcon(URL location, boolean proportionate) {
		super(location); setImageAspectRatio();
		this.fixedImageAspectRatio = proportionate;
	}

	/**
	 * Creates a <CODE>StretchIcon</CODE> from the specified URL.
	 *
	 * @param location the URL for the image
	 * @param  description a brief textual description of the image
	 *
	 * @see ImageIcon#ImageIcon(java.net.URL, java.lang.String)
	 */
	public StretchIcon(URL location, String description) {
		super(location, description); setImageAspectRatio();
	}

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
	public StretchIcon(URL location, String description, boolean proportionate) {
		super(location, description); setImageAspectRatio();
		this.fixedImageAspectRatio = proportionate;
	}

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

		int currentComponentWidth = c.getWidth() - x - insets.right;
		int currentComponentHeight = c.getHeight() - y - insets.bottom;
		currentImageWidth = image.getWidth(c);
		currentImageHeight = image.getHeight(c);

		int newComponentWidth = currentComponentWidth;
		int newComponentHeight = currentComponentHeight;

		int newImageX = x, newImageY = y;
		double compAspectRatio = (double) currentComponentWidth / (double) currentComponentHeight;

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
		g.drawImage(image, newImageX, newImageY, newComponentWidth, newComponentHeight, io == null ? c : io);
	}

	public int getImageNRows() { return originalImageHeight; }
	public int getImageNCols() { return originalImageWidth; }

	public void printImageCorneCoords()
	{
		int top = getY();
		int left = getX();
		int height = currentImageHeight;
		int width = currentImageWidth;
		System.out.println("source image height: " + originalImageHeight + ", source image width: " + originalImageWidth);	
		System.out.println("display image height: " + height + ", display image width: " + width);	
		System.out.println("display image: top: " + top + ", display image bottom: " + (top + getImageHeight()));
		System.out.println("display image: left: " + left + ", display image bottom: " + (left + getImageWidth()));
	}

	private int relativeX(int pixelX) { return pixelX - getX(); }
	private int relativeY(int pixelY) { return pixelY - getY(); }

	private double ratio (int displayedPix, int imageDim)
	{
		return Math.max(
				0.0,
				Math.min(
						1.0,
						(double) displayedPix / (double) imageDim));
	}
	
	public int getSourceImageColIndex(int clickedX)
	{
		int displayedX = relativeX(clickedX);
		double ratioX = ratio(displayedX, currentImageWidth);
		return (int) Math.min(originalImageWidth - 1, (ratioX * originalImageWidth));
	}
	public int getSourceImageRowIndex(int clickedY)
	{
		int displayedY = relativeY(clickedY);
		double ratioY = ratio(displayedY, currentImageHeight);
		return (int) Math.min(originalImageHeight - 1, (ratioY * originalImageHeight));
	}

	private double getAspectRatio(int width, int height) { return (double) width / (double) height; }
	public void setConstantWidth(int width) { this.width = width; this.constantWidth = true; this.fixedImageAspectRatio = false;}
	public int getImageHeight() { return currentImageHeight; };
	public int getImageWidth() { return currentImageWidth; }
	public int getX() { return currentOriginX; }; 
	public int getY() { return currentOriginY; }

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

}


