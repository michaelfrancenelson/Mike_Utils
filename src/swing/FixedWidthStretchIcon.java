package swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;

public class FixedWidthStretchIcon extends StretchIcon
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5880631647167947183L;

	public FixedWidthStretchIcon(byte[] imageData, boolean proportionate) {
		super(imageData, proportionate);
		// TODO Auto-generated constructor stub
	}

	public void FixedWidthStretchIcon() {}
	
	private int width;
	
	public static void main(String[] args) {
		
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
		if (image == null) {
			return;
		}
		Insets insets = ((Container) c).getInsets();
		x = insets.left;
		y = insets.top;

		int componentWidth = c.getWidth() - x - insets.right;
		int componentHeight = c.getHeight() - y - insets.bottom;
		int imageWidth = image.getWidth(c);
		int imageHeight = image.getHeight(c);

		if (fixedImageAspectRatio) {

			if (imageWidth * componentHeight < imageHeight * componentWidth) {
				imageWidth = (componentHeight * imageWidth) / imageHeight;
				x += (componentWidth - imageWidth) / 2;
				componentWidth = imageWidth;
			} else {
				imageHeight = (componentWidth * imageHeight) / imageWidth;
				y += (componentHeight - imageHeight) / 2;
				componentHeight = imageHeight;
			}
		}

		currentOriginX = x; 
		currentOriginY = y;
		this.currentImageHeight = componentHeight;
		this.currentImageWidth = componentWidth;
		ImageObserver io = getImageObserver();
		
		if (constantWidth)
		{
			componentWidth = width;
			this.currentImageWidth = width;
		}
		g.drawImage(image, x, y, componentWidth, componentHeight, io == null ? c : io);
	}
	
	
}
