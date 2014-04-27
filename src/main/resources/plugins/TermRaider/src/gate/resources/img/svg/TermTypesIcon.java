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
public class TermTypesIcon implements
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
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0 is CompositeGraphicsNode
origAlpha = alpha__0_0;
g.setTransform(defaultTransform__0_0);
g.setClip(clip__0_0);
float alpha__0_1 = origAlpha;
origAlpha = origAlpha * 0.3f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_1 = g.getClip();
AffineTransform defaultTransform__0_1 = g.getTransform();
g.transform(new AffineTransform(1.0250790119171143f, 0.0f, 0.0f, 1.5679240226745605f, -0.589372992515564f, -22.968629837036133f));
// _0_1 is CompositeGraphicsNode
float alpha__0_1_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_1_0 = g.getClip();
AffineTransform defaultTransform__0_1_0 = g.getTransform();
g.transform(new AffineTransform(-1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f));
// _0_1_0 is ShapeNode
paint = new RadialGradientPaint(new Point2D.Double(2.0, 39.015625), 1.5f, new Point2D.Double(2.0, 39.015625), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(2.0f, 0.0f, 0.0f, 2.0f, -9.0f, -116.53130340576172f));
shape = new Rectangle2D.Double(-5.0, -41.5, 3.0, 6.0);
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_1_0;
g.setTransform(defaultTransform__0_1_0);
g.setClip(clip__0_1_0);
float alpha__0_1_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_1_1 = g.getClip();
AffineTransform defaultTransform__0_1_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_1 is ShapeNode
paint = new RadialGradientPaint(new Point2D.Double(2.0, 39.015625), 1.5f, new Point2D.Double(2.0, 39.015625), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(2.0f, 0.0f, 0.0f, 2.0f, 38.0f, -39.53129959106445f));
shape = new Rectangle2D.Double(42.0, 35.5, 3.0, 6.0);
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_1_1;
g.setTransform(defaultTransform__0_1_1);
g.setClip(clip__0_1_1);
float alpha__0_1_2 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_1_2 = g.getClip();
AffineTransform defaultTransform__0_1_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_2 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(10.915961265563965, 35.49853515625), new Point2D.Double(10.915961265563965, 41.50017547607422), new float[] {0.0f,0.5f,1.0f}, new Color[] {new Color(0, 0, 0, 0),new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new Rectangle2D.Double(5.0, 35.5, 37.0, 6.0);
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_1_2;
g.setTransform(defaultTransform__0_1_2);
g.setClip(clip__0_1_2);
origAlpha = alpha__0_1;
g.setTransform(defaultTransform__0_1);
g.setClip(clip__0_1);
float alpha__0_2 = origAlpha;
origAlpha = origAlpha * 0.18857141f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_2 = g.getClip();
AffineTransform defaultTransform__0_2 = g.getTransform();
g.transform(new AffineTransform(0.4623599946498871f, 0.0f, 0.0f, 1.4047620296478271f, 12.258919715881348f, -15.410710334777832f));
// _0_2 is ShapeNode
paint = new RadialGradientPaint(new Point2D.Double(24.3125, 39.0), 22.5625f, new Point2D.Double(24.3125, 39.0), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.23268699645996094f, 0.0f, 29.92521095275879f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(46.875, 39.0);
((GeneralPath)shape).curveTo(46.875, 41.899494, 36.773426, 44.25, 24.3125, 44.25);
((GeneralPath)shape).curveTo(11.851575, 44.25, 1.75, 41.899494, 1.75, 39.0);
((GeneralPath)shape).curveTo(1.75, 36.100506, 11.851575, 33.75, 24.3125, 33.75);
((GeneralPath)shape).curveTo(36.773426, 33.75, 46.875, 36.100506, 46.875, 39.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_2;
g.setTransform(defaultTransform__0_2);
g.setClip(clip__0_2);
float alpha__0_3 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_3 = g.getClip();
AffineTransform defaultTransform__0_3 = g.getTransform();
g.transform(new AffineTransform(-1.0f, 0.0f, 0.0f, 1.0f, -36.0f, 0.0f));
// _0_3 is CompositeGraphicsNode
float alpha__0_3_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_3_0 = g.getClip();
AffineTransform defaultTransform__0_3_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_3_0 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(56.500003814697266, 13.926060676574707), new Point2D.Double(56.500003814697266, 33.86942672729492), new float[] {0.0f,1.0f}, new Color[] {new Color(115, 210, 22, 255),new Color(138, 226, 52, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(-1.0f, 0.0f, 0.0f, 1.0f, -16.0f, -1.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(-78.50001, 8.0);
((GeneralPath)shape).lineTo(-78.50001, 38.001827);
((GeneralPath)shape).curveTo(-78.50001, 38.83181, -77.839355, 39.499992, -77.01872, 39.499992);
((GeneralPath)shape).lineTo(-67.98128, 39.499992);
((GeneralPath)shape).curveTo(-67.16065, 39.499992, -66.5, 38.83181, -66.5, 38.001827);
((GeneralPath)shape).lineTo(-66.5, 8.0);
((GeneralPath)shape).lineTo(-65.5, 5.5);
((GeneralPath)shape).lineTo(-65.5, 10.5);
((GeneralPath)shape).lineTo(-65.5, 7.5);
((GeneralPath)shape).lineTo(-76.5, 7.5);
((GeneralPath)shape).lineTo(-76.5, 5.5);
((GeneralPath)shape).lineTo(-78.50001, 8.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(78, 154, 6, 255);
stroke = new BasicStroke(1.0000001f,2,1,7.1f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(-78.50001, 8.0);
((GeneralPath)shape).lineTo(-78.50001, 38.001827);
((GeneralPath)shape).curveTo(-78.50001, 38.83181, -77.839355, 39.499992, -77.01872, 39.499992);
((GeneralPath)shape).lineTo(-67.98128, 39.499992);
((GeneralPath)shape).curveTo(-67.16065, 39.499992, -66.5, 38.83181, -66.5, 38.001827);
((GeneralPath)shape).lineTo(-66.5, 8.0);
((GeneralPath)shape).lineTo(-65.5, 5.5);
((GeneralPath)shape).lineTo(-65.5, 10.5);
((GeneralPath)shape).lineTo(-65.5, 7.5);
((GeneralPath)shape).lineTo(-76.5, 7.5);
((GeneralPath)shape).lineTo(-76.5, 5.5);
((GeneralPath)shape).lineTo(-78.50001, 8.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_3_0;
g.setTransform(defaultTransform__0_3_0);
g.setClip(clip__0_3_0);
float alpha__0_3_1 = origAlpha;
origAlpha = origAlpha * 0.7f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_3_1 = g.getClip();
AffineTransform defaultTransform__0_3_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_3_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(5.391689300537109, -38.885746002197266), new Point2D.Double(5.391689300537109, 8.933954238891602), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -83.0f, 49.0f));
stroke = new BasicStroke(1.0f,1,0,7.1f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(-67.49999, 8.506263);
((GeneralPath)shape).lineTo(-67.49999, 37.874996);
((GeneralPath)shape).curveTo(-67.49999, 38.22125, -67.77874, 38.499996, -68.12499, 38.499996);
((GeneralPath)shape).lineTo(-76.875, 38.499996);
((GeneralPath)shape).curveTo(-77.22125, 38.499996, -77.5, 38.22125, -77.5, 37.874996);
((GeneralPath)shape).lineTo(-77.5, 8.506263);
((GeneralPath)shape).lineTo(-67.49999, 8.506263);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_3_1;
g.setTransform(defaultTransform__0_3_1);
g.setClip(clip__0_3_1);
origAlpha = alpha__0_3;
g.setTransform(defaultTransform__0_3);
g.setClip(clip__0_3);
float alpha__0_4 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_4 = g.getClip();
AffineTransform defaultTransform__0_4 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 83.0f, 0.0f));
// _0_4 is CompositeGraphicsNode
float alpha__0_4_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_4_0 = g.getClip();
AffineTransform defaultTransform__0_4_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_4_0 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(56.500003814697266, 13.926060676574707), new Point2D.Double(56.500003814697266, 35.46622085571289), new float[] {0.0f,1.0f}, new Color[] {new Color(186, 189, 182, 255),new Color(211, 215, 207, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(-1.0f, 0.0f, 0.0f, 1.0f, -16.0f, -1.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(-78.50001, 8.0);
((GeneralPath)shape).lineTo(-78.50001, 38.001827);
((GeneralPath)shape).curveTo(-78.50001, 38.83181, -77.839355, 39.499992, -77.01872, 39.499992);
((GeneralPath)shape).lineTo(-67.98128, 39.499992);
((GeneralPath)shape).curveTo(-67.16065, 39.499992, -66.5, 38.83181, -66.5, 38.001827);
((GeneralPath)shape).lineTo(-66.5, 8.0);
((GeneralPath)shape).lineTo(-65.5, 5.5);
((GeneralPath)shape).lineTo(-65.5, 10.5);
((GeneralPath)shape).lineTo(-65.5, 7.5);
((GeneralPath)shape).lineTo(-76.5, 7.5);
((GeneralPath)shape).lineTo(-76.5, 5.5);
((GeneralPath)shape).lineTo(-78.50001, 8.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(136, 138, 133, 255);
stroke = new BasicStroke(1.0000001f,2,1,7.1f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(-78.50001, 8.0);
((GeneralPath)shape).lineTo(-78.50001, 38.001827);
((GeneralPath)shape).curveTo(-78.50001, 38.83181, -77.839355, 39.499992, -77.01872, 39.499992);
((GeneralPath)shape).lineTo(-67.98128, 39.499992);
((GeneralPath)shape).curveTo(-67.16065, 39.499992, -66.5, 38.83181, -66.5, 38.001827);
((GeneralPath)shape).lineTo(-66.5, 8.0);
((GeneralPath)shape).lineTo(-65.5, 5.5);
((GeneralPath)shape).lineTo(-65.5, 10.5);
((GeneralPath)shape).lineTo(-65.5, 7.5);
((GeneralPath)shape).lineTo(-76.5, 7.5);
((GeneralPath)shape).lineTo(-76.5, 5.5);
((GeneralPath)shape).lineTo(-78.50001, 8.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_4_0;
g.setTransform(defaultTransform__0_4_0);
g.setClip(clip__0_4_0);
float alpha__0_4_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_4_1 = g.getClip();
AffineTransform defaultTransform__0_4_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_4_1 is ShapeNode
paint = new Color(186, 189, 182, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(-76.421875, 6.03125);
((GeneralPath)shape).lineTo(-77.8125, 7.6875);
((GeneralPath)shape).curveTo(-77.67165, 7.8804107, -77.454796, 8.0, -77.15625, 8.0);
((GeneralPath)shape).lineTo(-67.8125, 8.0);
((GeneralPath)shape).curveTo(-67.470116, 8.0, -67.13079, 7.8383584, -66.875, 7.59375);
((GeneralPath)shape).lineTo(-66.3125, 6.3125);
((GeneralPath)shape).curveTo(-66.3125, 4.3125, -75.52489, 4.8535533, -76.421875, 6.03125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_4_1;
g.setTransform(defaultTransform__0_4_1);
g.setClip(clip__0_4_1);
float alpha__0_4_2 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_4_2 = g.getClip();
AffineTransform defaultTransform__0_4_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_4_2 is ShapeNode
paint = new Color(217, 217, 213, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(-76.75, 7.15625);
((GeneralPath)shape).lineTo(-77.5, 8.0);
((GeneralPath)shape).curveTo(-77.5, 8.0, -77.5, 8.5, -77.0, 8.5);
((GeneralPath)shape).lineTo(-68.09375, 8.5);
((GeneralPath)shape).curveTo(-67.91508, 8.5, -67.5, 8.5, -67.5, 8.0);
((GeneralPath)shape).lineTo(-67.1875, 7.09375);
((GeneralPath)shape).curveTo(-68.02746, 5.375, -75.61531, 5.9272366, -76.75, 7.15625);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new LinearGradientPaint(new Point2D.Double(-71.72442626953125, 8.402941703796387), new Point2D.Double(-71.63724517822266, 6.193233013153076), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(-76.75, 7.15625);
((GeneralPath)shape).lineTo(-77.5, 8.0);
((GeneralPath)shape).curveTo(-77.5, 8.0, -77.5, 8.5, -77.0, 8.5);
((GeneralPath)shape).lineTo(-68.09375, 8.5);
((GeneralPath)shape).curveTo(-67.91508, 8.5, -67.5, 8.5, -67.5, 8.0);
((GeneralPath)shape).lineTo(-67.1875, 7.09375);
((GeneralPath)shape).curveTo(-68.02746, 5.375, -75.61531, 5.9272366, -76.75, 7.15625);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_4_2;
g.setTransform(defaultTransform__0_4_2);
g.setClip(clip__0_4_2);
float alpha__0_4_3 = origAlpha;
origAlpha = origAlpha * 0.7f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_4_3 = g.getClip();
AffineTransform defaultTransform__0_4_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_4_3 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(5.391689300537109, -38.885746002197266), new Point2D.Double(5.391689300537109, 8.933954238891602), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -83.0f, 49.0f));
stroke = new BasicStroke(1.0f,1,0,7.1f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(-67.49999, 8.506263);
((GeneralPath)shape).lineTo(-67.49999, 37.874996);
((GeneralPath)shape).curveTo(-67.49999, 38.22125, -67.77874, 38.499996, -68.12499, 38.499996);
((GeneralPath)shape).lineTo(-76.875, 38.499996);
((GeneralPath)shape).curveTo(-77.22125, 38.499996, -77.5, 38.22125, -77.5, 37.874996);
((GeneralPath)shape).lineTo(-77.5, 8.506263);
((GeneralPath)shape).lineTo(-67.49999, 8.506263);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_4_3;
g.setTransform(defaultTransform__0_4_3);
g.setClip(clip__0_4_3);
origAlpha = alpha__0_4;
g.setTransform(defaultTransform__0_4);
g.setClip(clip__0_4);
float alpha__0_5 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_5 = g.getClip();
AffineTransform defaultTransform__0_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_5 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(-6.4523491859436035, 17.329242706298828), new Point2D.Double(-6.4965434074401855, 13.086602210998535), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 29.0f, 1.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(18.84375, 4.40625);
((GeneralPath)shape).lineTo(17.15625, 14.8125);
((GeneralPath)shape).curveTo(17.330482, 15.104491, 17.633902, 15.3125, 18.0, 15.3125);
((GeneralPath)shape).lineTo(29.0, 15.3125);
((GeneralPath)shape).curveTo(29.366098, 15.3125, 29.669518, 15.104491, 29.84375, 14.8125);
((GeneralPath)shape).lineTo(28.15625, 4.40625);
((GeneralPath)shape).curveTo(25.024803, 5.7913017, 21.89788, 5.9787254, 18.84375, 4.40625);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_5;
g.setTransform(defaultTransform__0_5);
g.setClip(clip__0_5);
float alpha__0_6 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_6 = g.getClip();
AffineTransform defaultTransform__0_6 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_6 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(22.625, 20.624618530273438), new Point2D.Double(22.625, 38.75), new float[] {0.0f,1.0f}, new Color[] {new Color(114, 159, 207, 255),new Color(52, 101, 164, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(30.500004, 15.519495);
((GeneralPath)shape).lineTo(29.500004, 39.001827);
((GeneralPath)shape).curveTo(29.500004, 39.83181, 28.839354, 40.499992, 28.018724, 40.499992);
((GeneralPath)shape).lineTo(18.98128, 40.499992);
((GeneralPath)shape).curveTo(18.16065, 40.499992, 17.500002, 39.83181, 17.500002, 39.001827);
((GeneralPath)shape).lineTo(16.500002, 15.519495);
((GeneralPath)shape).lineTo(18.5, 4.5);
((GeneralPath)shape).lineTo(18.56932, 8.455806);
((GeneralPath)shape).lineTo(28.519068, 8.5);
((GeneralPath)shape).lineTo(28.5, 4.5);
((GeneralPath)shape).lineTo(30.500004, 15.519495);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(32, 74, 135, 255);
stroke = new BasicStroke(1.0000001f,2,1,7.1f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(30.500004, 15.519495);
((GeneralPath)shape).lineTo(29.500004, 39.001827);
((GeneralPath)shape).curveTo(29.500004, 39.83181, 28.839354, 40.499992, 28.018724, 40.499992);
((GeneralPath)shape).lineTo(18.98128, 40.499992);
((GeneralPath)shape).curveTo(18.16065, 40.499992, 17.500002, 39.83181, 17.500002, 39.001827);
((GeneralPath)shape).lineTo(16.500002, 15.519495);
((GeneralPath)shape).lineTo(18.5, 4.5);
((GeneralPath)shape).lineTo(18.56932, 8.455806);
((GeneralPath)shape).lineTo(28.519068, 8.5);
((GeneralPath)shape).lineTo(28.5, 4.5);
((GeneralPath)shape).lineTo(30.500004, 15.519495);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_6;
g.setTransform(defaultTransform__0_6);
g.setClip(clip__0_6);
float alpha__0_7 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_7 = g.getClip();
AffineTransform defaultTransform__0_7 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_7 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(18.250001907348633, 11.918621063232422), new Point2D.Double(28.615379333496094, 11.952705383300781), new float[] {0.0f,0.3243243f,0.83564645f,1.0f}, new Color[] {new Color(231, 231, 228, 255),new Color(255, 255, 255, 255),new Color(255, 255, 255, 255),new Color(211, 215, 207, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.9285719990730286f, 0.0f, 0.0f, 1.1100000143051147f, 1.6785600185394287f, -1.705001950263977f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(18.84375, 5.5625);
((GeneralPath)shape).lineTo(17.15625, 14.8125);
((GeneralPath)shape).curveTo(17.330482, 15.104491, 17.633902, 15.3125, 18.0, 15.3125);
((GeneralPath)shape).lineTo(29.0, 15.3125);
((GeneralPath)shape).curveTo(29.366098, 15.3125, 29.669518, 15.104491, 29.84375, 14.8125);
((GeneralPath)shape).lineTo(28.15625, 5.5625);
((GeneralPath)shape).curveTo(25.051937, 5.835068, 21.95209, 4.9123898, 18.84375, 5.5625);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_7;
g.setTransform(defaultTransform__0_7);
g.setClip(clip__0_7);
float alpha__0_8 = origAlpha;
origAlpha = origAlpha * 0.5f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_8 = g.getClip();
AffineTransform defaultTransform__0_8 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_8 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(19.375, 17.749723434448242), new Point2D.Double(19.375, 42.9442253112793), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.0f,2,1,7.1f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(17.5, 15.5);
((GeneralPath)shape).lineTo(18.46875, 38.96875);
((GeneralPath)shape).curveTo(18.468918, 38.979164, 18.468918, 38.989586, 18.46875, 39.0);
((GeneralPath)shape).curveTo(18.46875, 39.325665, 18.675425, 39.53125, 18.96875, 39.53125);
((GeneralPath)shape).lineTo(28.03125, 39.53125);
((GeneralPath)shape).curveTo(28.324577, 39.53125, 28.53125, 39.325665, 28.53125, 39.0);
((GeneralPath)shape).curveTo(28.531082, 38.989586, 28.531082, 38.979164, 28.53125, 38.96875);
((GeneralPath)shape).lineTo(29.5, 15.5);
((GeneralPath)shape).lineTo(17.5, 15.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_8;
g.setTransform(defaultTransform__0_8);
g.setClip(clip__0_8);
float alpha__0_9 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_9 = g.getClip();
AffineTransform defaultTransform__0_9 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_9 is ShapeNode
paint = new Color(186, 189, 182, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(18.71875, 6.3212123);
((GeneralPath)shape).lineTo(18.875, 5.1962123);
((GeneralPath)shape).curveTo(22.042187, 3.8586583, 24.858465, 3.901964, 28.0625, 5.0868373);
((GeneralPath)shape).lineTo(28.3125, 6.3524623);
((GeneralPath)shape).curveTo(25.135082, 4.8843265, 21.88334, 5.046444, 18.71875, 6.3212123);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_9;
g.setTransform(defaultTransform__0_9);
g.setClip(clip__0_9);
float alpha__0_10 = origAlpha;
origAlpha = origAlpha * 0.2628571f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_10 = g.getClip();
AffineTransform defaultTransform__0_10 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_10 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(23.643882751464844, 14.324039459228516), new Point2D.Double(23.643882751464844, 12.60046672821045), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(17.854446, 15.235651);
((GeneralPath)shape).lineTo(29.43332, 15.324039);
((GeneralPath)shape).lineTo(28.726213, 12.230447);
((GeneralPath)shape).lineTo(18.119612, 11.876894);
((GeneralPath)shape).lineTo(17.854446, 15.235651);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_10;
g.setTransform(defaultTransform__0_10);
g.setClip(clip__0_10);
float alpha__0_11 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_11 = g.getClip();
AffineTransform defaultTransform__0_11 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_11 is ShapeNode
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(1.0000002f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(27.858713, 6.730838);
((GeneralPath)shape).curveTo(25.238676, 5.3955965, 21.879932, 5.1240983, 19.129932, 6.716716);
((GeneralPath)shape).lineTo(17.588375, 15.228661);
((GeneralPath)shape).curveTo(17.704845, 15.382909, 17.857544, 15.501406, 18.045033, 15.501406);
((GeneralPath)shape).lineTo(28.93285, 15.501406);
((GeneralPath)shape).curveTo(29.120338, 15.501406, 29.273037, 15.382909, 29.389507, 15.228661);
((GeneralPath)shape).lineTo(27.858713, 6.730838);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_11;
g.setTransform(defaultTransform__0_11);
g.setClip(clip__0_11);
float alpha__0_12 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_12 = g.getClip();
AffineTransform defaultTransform__0_12 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 49.0f, 1.0f));
// _0_12 is CompositeGraphicsNode
float alpha__0_12_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_12_0 = g.getClip();
AffineTransform defaultTransform__0_12_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_12_0 is ShapeNode
paint = new Color(207, 207, 201, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(-29.5, 16.5);
((GeneralPath)shape).lineTo(-29.25, 22.5);
((GeneralPath)shape).lineTo(-21.75, 22.5);
((GeneralPath)shape).lineTo(-21.5, 16.5);
((GeneralPath)shape).lineTo(-29.5, 16.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(226, 226, 226, 255);
stroke = new BasicStroke(0.9999999f,1,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(-29.5, 16.5);
((GeneralPath)shape).lineTo(-29.25, 22.5);
((GeneralPath)shape).lineTo(-21.75, 22.5);
((GeneralPath)shape).lineTo(-21.5, 16.5);
((GeneralPath)shape).lineTo(-29.5, 16.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_12_0;
g.setTransform(defaultTransform__0_12_0);
g.setClip(clip__0_12_0);
float alpha__0_12_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_12_1 = g.getClip();
AffineTransform defaultTransform__0_12_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_12_1 is CompositeGraphicsNode
float alpha__0_12_1_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_12_1_0 = g.getClip();
AffineTransform defaultTransform__0_12_1_0 = g.getTransform();
g.transform(new AffineTransform(0.9697459936141968f, 0.0f, 0.0f, 0.8198350071907043f, -35.65713882446289f, 6.365330219268799f));
// _0_12_1_0 is ShapeNode
paint = new Color(136, 138, 133, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(13.567611, 32.487846);
((GeneralPath)shape).curveTo(13.568045, 33.577576, 12.978504, 34.584713, 12.021166, 35.1297);
((GeneralPath)shape).curveTo(11.063827, 35.67469, 9.884211, 35.67469, 8.926872, 35.1297);
((GeneralPath)shape).curveTo(7.969533, 34.584713, 7.379993, 33.577576, 7.380427, 32.487846);
((GeneralPath)shape).curveTo(7.379993, 31.398117, 7.969533, 30.390982, 8.926872, 29.845993);
((GeneralPath)shape).curveTo(9.884211, 29.301004, 11.063827, 29.301004, 12.021166, 29.845993);
((GeneralPath)shape).curveTo(12.978504, 30.390982, 13.568045, 31.398117, 13.567611, 32.487846);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(32, 74, 135, 255);
stroke = new BasicStroke(1.1215221f,1,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(13.567611, 32.487846);
((GeneralPath)shape).curveTo(13.568045, 33.577576, 12.978504, 34.584713, 12.021166, 35.1297);
((GeneralPath)shape).curveTo(11.063827, 35.67469, 9.884211, 35.67469, 8.926872, 35.1297);
((GeneralPath)shape).curveTo(7.969533, 34.584713, 7.379993, 33.577576, 7.380427, 32.487846);
((GeneralPath)shape).curveTo(7.379993, 31.398117, 7.969533, 30.390982, 8.926872, 29.845993);
((GeneralPath)shape).curveTo(9.884211, 29.301004, 11.063827, 29.301004, 12.021166, 29.845993);
((GeneralPath)shape).curveTo(12.978504, 30.390982, 13.568045, 31.398117, 13.567611, 32.487846);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_12_1_0;
g.setTransform(defaultTransform__0_12_1_0);
g.setClip(clip__0_12_1_0);
float alpha__0_12_1_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_12_1_1 = g.getClip();
AffineTransform defaultTransform__0_12_1_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_12_1_1 is ShapeNode
paint = new Color(211, 215, 207, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(-25.5, 32.015614);
((GeneralPath)shape).curveTo(-26.699953, 32.015614, -27.674465, 32.4676, -27.90625, 33.41666);
((GeneralPath)shape).curveTo(-27.674465, 34.36572, -26.668703, 35.083332, -25.46875, 35.083332);
((GeneralPath)shape).curveTo(-24.268797, 35.083332, -23.294285, 34.365723, -23.0625, 33.41666);
((GeneralPath)shape).curveTo(-23.294285, 32.4676, -24.300047, 32.015614, -25.5, 32.015614);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_12_1_1;
g.setTransform(defaultTransform__0_12_1_1);
g.setClip(clip__0_12_1_1);
origAlpha = alpha__0_12_1;
g.setTransform(defaultTransform__0_12_1);
g.setClip(clip__0_12_1);
origAlpha = alpha__0_12;
g.setTransform(defaultTransform__0_12);
g.setClip(clip__0_12);
float alpha__0_13 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_13 = g.getClip();
AffineTransform defaultTransform__0_13 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 49.0f, 0.0f));
// _0_13 is CompositeGraphicsNode
float alpha__0_13_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_13_0 = g.getClip();
AffineTransform defaultTransform__0_13_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_13_0 is ShapeNode
paint = new Color(238, 238, 236, 255);
shape = new Rectangle2D.Double(-41.499996185302734, 11.499996185302734, 5.999997138977051, 7.000004291534424);
g.setPaint(paint);
g.fill(shape);
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(0.9999999f,1,1,4.0f,null,0.0f);
shape = new Rectangle2D.Double(-41.499996185302734, 11.499996185302734, 5.999997138977051, 7.000004291534424);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_13_0;
g.setTransform(defaultTransform__0_13_0);
g.setClip(clip__0_13_0);
float alpha__0_13_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_13_1 = g.getClip();
AffineTransform defaultTransform__0_13_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -49.0f, 0.0f));
// _0_13_1 is CompositeGraphicsNode
float alpha__0_13_1_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_13_1_0 = g.getClip();
AffineTransform defaultTransform__0_13_1_0 = g.getTransform();
g.transform(new AffineTransform(0.9697459936141968f, 0.0f, 0.0f, 0.9837989807128906f, 0.3428570032119751f, -0.46149998903274536f));
// _0_13_1_0 is ShapeNode
paint = new Color(186, 189, 182, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(13.567611, 32.487846);
((GeneralPath)shape).curveTo(13.568045, 33.577576, 12.978504, 34.584713, 12.021166, 35.1297);
((GeneralPath)shape).curveTo(11.063827, 35.67469, 9.884211, 35.67469, 8.926872, 35.1297);
((GeneralPath)shape).curveTo(7.969533, 34.584713, 7.379993, 33.577576, 7.380427, 32.487846);
((GeneralPath)shape).curveTo(7.379993, 31.398117, 7.969533, 30.390982, 8.926872, 29.845993);
((GeneralPath)shape).curveTo(9.884211, 29.301004, 11.063827, 29.301004, 12.021166, 29.845993);
((GeneralPath)shape).curveTo(12.978504, 30.390982, 13.568045, 31.398117, 13.567611, 32.487846);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(136, 138, 133, 255);
stroke = new BasicStroke(1.0238063f,1,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(13.567611, 32.487846);
((GeneralPath)shape).curveTo(13.568045, 33.577576, 12.978504, 34.584713, 12.021166, 35.1297);
((GeneralPath)shape).curveTo(11.063827, 35.67469, 9.884211, 35.67469, 8.926872, 35.1297);
((GeneralPath)shape).curveTo(7.969533, 34.584713, 7.379993, 33.577576, 7.380427, 32.487846);
((GeneralPath)shape).curveTo(7.379993, 31.398117, 7.969533, 30.390982, 8.926872, 29.845993);
((GeneralPath)shape).curveTo(9.884211, 29.301004, 11.063827, 29.301004, 12.021166, 29.845993);
((GeneralPath)shape).curveTo(12.978504, 30.390982, 13.568045, 31.398117, 13.567611, 32.487846);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_13_1_0;
g.setTransform(defaultTransform__0_13_1_0);
g.setClip(clip__0_13_1_0);
float alpha__0_13_1_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_13_1_1 = g.getClip();
AffineTransform defaultTransform__0_13_1_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_13_1_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(36.5625, -16.999677658081055), new Point2D.Double(36.5625, -20.594276428222656), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -26.0f, 49.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(10.5, 30.0);
((GeneralPath)shape).curveTo(9.300046, 30.0, 8.325534, 30.861134, 8.09375, 32.0);
((GeneralPath)shape).curveTo(8.325534, 33.138866, 9.331296, 34.0, 10.53125, 34.0);
((GeneralPath)shape).curveTo(11.731204, 34.0, 12.705716, 33.138866, 12.9375, 32.0);
((GeneralPath)shape).curveTo(12.705715, 30.861134, 11.699953, 30.0, 10.5, 30.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_13_1_1;
g.setTransform(defaultTransform__0_13_1_1);
g.setClip(clip__0_13_1_1);
origAlpha = alpha__0_13_1;
g.setTransform(defaultTransform__0_13_1);
g.setClip(clip__0_13_1);
origAlpha = alpha__0_13;
g.setTransform(defaultTransform__0_13);
g.setClip(clip__0_13);
float alpha__0_14 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_14 = g.getClip();
AffineTransform defaultTransform__0_14 = g.getTransform();
g.transform(new AffineTransform(0.9697459936141968f, 0.0f, 0.0f, 0.9837989807128906f, 26.342859268188477f, -0.46149998903274536f));
// _0_14 is ShapeNode
paint = new Color(186, 189, 182, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(13.567611, 32.487846);
((GeneralPath)shape).curveTo(13.568045, 33.577576, 12.978504, 34.584713, 12.021166, 35.1297);
((GeneralPath)shape).curveTo(11.063827, 35.67469, 9.884211, 35.67469, 8.926872, 35.1297);
((GeneralPath)shape).curveTo(7.969533, 34.584713, 7.379993, 33.577576, 7.380427, 32.487846);
((GeneralPath)shape).curveTo(7.379993, 31.398117, 7.969533, 30.390982, 8.926872, 29.845993);
((GeneralPath)shape).curveTo(9.884211, 29.301004, 11.063827, 29.301004, 12.021166, 29.845993);
((GeneralPath)shape).curveTo(12.978504, 30.390982, 13.568045, 31.398117, 13.567611, 32.487846);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(78, 154, 6, 255);
stroke = new BasicStroke(1.0238063f,1,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(13.567611, 32.487846);
((GeneralPath)shape).curveTo(13.568045, 33.577576, 12.978504, 34.584713, 12.021166, 35.1297);
((GeneralPath)shape).curveTo(11.063827, 35.67469, 9.884211, 35.67469, 8.926872, 35.1297);
((GeneralPath)shape).curveTo(7.969533, 34.584713, 7.379993, 33.577576, 7.380427, 32.487846);
((GeneralPath)shape).curveTo(7.379993, 31.398117, 7.969533, 30.390982, 8.926872, 29.845993);
((GeneralPath)shape).curveTo(9.884211, 29.301004, 11.063827, 29.301004, 12.021166, 29.845993);
((GeneralPath)shape).curveTo(12.978504, 30.390982, 13.568045, 31.398117, 13.567611, 32.487846);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_14;
g.setTransform(defaultTransform__0_14);
g.setClip(clip__0_14);
float alpha__0_15 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_15 = g.getClip();
AffineTransform defaultTransform__0_15 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 49.0f, 0.0f));
// _0_15 is CompositeGraphicsNode
float alpha__0_15_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_15_0 = g.getClip();
AffineTransform defaultTransform__0_15_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_15_0 is ShapeNode
paint = new Color(238, 238, 236, 255);
shape = new Rectangle2D.Double(-15.499996185302734, 11.499996185302734, 5.999997138977051, 7.000004291534424);
g.setPaint(paint);
g.fill(shape);
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(0.9999999f,1,1,4.0f,null,0.0f);
shape = new Rectangle2D.Double(-15.499996185302734, 11.499996185302734, 5.999997138977051, 7.000004291534424);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_15_0;
g.setTransform(defaultTransform__0_15_0);
g.setClip(clip__0_15_0);
float alpha__0_15_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_15_1 = g.getClip();
AffineTransform defaultTransform__0_15_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_15_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(36.5625, -16.999677658081055), new Point2D.Double(36.5625, -20.594276428222656), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -49.0f, 49.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(-12.5, 30.0);
((GeneralPath)shape).curveTo(-13.699954, 30.0, -14.674466, 30.861134, -14.90625, 32.0);
((GeneralPath)shape).curveTo(-14.674466, 33.138866, -13.668704, 34.0, -12.46875, 34.0);
((GeneralPath)shape).curveTo(-11.268796, 34.0, -10.294284, 33.138866, -10.0625, 32.0);
((GeneralPath)shape).curveTo(-10.294285, 30.861134, -11.300047, 30.0, -12.5, 30.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_15_1;
g.setTransform(defaultTransform__0_15_1);
g.setClip(clip__0_15_1);
origAlpha = alpha__0_15;
g.setTransform(defaultTransform__0_15);
g.setClip(clip__0_15);
float alpha__0_16 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_16 = g.getClip();
AffineTransform defaultTransform__0_16 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_16 is ShapeNode
paint = new Color(186, 189, 182, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(40.421875, 6.0529866);
((GeneralPath)shape).lineTo(41.8125, 7.7092366);
((GeneralPath)shape).curveTo(41.671654, 7.9021473, 41.454796, 8.021736, 41.15625, 8.021737);
((GeneralPath)shape).lineTo(31.8125, 8.021737);
((GeneralPath)shape).curveTo(31.470114, 8.021737, 31.13079, 7.860095, 30.875, 7.6154866);
((GeneralPath)shape).lineTo(30.3125, 6.3342366);
((GeneralPath)shape).curveTo(30.3125, 4.3342366, 39.52489, 4.87529, 40.421875, 6.0529866);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_16;
g.setTransform(defaultTransform__0_16);
g.setClip(clip__0_16);
float alpha__0_17 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_17 = g.getClip();
AffineTransform defaultTransform__0_17 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_17 is ShapeNode
paint = new Color(217, 217, 213, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(40.75, 7.1779866);
((GeneralPath)shape).lineTo(41.5, 8.021737);
((GeneralPath)shape).curveTo(41.5, 8.021737, 41.5, 8.521737, 41.0, 8.521737);
((GeneralPath)shape).lineTo(32.09375, 8.521737);
((GeneralPath)shape).curveTo(31.915073, 8.521737, 31.5, 8.521737, 31.5, 8.021737);
((GeneralPath)shape).lineTo(31.1875, 7.1154866);
((GeneralPath)shape).curveTo(32.02746, 5.3967366, 39.61531, 5.948973, 40.75, 7.1779866);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new LinearGradientPaint(new Point2D.Double(35.283695220947266, 7.982736587524414), new Point2D.Double(35.194095611572266, 6.325018405914307), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(40.75, 7.1779866);
((GeneralPath)shape).lineTo(41.5, 8.021737);
((GeneralPath)shape).curveTo(41.5, 8.021737, 41.5, 8.521737, 41.0, 8.521737);
((GeneralPath)shape).lineTo(32.09375, 8.521737);
((GeneralPath)shape).curveTo(31.915073, 8.521737, 31.5, 8.521737, 31.5, 8.021737);
((GeneralPath)shape).lineTo(31.1875, 7.1154866);
((GeneralPath)shape).curveTo(32.02746, 5.3967366, 39.61531, 5.948973, 40.75, 7.1779866);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_17;
g.setTransform(defaultTransform__0_17);
g.setClip(clip__0_17);
float alpha__0_18 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_18 = g.getClip();
AffineTransform defaultTransform__0_18 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 0.9601590037345886f, 0.0f, 1.2577190399169922f));
// _0_18 is CompositeGraphicsNode
float alpha__0_18_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_18_0 = g.getClip();
AffineTransform defaultTransform__0_18_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_18_0 is ShapeNode
paint = new Color(186, 189, 182, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(22.6875, 14.3125);
((GeneralPath)shape).lineTo(23.1875, 6.46875);
((GeneralPath)shape).lineTo(23.375, 14.3125);
((GeneralPath)shape).lineTo(22.6875, 14.3125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_18_0;
g.setTransform(defaultTransform__0_18_0);
g.setClip(clip__0_18_0);
float alpha__0_18_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_18_1 = g.getClip();
AffineTransform defaultTransform__0_18_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_18_1 is ShapeNode
paint = new Color(186, 189, 182, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(25.6875, 14.3125);
((GeneralPath)shape).lineTo(25.6875, 7.75);
((GeneralPath)shape).lineTo(26.375, 14.3125);
((GeneralPath)shape).lineTo(25.6875, 14.3125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_18_1;
g.setTransform(defaultTransform__0_18_1);
g.setClip(clip__0_18_1);
float alpha__0_18_2 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_18_2 = g.getClip();
AffineTransform defaultTransform__0_18_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_18_2 is ShapeNode
paint = new Color(186, 189, 182, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(19.6875, 14.3125);
((GeneralPath)shape).lineTo(20.46875, 9.9375);
((GeneralPath)shape).lineTo(20.375, 14.3125);
((GeneralPath)shape).lineTo(19.6875, 14.3125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_18_2;
g.setTransform(defaultTransform__0_18_2);
g.setClip(clip__0_18_2);
origAlpha = alpha__0_18;
g.setTransform(defaultTransform__0_18);
g.setClip(clip__0_18);
float alpha__0_19 = origAlpha;
origAlpha = origAlpha * 0.22285713f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_19 = g.getClip();
AffineTransform defaultTransform__0_19 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f));
// _0_19 is CompositeGraphicsNode
float alpha__0_19_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_19_0 = g.getClip();
AffineTransform defaultTransform__0_19_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_19_0 is ShapeNode
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(0.9999998f,2,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(21.416658, 18.5);
((GeneralPath)shape).lineTo(25.583324, 18.5);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_19_0;
g.setTransform(defaultTransform__0_19_0);
g.setClip(clip__0_19_0);
float alpha__0_19_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_19_1 = g.getClip();
AffineTransform defaultTransform__0_19_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_19_1 is ShapeNode
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(0.9999998f,2,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(21.333336, 20.5);
((GeneralPath)shape).lineTo(25.5, 20.5);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_19_1;
g.setTransform(defaultTransform__0_19_1);
g.setClip(clip__0_19_1);
origAlpha = alpha__0_19;
g.setTransform(defaultTransform__0_19);
g.setClip(clip__0_19);
float alpha__0_20 = origAlpha;
origAlpha = origAlpha * 0.1f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_20 = g.getClip();
AffineTransform defaultTransform__0_20 = g.getTransform();
g.transform(new AffineTransform(0.9523839950561523f, 0.0f, 0.0f, 1.0f, 14.158659934997559f, -5.0f));
// _0_20 is CompositeGraphicsNode
float alpha__0_20_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_20_0 = g.getClip();
AffineTransform defaultTransform__0_20_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_20_0 is ShapeNode
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(1.0246933f,2,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(21.416658, 18.5);
((GeneralPath)shape).lineTo(25.583324, 18.5);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_20_0;
g.setTransform(defaultTransform__0_20_0);
g.setClip(clip__0_20_0);
float alpha__0_20_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_20_1 = g.getClip();
AffineTransform defaultTransform__0_20_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_20_1 is ShapeNode
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(1.0246933f,2,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(21.333336, 20.5);
((GeneralPath)shape).lineTo(25.5, 20.5);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_20_1;
g.setTransform(defaultTransform__0_20_1);
g.setClip(clip__0_20_1);
origAlpha = alpha__0_20;
g.setTransform(defaultTransform__0_20);
g.setClip(clip__0_20);
float alpha__0_21 = origAlpha;
origAlpha = origAlpha * 0.1f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_21 = g.getClip();
AffineTransform defaultTransform__0_21 = g.getTransform();
g.transform(new AffineTransform(0.9523839950561523f, 0.0f, 0.0f, 1.0f, -11.841329574584961f, -5.0f));
// _0_21 is CompositeGraphicsNode
float alpha__0_21_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_21_0 = g.getClip();
AffineTransform defaultTransform__0_21_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_21_0 is ShapeNode
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(1.0246934f,2,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(21.416658, 18.5);
((GeneralPath)shape).lineTo(25.583324, 18.5);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_21_0;
g.setTransform(defaultTransform__0_21_0);
g.setClip(clip__0_21_0);
float alpha__0_21_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_21_1 = g.getClip();
AffineTransform defaultTransform__0_21_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_21_1 is ShapeNode
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(1.0246934f,2,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(21.333336, 20.5);
((GeneralPath)shape).lineTo(25.5, 20.5);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_21_1;
g.setTransform(defaultTransform__0_21_1);
g.setClip(clip__0_21_1);
origAlpha = alpha__0_21;
g.setTransform(defaultTransform__0_21);
g.setClip(clip__0_21);
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
	public TermTypesIcon() {
        this.width = getOrigWidth();
        this.height = getOrigHeight();
	}
	
	/**
	 * Creates a new transcoded SVG image with the given dimensions.
	 *
	 * @param size the dimensions of the icon
	 */
	public TermTypesIcon(Dimension size) {
	this.width = size.width;
	this.height = size.width;
	}

	public TermTypesIcon(int width, int height) {
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

