/**
 * @(#)StretchIcon.java	1.0 03/27/12
 */
package swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.ImageObserver;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
	private int width;

	protected int currentOriginX;
	protected int currentOriginY;
	protected int currentImageWidth;
	protected int currentImageHeight; 

	/** */ private static final long serialVersionUID = 1L;

	/**
	 * Determines whether the aspect ratio of the image is maintained.
	 * Set to <code>false</code> to allow th image to distort to fill the component.
	 */
	protected boolean fixedImageAspectRatio = true;

	private void setImageAspectRatio()
	{
		Image img = getImage(); aspectRatio = getAspectRatio(img.getWidth(null), img.getHeight(null));
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1000, 1002));

		StretchIcon icon = new StretchIcon("testData/france.jpg");
		//		icon.proportionate = false;
		JLabel label = new JLabel(icon);
		frame.add(label);
		frame.setVisible(true);

		System.out.println(icon.currentOriginX + " " + icon.currentOriginY);
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
				System.out.println("fat component");
				newComponentWidth = (int) ((double) currentComponentHeight * aspectRatio);
				newImageX += (currentComponentWidth - newComponentWidth) / 2;
			} else {
				System.out.println("thin component");
				newComponentHeight = (int) ((double) currentComponentWidth * aspectRatio);
				newImageY += (currentComponentHeight - newComponentHeight) / 2;
			}
			
			


		}
		
		if (constantWidth)
		{
			currentComponentWidth = width;
			this.currentImageWidth = width;
		}
		System.out.println("old comp. width  = " + currentComponentWidth);
		System.out.println("new comp. width  = " + newComponentWidth);
		System.out.println("old comp height = " + currentComponentHeight);
		System.out.println("new comp. height = " + newComponentHeight);

		currentOriginX = x; 
		currentOriginY = y;
		this.currentImageHeight = currentComponentHeight;
		this.currentImageWidth = currentComponentWidth;
		g.drawImage(image, newImageX, newImageY, newComponentWidth, newComponentHeight, io == null ? c : io);

		
		//		ImageObserver io = getImageObserver();
//
//
////		g.drawImage(image, newImageX, newImageY, newImageWidth, currentComponentHeight, io == null ? c : io);
//		g.drawImage(image, newImageX, newImageY, currentComponentWidth, currentComponentHeight, io == null ? c : io);



		//		g.drawImage(image, newImageX, newImageY, currentComponentWidth, newImageHeight, io == null ? c : io);
		//		g.drawImage(image, x, newImageY, currentComponentWidth, currentComponentHeight, io == null ? c : io);
		//				g.drawImage(image, x, y, currentComponentWidth, currentComponentHeight, io == null ? c : io);
	}

	private double getAspectRatio(int width, int height) { return (double) width / (double) height; }

	public void setConstantWidth(int width)
	{
		this.width = width; this.constantWidth = true;
	}
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


