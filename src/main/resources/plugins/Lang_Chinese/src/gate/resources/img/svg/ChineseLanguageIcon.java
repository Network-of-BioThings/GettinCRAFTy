package gate.resources.img.svg;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class has been automatically generated using <a
 * href="http://englishjavadrinker.blogspot.com/search/label/SVGRoundTrip">SVGRoundTrip</a>.
 */
@SuppressWarnings("unused")
public class ChineseLanguageIcon implements
		javax.swing.Icon {
	/**
	 * Paints the transcoded SVG image on the specified graphics context. You
	 * can install a custom transformation on the graphics context to scale the
	 * image.
	 * 
	 * @param g
	 *            Graphics context.
	 */
	public static void paint(Graphics2D g) {
        Shape shape = null;
        Paint paint = null;
        Stroke stroke = null;
        Area clip = null;
         
        float origAlpha = 1.0f;
        Composite origComposite = g.getComposite();
        if (origComposite instanceof AlphaComposite) {
            AlphaComposite origAlphaComposite = 
                (AlphaComposite)origComposite;
            if (origAlphaComposite.getRule() == AlphaComposite.SRC_OVER) {
                origAlpha = origAlphaComposite.getAlpha();
            }
        }
        
	    Shape clip_ = g.getClip();
AffineTransform defaultTransform_ = g.getTransform();
//  is CompositeGraphicsNode
float alpha__0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0 = g.getClip();
AffineTransform defaultTransform__0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
clip = new Area(g.getClip());
clip.intersect(new Area(new Rectangle2D.Double(0.0,0.0,48.0,48.0)));
g.setClip(clip);
// _0 is CompositeGraphicsNode
float alpha__0_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0 = g.getClip();
AffineTransform defaultTransform__0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1004.3621826171875f));
// _0_0 is CompositeGraphicsNode
float alpha__0_0_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0 = g.getClip();
AffineTransform defaultTransform__0_0_0 = g.getTransform();
g.transform(new AffineTransform(2.0f, 0.0f, 0.0f, 1.9999804496765137f, 0.0f, -1053.091796875f));
// _0_0_0 is CompositeGraphicsNode
float alpha__0_0_0_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_0 is ShapeNode
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(2.4861574, 1030.4946);
((GeneralPath)shape).lineTo(8.796234, 1030.4946);
((GeneralPath)shape).lineTo(8.796234, 1031.8833);
((GeneralPath)shape).lineTo(2.486157, 1031.8833);
((GeneralPath)shape).lineTo(2.486157, 1030.4946);
((GeneralPath)shape).moveTo(10.6478405, 1030.4946);
((GeneralPath)shape).lineTo(21.806202, 1030.4946);
((GeneralPath)shape).lineTo(21.806202, 1031.8833);
((GeneralPath)shape).lineTo(16.568108, 1031.8833);
((GeneralPath)shape).curveTo(16.486877, 1032.3542, 16.300097, 1033.2477, 16.007753, 1034.5634);
((GeneralPath)shape).lineTo(20.49059, 1034.5634);
((GeneralPath)shape).lineTo(20.49059, 1039.3142);
((GeneralPath)shape).lineTo(23.000002, 1039.3142);
((GeneralPath)shape).lineTo(23.000002, 1040.7029);
((GeneralPath)shape).lineTo(10.014398, 1040.7029);
((GeneralPath)shape).lineTo(10.014398, 1039.3142);
((GeneralPath)shape).lineTo(13.181619, 1039.3142);
((GeneralPath)shape).lineTo(14.083057, 1035.9521);
((GeneralPath)shape).lineTo(11.183833, 1035.9521);
((GeneralPath)shape).lineTo(11.183833, 1034.5635);
((GeneralPath)shape).lineTo(14.472869, 1034.5635);
((GeneralPath)shape).lineTo(15.00886, 1031.8834);
((GeneralPath)shape).lineTo(10.6478405, 1031.8834);
((GeneralPath)shape).lineTo(10.6478405, 1030.4948);
((GeneralPath)shape).moveTo(1.0, 1033.5156);
((GeneralPath)shape).lineTo(10.014396, 1033.5156);
((GeneralPath)shape).lineTo(10.014396, 1034.9043);
((GeneralPath)shape).lineTo(1.0, 1034.9043);
((GeneralPath)shape).lineTo(1.0, 1033.5156);
((GeneralPath)shape).moveTo(19.00443, 1035.952);
((GeneralPath)shape).lineTo(15.569215, 1035.952);
((GeneralPath)shape).curveTo(15.569199, 1035.952, 15.27684, 1037.0728, 14.692138, 1039.3141);
((GeneralPath)shape).lineTo(19.00443, 1039.3141);
((GeneralPath)shape).lineTo(19.00443, 1035.952);
((GeneralPath)shape).moveTo(2.1938, 1036.5367);
((GeneralPath)shape).lineTo(9.137321, 1036.5367);
((GeneralPath)shape).lineTo(9.137321, 1037.9254);
((GeneralPath)shape).lineTo(2.1938, 1037.9254);
((GeneralPath)shape).lineTo(2.1938, 1036.5367);
((GeneralPath)shape).moveTo(2.1938, 1039.8014);
((GeneralPath)shape).lineTo(9.137321, 1039.8014);
((GeneralPath)shape).lineTo(9.137321, 1041.1902);
((GeneralPath)shape).lineTo(2.1938, 1041.1902);
((GeneralPath)shape).lineTo(2.1938, 1039.8014);
((GeneralPath)shape).moveTo(11.963456, 1042.8225);
((GeneralPath)shape).lineTo(20.904762, 1042.8225);
((GeneralPath)shape).lineTo(20.904762, 1050.6187);
((GeneralPath)shape).lineTo(19.418606, 1050.6187);
((GeneralPath)shape).lineTo(19.418606, 1049.5955);
((GeneralPath)shape).lineTo(13.449613, 1049.5955);
((GeneralPath)shape).lineTo(13.449613, 1050.6187);
((GeneralPath)shape).lineTo(11.963455, 1050.6187);
((GeneralPath)shape).lineTo(11.963455, 1042.8225);
((GeneralPath)shape).moveTo(2.193799, 1043.1879);
((GeneralPath)shape).lineTo(9.1373205, 1043.1879);
((GeneralPath)shape).lineTo(9.1373205, 1050.8624);
((GeneralPath)shape).lineTo(7.6755266, 1050.8624);
((GeneralPath)shape).lineTo(7.6755266, 1049.5956);
((GeneralPath)shape).lineTo(3.7043195, 1049.5956);
((GeneralPath)shape).lineTo(3.7043195, 1050.8624);
((GeneralPath)shape).lineTo(2.193799, 1050.8624);
((GeneralPath)shape).lineTo(2.193799, 1043.1879);
((GeneralPath)shape).moveTo(19.418606, 1044.2112);
((GeneralPath)shape).lineTo(13.449613, 1044.2112);
((GeneralPath)shape).lineTo(13.449613, 1048.2068);
((GeneralPath)shape).lineTo(19.418606, 1048.2068);
((GeneralPath)shape).lineTo(19.418606, 1044.2112);
((GeneralPath)shape).moveTo(7.6755266, 1044.5765);
((GeneralPath)shape).lineTo(3.7043195, 1044.5765);
((GeneralPath)shape).lineTo(3.7043195, 1048.2068);
((GeneralPath)shape).lineTo(7.6755266, 1048.2068);
((GeneralPath)shape).lineTo(7.6755266, 1044.5765);
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_0;
g.setTransform(defaultTransform__0_0_0_0);
g.setClip(clip__0_0_0_0);
origAlpha = alpha__0_0_0;
g.setTransform(defaultTransform__0_0_0);
g.setClip(clip__0_0_0);
origAlpha = alpha__0_0;
g.setTransform(defaultTransform__0_0);
g.setClip(clip__0_0);
origAlpha = alpha__0;
g.setTransform(defaultTransform__0);
g.setClip(clip__0);
g.setTransform(defaultTransform_);
g.setClip(clip_);

	}
	
	public Image getImage() {
		BufferedImage image =
            new BufferedImage(getIconWidth(), getIconHeight(),
                    BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g = image.createGraphics();
    	paintIcon(null, g, 0, 0);
    	g.dispose();
    	return image;
	}

    /**
     * Returns the X of the bounding box of the original SVG image.
     * 
     * @return The X of the bounding box of the original SVG image.
     */
    public static int getOrigX() {
        return 2;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     * 
     * @return The Y of the bounding box of the original SVG image.
     */
    public static int getOrigY() {
        return 4;
    }

	/**
	 * Returns the width of the bounding box of the original SVG image.
	 * 
	 * @return The width of the bounding box of the original SVG image.
	 */
	public static int getOrigWidth() {
		return 48;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static int getOrigHeight() {
		return 48;
	}

	/**
	 * The current width of this resizable icon.
	 */
	int width;

	/**
	 * The current height of this resizable icon.
	 */
	int height;

	/**
	 * Creates a new transcoded SVG image.
	 */
	public ChineseLanguageIcon() {
        this.width = getOrigWidth();
        this.height = getOrigHeight();
	}
	
	/**
	 * Creates a new transcoded SVG image with the given dimensions.
	 *
	 * @param size the dimensions of the icon
	 */
	public ChineseLanguageIcon(Dimension size) {
	this.width = size.width;
	this.height = size.width;
	}

	public ChineseLanguageIcon(int width, int height) {
	this.width = width;
	this.height = height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconHeight()
	 */
    @Override
	public int getIconHeight() {
		return height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconWidth()
	 */
    @Override
	public int getIconWidth() {
		return width;
	}

	public void setDimension(Dimension newDimension) {
		this.width = newDimension.width;
		this.height = newDimension.height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics,
	 * int, int)
	 */
    @Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.translate(x, y);
						
		Area clip = new Area(new Rectangle(0, 0, this.width, this.height));		
		if (g2d.getClip() != null) clip.intersect(new Area(g2d.getClip()));		
		g2d.setClip(clip);

		double coef1 = (double) this.width / (double) getOrigWidth();
		double coef2 = (double) this.height / (double) getOrigHeight();
		double coef = Math.min(coef1, coef2);
		g2d.scale(coef, coef);
		paint(g2d);
		g2d.dispose();
	}
}

