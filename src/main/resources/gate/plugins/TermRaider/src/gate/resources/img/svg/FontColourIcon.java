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
public class FontColourIcon implements
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
float alpha__0_0_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0 = g.getClip();
AffineTransform defaultTransform__0_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -1.2374368906021118f, -2.4748737812042236f));
// _0_0_0 is CompositeGraphicsNode
float alpha__0_0_0_0 = origAlpha;
origAlpha = origAlpha * 0.5f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0353939533233643f, 0.0f, 0.0f, 1.0153329372406006f, -0.0133689995855093f, -1.1077728271484375f));
// _0_0_0_0 is ShapeNode
paint = new RadialGradientPaint(new Point2D.Double(24.174213409423828, 38.7634162902832), 22.22967f, new Point2D.Double(24.174213409423828, 38.7634162902832), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.23260439932346344f, 0.0f, 29.746875762939453f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(46.403885, 38.763416);
((GeneralPath)shape).curveTo(46.403885, 41.619125, 36.451324, 43.934135, 24.174215, 43.934135);
((GeneralPath)shape).curveTo(11.897108, 43.934135, 1.9445457, 41.619125, 1.9445457, 38.763416);
((GeneralPath)shape).curveTo(1.9445457, 35.907707, 11.897108, 33.592697, 24.174215, 33.592697);
((GeneralPath)shape).curveTo(36.451324, 33.592697, 46.403885, 35.907707, 46.403885, 38.763416);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_0;
g.setTransform(defaultTransform__0_0_0_0);
g.setClip(clip__0_0_0_0);
float alpha__0_0_0_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(29.037691116333008, 20.96454620361328), new Point2D.Double(32.5, 56.763458251953125), new float[] {0.0f,1.0f}, new Color[] {new Color(85, 87, 83, 255),new Color(136, 138, 133, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -1.0f, -3.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(39.5, 19.0);
((GeneralPath)shape).curveTo(39.50004, 23.72492, 39.5, 35.5, 39.5, 35.5);
((GeneralPath)shape).lineTo(43.5, 35.5);
((GeneralPath)shape).lineTo(43.5, 39.5);
((GeneralPath)shape).lineTo(27.5, 39.5);
((GeneralPath)shape).lineTo(27.5, 35.5);
((GeneralPath)shape).curveTo(25.939217, 37.24844, 24.277012, 38.41853, 22.359531, 39.25112);
((GeneralPath)shape).curveTo(20.44201, 40.08371, 18.25695, 40.5, 15.804347, 40.5);
((GeneralPath)shape).curveTo(12.17, 40.5, 10.371786, 39.59456, 8.409698, 37.78368);
((GeneralPath)shape).curveTo(6.469895, 35.95198, 5.499997, 33.33973, 5.5, 29.94692);
((GeneralPath)shape).curveTo(5.499997, 26.2211, 6.893529, 23.43192, 9.680601, 21.5794);
((GeneralPath)shape).curveTo(12.489957, 19.7269, 15.358959, 18.50002, 21.0, 18.5);
((GeneralPath)shape).lineTo(27.5, 18.5);
((GeneralPath)shape).lineTo(27.5, 15.0);
((GeneralPath)shape).curveTo(27.5, 11.5, 23.5, 10.5, 19.5, 10.5);
((GeneralPath)shape).curveTo(15.5, 10.5, 12.411251, 12.10142, 11.136363, 16.5);
((GeneralPath)shape).lineTo(7.5, 16.5);
((GeneralPath)shape).lineTo(7.5, 8.5);
((GeneralPath)shape).curveTo(11.858868, 7.0523, 16.45034, 6.56488, 21.021738, 6.5);
((GeneralPath)shape).curveTo(27.376226, 6.50003, 32.025055, 7.60321, 34.968227, 9.80955);
((GeneralPath)shape).curveTo(37.93363, 12.01594, 39.49996, 14.27508, 39.5, 19.0);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(27.5, 29.0);
((GeneralPath)shape).lineTo(27.5, 22.5);
((GeneralPath)shape).lineTo(23.05184, 22.5);
((GeneralPath)shape).curveTo(20.844463, 22.50002, 19.226849, 23.07807, 18.04515, 24.20205);
((GeneralPath)shape).curveTo(16.863419, 25.32606, 16.27256, 26.939209, 16.272575, 29.04148);
((GeneralPath)shape).curveTo(16.27256, 31.14378, 16.696194, 32.71529, 17.543478, 33.75602);
((GeneralPath)shape).curveTo(18.413027, 34.79676, 19.728521, 35.31713, 21.489967, 35.31713);
((GeneralPath)shape).curveTo(23.318262, 35.31713, 24.789831, 34.75513, 25.904682, 33.63113);
((GeneralPath)shape).curveTo(27.019484, 32.50715, 27.499973, 30.85252, 27.5, 29.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(46, 52, 54, 255);
stroke = new BasicStroke(0.99999994f,0,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(39.5, 19.0);
((GeneralPath)shape).curveTo(39.50004, 23.72492, 39.5, 35.5, 39.5, 35.5);
((GeneralPath)shape).lineTo(43.5, 35.5);
((GeneralPath)shape).lineTo(43.5, 39.5);
((GeneralPath)shape).lineTo(27.5, 39.5);
((GeneralPath)shape).lineTo(27.5, 35.5);
((GeneralPath)shape).curveTo(25.939217, 37.24844, 24.277012, 38.41853, 22.359531, 39.25112);
((GeneralPath)shape).curveTo(20.44201, 40.08371, 18.25695, 40.5, 15.804347, 40.5);
((GeneralPath)shape).curveTo(12.17, 40.5, 10.371786, 39.59456, 8.409698, 37.78368);
((GeneralPath)shape).curveTo(6.469895, 35.95198, 5.499997, 33.33973, 5.5, 29.94692);
((GeneralPath)shape).curveTo(5.499997, 26.2211, 6.893529, 23.43192, 9.680601, 21.5794);
((GeneralPath)shape).curveTo(12.489957, 19.7269, 15.358959, 18.50002, 21.0, 18.5);
((GeneralPath)shape).lineTo(27.5, 18.5);
((GeneralPath)shape).lineTo(27.5, 15.0);
((GeneralPath)shape).curveTo(27.5, 11.5, 23.5, 10.5, 19.5, 10.5);
((GeneralPath)shape).curveTo(15.5, 10.5, 12.411251, 12.10142, 11.136363, 16.5);
((GeneralPath)shape).lineTo(7.5, 16.5);
((GeneralPath)shape).lineTo(7.5, 8.5);
((GeneralPath)shape).curveTo(11.858868, 7.0523, 16.45034, 6.56488, 21.021738, 6.5);
((GeneralPath)shape).curveTo(27.376226, 6.50003, 32.025055, 7.60321, 34.968227, 9.80955);
((GeneralPath)shape).curveTo(37.93363, 12.01594, 39.49996, 14.27508, 39.5, 19.0);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(27.5, 29.0);
((GeneralPath)shape).lineTo(27.5, 22.5);
((GeneralPath)shape).lineTo(23.05184, 22.5);
((GeneralPath)shape).curveTo(20.844463, 22.50002, 19.226849, 23.07807, 18.04515, 24.20205);
((GeneralPath)shape).curveTo(16.863419, 25.32606, 16.27256, 26.939209, 16.272575, 29.04148);
((GeneralPath)shape).curveTo(16.27256, 31.14378, 16.696194, 32.71529, 17.543478, 33.75602);
((GeneralPath)shape).curveTo(18.413027, 34.79676, 19.728521, 35.31713, 21.489967, 35.31713);
((GeneralPath)shape).curveTo(23.318262, 35.31713, 24.789831, 34.75513, 25.904682, 33.63113);
((GeneralPath)shape).curveTo(27.019484, 32.50715, 27.499973, 30.85252, 27.5, 29.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_0_1;
g.setTransform(defaultTransform__0_0_0_1);
g.setClip(clip__0_0_0_1);
float alpha__0_0_0_2 = origAlpha;
origAlpha = origAlpha * 0.15f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_2 = g.getClip();
AffineTransform defaultTransform__0_0_0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_2 is ShapeNode
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(0.99999994f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(21.03125, 7.5);
((GeneralPath)shape).curveTo(16.76025, 7.56062, 12.52531, 8.07522, 8.5, 9.3125);
((GeneralPath)shape).lineTo(8.5, 15.5);
((GeneralPath)shape).lineTo(10.5, 15.5);
((GeneralPath)shape).curveTo(11.22118, 13.53106, 12.26568, 11.96255, 13.71875, 11.0);
((GeneralPath)shape).curveTo(15.36392, 9.9102, 17.36531, 9.5, 19.5, 9.5);
((GeneralPath)shape).curveTo(21.584108, 9.5, 23.681587, 9.74078, 25.40625, 10.53125);
((GeneralPath)shape).curveTo(27.130913, 11.32172, 28.5, 12.86551, 28.5, 15.0);
((GeneralPath)shape).lineTo(28.5, 18.5);
((GeneralPath)shape).curveTo(28.4949, 19.05016, 28.05016, 19.4949, 27.5, 19.5);
((GeneralPath)shape).lineTo(21.0, 19.5);
((GeneralPath)shape).curveTo(15.5070095, 19.50002, 12.96933, 20.64437, 10.25, 22.4375);
((GeneralPath)shape).curveTo(7.71876, 24.11998, 6.5, 26.46458, 6.5, 29.9375);
((GeneralPath)shape).curveTo(6.5, 33.13864, 7.3814898, 35.44567, 9.09375, 37.0625);
((GeneralPath)shape).curveTo(10.95207, 38.77762, 12.34623, 39.5, 15.8125, 39.5);
((GeneralPath)shape).curveTo(18.150589, 39.5, 20.195984, 39.08223, 21.96875, 38.3125);
((GeneralPath)shape).curveTo(23.763294, 37.53329, 25.275253, 36.46456, 26.75, 34.8125);
((GeneralPath)shape).curveTo(27.0324, 34.50755, 27.47285, 34.40687, 27.859694, 34.55885);
((GeneralPath)shape).curveTo(28.246536, 34.71082, 28.50068, 35.08438, 28.5, 35.5);
((GeneralPath)shape).lineTo(28.5, 38.5);
((GeneralPath)shape).lineTo(42.5, 38.5);
((GeneralPath)shape).lineTo(42.5, 36.5);
((GeneralPath)shape).lineTo(39.5, 36.5);
((GeneralPath)shape).curveTo(38.94984, 36.4949, 38.505096, 36.05016, 38.5, 35.5);
((GeneralPath)shape).curveTo(38.5, 35.5, 38.50004, 23.7249, 38.5, 19.0);
((GeneralPath)shape).curveTo(38.499966, 14.49806, 37.212547, 12.736259, 34.375, 10.625);
((GeneralPath)shape).curveTo(31.709625, 8.62692, 27.268616, 7.50003, 21.03125, 7.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_0_2;
g.setTransform(defaultTransform__0_0_0_2);
g.setClip(clip__0_0_0_2);
float alpha__0_0_0_3 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_3 = g.getClip();
AffineTransform defaultTransform__0_0_0_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_3 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(28.21656608581543, 38.6302604675293), new Point2D.Double(20.90625, 21.48896026611328), new float[] {0.0f,1.0f}, new Color[] {new Color(186, 189, 182, 255),new Color(186, 189, 182, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -3.0f, -3.0f));
stroke = new BasicStroke(0.99999994f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(23.0625, 21.5);
((GeneralPath)shape).lineTo(27.5, 21.5);
((GeneralPath)shape).curveTo(28.05016, 21.5051, 28.494902, 21.94984, 28.5, 22.5);
((GeneralPath)shape).lineTo(28.5, 29.0);
((GeneralPath)shape).curveTo(28.49997, 31.02646, 27.985485, 32.97206, 26.625, 34.34375);
((GeneralPath)shape).curveTo(25.318007, 35.66147, 23.535683, 36.31251, 21.5, 36.3125);
((GeneralPath)shape).curveTo(19.539242, 36.31251, 17.839699, 35.71049, 16.75, 34.40625);
((GeneralPath)shape).curveTo(16.74984, 34.39583, 16.74984, 34.38542, 16.75, 34.375);
((GeneralPath)shape).curveTo(15.686664, 33.06889, 15.281234, 31.26245, 15.28125, 29.03125);
((GeneralPath)shape).curveTo(15.281234, 26.74708, 15.928747, 24.81464, 17.34375, 23.46875);
((GeneralPath)shape).curveTo(18.741728, 22.139061, 20.683357, 21.50002, 23.0625, 21.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_0_3;
g.setTransform(defaultTransform__0_0_0_3);
g.setClip(clip__0_0_0_3);
float alpha__0_0_0_4 = origAlpha;
origAlpha = origAlpha * 0.55f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_4 = g.getClip();
AffineTransform defaultTransform__0_0_0_4 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_4 is ShapeNode
paint = new RadialGradientPaint(new Point2D.Double(-42.750152587890625, 5.346876621246338), 15.500008f, new Point2D.Double(-42.750152587890625, 5.346876621246338), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(2.300097703933716f, 0.0f, 0.0f, 1.8146644830703735f, 120.45453643798828f, -4.827786922454834f));
stroke = new BasicStroke(1.0f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(38.5, 35.5);
((GeneralPath)shape).curveTo(38.5, 35.5, 38.50004, 23.7249, 38.5, 19.0);
((GeneralPath)shape).curveTo(38.499966, 14.49806, 37.212547, 12.736259, 34.375, 10.625);
((GeneralPath)shape).curveTo(31.709625, 8.62692, 27.268616, 7.50003, 21.03125, 7.5);
((GeneralPath)shape).curveTo(16.760252, 7.56062, 12.525309, 8.07522, 8.5, 9.3125);
((GeneralPath)shape).lineTo(8.5, 15.5);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_0_4;
g.setTransform(defaultTransform__0_0_0_4);
g.setClip(clip__0_0_0_4);
float alpha__0_0_0_5 = origAlpha;
origAlpha = origAlpha * 0.45f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_5 = g.getClip();
AffineTransform defaultTransform__0_0_0_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_5 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(-18.296388626098633, 36.42112731933594), new Point2D.Double(-21.478368759155273, 35.58143615722656), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 60.0f, 0.0f));
stroke = new BasicStroke(1.0f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(42.5, 36.5);
((GeneralPath)shape).lineTo(39.5, 36.5);
((GeneralPath)shape).curveTo(38.94984, 36.4949, 38.505096, 36.05016, 38.5, 35.5);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_0_5;
g.setTransform(defaultTransform__0_0_0_5);
g.setClip(clip__0_0_0_5);
float alpha__0_0_0_6 = origAlpha;
origAlpha = origAlpha * 0.55f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_6 = g.getClip();
AffineTransform defaultTransform__0_0_0_6 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_6 is ShapeNode
paint = new RadialGradientPaint(new Point2D.Double(-49.9375, 18.890317916870117), 18.5f, new Point2D.Double(-49.9375, 18.890317916870117), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.390496015548706f, 0.0f, 0.0f, 0.7351776957511902f, 79.5003890991211f, 9.456008911132812f));
stroke = new BasicStroke(1.0f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(28.5, 18.5);
((GeneralPath)shape).curveTo(28.4949, 19.05016, 28.05016, 19.4949, 27.5, 19.5);
((GeneralPath)shape).lineTo(21.0, 19.5);
((GeneralPath)shape).curveTo(15.507011, 19.50002, 12.969326, 20.64437, 10.25, 22.4375);
((GeneralPath)shape).curveTo(7.7187557, 24.11998, 6.499997, 26.46458, 6.5, 29.9375);
((GeneralPath)shape).curveTo(6.499997, 33.13864, 7.381488, 35.44567, 9.09375, 37.0625);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_0_6;
g.setTransform(defaultTransform__0_0_0_6);
g.setClip(clip__0_0_0_6);
origAlpha = alpha__0_0_0;
g.setTransform(defaultTransform__0_0_0);
g.setClip(clip__0_0_0);
float alpha__0_0_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1 = g.getClip();
AffineTransform defaultTransform__0_0_1 = g.getTransform();
g.transform(new AffineTransform(0.5450883507728577f, 0.0f, 0.0f, 0.5450883507728577f, 21.932666778564453f, 22.469585418701172f));
// _0_0_1 is CompositeGraphicsNode
float alpha__0_0_1_0 = origAlpha;
origAlpha = origAlpha * 0.30268195f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_0 = g.getClip();
AffineTransform defaultTransform__0_0_1_0 = g.getTransform();
g.transform(new AffineTransform(0.9345970749855042f, 0.0f, 0.0f, 1.1113158464431763f, -5.571971416473389f, -10.707450866699219f));
// _0_0_1_0 is CompositeGraphicsNode
float alpha__0_0_1_0_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_0_0 = g.getClip();
AffineTransform defaultTransform__0_0_1_0_0 = g.getTransform();
g.transform(new AffineTransform(0.0f, -1.0f, 5.888888835906982f, 0.0f, -94.8888931274414f, 53.0f));
// _0_0_1_0_0 is ShapeNode
paint = new RadialGradientPaint(new Point2D.Double(6.5, 23.5), 4.5f, new Point2D.Double(6.5, 23.5), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(2.0, 23.5);
((GeneralPath)shape).curveTo(2.0, 21.014717, 4.014719, 19.0, 6.5, 19.0);
((GeneralPath)shape).curveTo(8.985282, 19.0, 11.0, 21.014719, 11.0, 23.5);
((GeneralPath)shape).lineTo(6.5, 23.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_1_0_0;
g.setTransform(defaultTransform__0_0_1_0_0);
g.setClip(clip__0_0_1_0_0);
float alpha__0_0_1_0_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_0_1 = g.getClip();
AffineTransform defaultTransform__0_0_1_0_1 = g.getTransform();
g.transform(new AffineTransform(0.0f, -1.0f, -3.0f, 0.0f, 114.0f, 53.0f));
// _0_0_1_0_1 is ShapeNode
paint = new RadialGradientPaint(new Point2D.Double(6.5, 23.5), 4.5f, new Point2D.Double(6.5, 23.5), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(2.0, 23.5);
((GeneralPath)shape).curveTo(2.0, 21.014717, 4.014719, 19.0, 6.5, 19.0);
((GeneralPath)shape).curveTo(8.985282, 19.0, 11.0, 21.014719, 11.0, 23.5);
((GeneralPath)shape).lineTo(6.5, 23.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_1_0_1;
g.setTransform(defaultTransform__0_0_1_0_1);
g.setClip(clip__0_0_1_0_1);
origAlpha = alpha__0_0_1_0;
g.setTransform(defaultTransform__0_0_1_0);
g.setClip(clip__0_0_1_0);
float alpha__0_0_1_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_1 = g.getClip();
AffineTransform defaultTransform__0_0_1_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_1 is ShapeNode
paint = new RadialGradientPaint(new Point2D.Double(28.65760040283203, 33.9866943359375), 18.572308f, new Point2D.Double(28.65760040283203, 33.9866943359375), new float[] {0.0f,1.0f}, new Color[] {new Color(231, 206, 121, 255),new Color(193, 125, 17, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(6.042988300323486f, 0.0f, 0.0f, 5.537075996398926f, -142.7943878173828f, -152.23843383789062f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(23.03125, 4.5);
((GeneralPath)shape).curveTo(17.16448, 4.576939, 9.304703, 6.5845976, 9.4375, 13.03125);
((GeneralPath)shape).curveTo(9.579161, 19.90817, 17.932526, 17.194685, 18.25, 20.25);
((GeneralPath)shape).curveTo(18.763103, 25.188007, 8.07627, 40.21102, 24.3125, 43.15625);
((GeneralPath)shape).curveTo(35.15176, 45.122475, 46.655804, 38.37184, 45.5, 24.75);
((GeneralPath)shape).curveTo(44.460564, 12.499581, 34.3249, 4.811841, 24.1875, 4.5);
((GeneralPath)shape).curveTo(23.81714, 4.488607, 23.422367, 4.49487, 23.03125, 4.5);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(20.5, 8.5);
((GeneralPath)shape).curveTo(23.26, 8.5, 25.5, 10.068, 25.5, 12.0);
((GeneralPath)shape).curveTo(25.500002, 13.932, 23.26, 15.5, 20.5, 15.5);
((GeneralPath)shape).curveTo(17.74, 15.5, 15.5, 13.932, 15.5, 12.0);
((GeneralPath)shape).curveTo(15.5, 10.068, 17.74, 8.5, 20.5, 8.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(193, 125, 17, 255);
stroke = new BasicStroke(0.9999999f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(23.03125, 4.5);
((GeneralPath)shape).curveTo(17.16448, 4.576939, 9.304703, 6.5845976, 9.4375, 13.03125);
((GeneralPath)shape).curveTo(9.579161, 19.90817, 17.932526, 17.194685, 18.25, 20.25);
((GeneralPath)shape).curveTo(18.763103, 25.188007, 8.07627, 40.21102, 24.3125, 43.15625);
((GeneralPath)shape).curveTo(35.15176, 45.122475, 46.655804, 38.37184, 45.5, 24.75);
((GeneralPath)shape).curveTo(44.460564, 12.499581, 34.3249, 4.811841, 24.1875, 4.5);
((GeneralPath)shape).curveTo(23.81714, 4.488607, 23.422367, 4.49487, 23.03125, 4.5);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(20.5, 8.5);
((GeneralPath)shape).curveTo(23.26, 8.5, 25.5, 10.068, 25.5, 12.0);
((GeneralPath)shape).curveTo(25.500002, 13.932, 23.26, 15.5, 20.5, 15.5);
((GeneralPath)shape).curveTo(17.74, 15.5, 15.5, 13.932, 15.5, 12.0);
((GeneralPath)shape).curveTo(15.5, 10.068, 17.74, 8.5, 20.5, 8.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_1_1;
g.setTransform(defaultTransform__0_0_1_1);
g.setClip(clip__0_0_1_1);
float alpha__0_0_1_2 = origAlpha;
origAlpha = origAlpha * 0.6704981f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_2 = g.getClip();
AffineTransform defaultTransform__0_0_1_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_2 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(15.299922943115234, 9.144098281860352), new Point2D.Double(51.56820297241211, 83.0760726928711), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 133)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 1.0640332698822021f, 0.024633700028061867f));
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(23.095284, 5.524634);
((GeneralPath)shape).curveTo(20.271, 5.5616727, 16.972109, 6.09223, 14.5015335, 7.305884);
((GeneralPath)shape).curveTo(12.030958, 8.519538, 10.443617, 10.213046, 10.5015335, 13.024634);
((GeneralPath)shape).curveTo(10.532714, 14.538465, 10.981199, 15.35297, 11.5952835, 15.930884);
((GeneralPath)shape).curveTo(12.209368, 16.508799, 13.090957, 16.854794, 14.0952835, 17.118385);
((GeneralPath)shape).curveTo(15.09961, 17.381977, 16.186838, 17.537655, 17.157784, 17.868385);
((GeneralPath)shape).curveTo(17.643257, 18.03375, 18.118864, 18.248297, 18.532784, 18.618385);
((GeneralPath)shape).curveTo(18.946703, 18.988474, 19.250063, 19.565245, 19.314034, 20.180885);
((GeneralPath)shape).curveTo(19.484936, 21.825615, 18.835106, 23.684237, 18.157784, 25.837135);
((GeneralPath)shape).curveTo(17.480461, 27.990034, 16.711693, 30.35075, 16.439034, 32.587135);
((GeneralPath)shape).curveTo(16.166374, 34.82352, 16.397823, 36.867165, 17.501534, 38.493385);
((GeneralPath)shape).curveTo(18.605246, 40.119606, 20.672241, 41.474922, 24.564034, 42.180885);
((GeneralPath)shape).curveTo(29.722582, 43.11664, 35.012897, 41.973743, 38.845284, 39.024635);
((GeneralPath)shape).curveTo(42.67767, 36.075527, 45.116917, 31.384464, 44.564034, 24.868385);
((GeneralPath)shape).curveTo(43.568283, 13.13285, 33.8692, 5.8214483, 24.220284, 5.524634);
((GeneralPath)shape).curveTo(23.883291, 5.514268, 23.488268, 5.519484, 23.095284, 5.524634);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_1_2;
g.setTransform(defaultTransform__0_0_1_2);
g.setClip(clip__0_0_1_2);
float alpha__0_0_1_3 = origAlpha;
origAlpha = origAlpha * 0.7356322f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_3 = g.getClip();
AffineTransform defaultTransform__0_0_1_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_3 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(21.424922943115234, 14.769098281860352), new Point2D.Double(16.63990020751953, 5.5), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 88)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 1.0640332698822021f, 0.024633700028061867f));
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(20.564034, 7.524634);
((GeneralPath)shape).curveTo(22.13501, 7.524634, 23.58675, 7.940535, 24.689034, 8.712133);
((GeneralPath)shape).curveTo(25.791317, 9.483732, 26.564034, 10.668886, 26.564034, 12.024633);
((GeneralPath)shape).curveTo(26.564035, 13.380382, 25.791317, 14.565535, 24.689034, 15.337133);
((GeneralPath)shape).curveTo(23.58675, 16.108732, 22.13501, 16.524633, 20.564034, 16.524633);
((GeneralPath)shape).curveTo(18.993057, 16.524633, 17.541317, 16.108732, 16.439034, 15.337133);
((GeneralPath)shape).curveTo(15.33675, 14.565535, 14.5640335, 13.380381, 14.5640335, 12.024633);
((GeneralPath)shape).curveTo(14.5640335, 10.668886, 15.336749, 9.483732, 16.439034, 8.712133);
((GeneralPath)shape).curveTo(17.541317, 7.9405346, 18.993057, 7.5246334, 20.564034, 7.5246334);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_1_3;
g.setTransform(defaultTransform__0_0_1_3);
g.setClip(clip__0_0_1_3);
float alpha__0_0_1_4 = origAlpha;
origAlpha = origAlpha * 0.13026817f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_4 = g.getClip();
AffineTransform defaultTransform__0_0_1_4 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 1.220970869064331f, 0.4254758059978485f));
// _0_0_1_4 is CompositeGraphicsNode
float alpha__0_0_1_4_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_4_0 = g.getClip();
AffineTransform defaultTransform__0_0_1_4_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_4_0 is ShapeNode
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(26.824406, 37.4965);
((GeneralPath)shape).curveTo(25.843367, 38.089333, 26.895935, 38.770348, 27.369354, 39.16708);
((GeneralPath)shape).curveTo(27.744822, 39.481724, 27.180958, 41.00241, 26.50854, 40.330387);
((GeneralPath)shape).curveTo(25.73363, 39.555927, 25.2347, 38.4785, 24.930767, 38.450886);
((GeneralPath)shape).curveTo(23.183844, 38.292164, 24.528898, 40.598656, 23.123135, 40.09139);
((GeneralPath)shape).curveTo(22.281927, 39.787838, 23.328562, 38.51355, 22.205242, 38.122463);
((GeneralPath)shape).curveTo(21.312836, 37.811768, 21.401499, 38.76652, 20.885483, 38.301308);
((GeneralPath)shape).curveTo(18.456207, 36.1112, 19.733095, 34.20872, 20.857332, 32.722958);
((GeneralPath)shape).curveTo(21.981585, 31.237171, 24.202526, 31.07784, 25.84944, 32.39534);
((GeneralPath)shape).curveTo(27.496351, 33.712837, 27.933138, 35.998158, 26.82441, 37.4965);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(0.9999998f,0,1,5.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(26.824406, 37.4965);
((GeneralPath)shape).curveTo(25.843367, 38.089333, 26.895935, 38.770348, 27.369354, 39.16708);
((GeneralPath)shape).curveTo(27.744822, 39.481724, 27.180958, 41.00241, 26.50854, 40.330387);
((GeneralPath)shape).curveTo(25.73363, 39.555927, 25.2347, 38.4785, 24.930767, 38.450886);
((GeneralPath)shape).curveTo(23.183844, 38.292164, 24.528898, 40.598656, 23.123135, 40.09139);
((GeneralPath)shape).curveTo(22.281927, 39.787838, 23.328562, 38.51355, 22.205242, 38.122463);
((GeneralPath)shape).curveTo(21.312836, 37.811768, 21.401499, 38.76652, 20.885483, 38.301308);
((GeneralPath)shape).curveTo(18.456207, 36.1112, 19.733095, 34.20872, 20.857332, 32.722958);
((GeneralPath)shape).curveTo(21.981585, 31.237171, 24.202526, 31.07784, 25.84944, 32.39534);
((GeneralPath)shape).curveTo(27.496351, 33.712837, 27.933138, 35.998158, 26.82441, 37.4965);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_1_4_0;
g.setTransform(defaultTransform__0_0_1_4_0);
g.setClip(clip__0_0_1_4_0);
float alpha__0_0_1_4_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_4_1 = g.getClip();
AffineTransform defaultTransform__0_0_1_4_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_4_1 is ShapeNode
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(35.474537, 34.48353);
((GeneralPath)shape).curveTo(36.369083, 34.48353, 37.870365, 34.634525, 38.265453, 35.099167);
((GeneralPath)shape).curveTo(39.18928, 36.185635, 40.008957, 34.75653, 39.306164, 34.139572);
((GeneralPath)shape).curveTo(38.496246, 33.428577, 37.419785, 33.019367, 37.366905, 32.717335);
((GeneralPath)shape).curveTo(37.06298, 30.98134, 39.391457, 32.134716, 38.782948, 30.769594);
((GeneralPath)shape).curveTo(38.418816, 29.952703, 37.287834, 31.107681, 36.815258, 30.01564);
((GeneralPath)shape).curveTo(36.43983, 29.148083, 37.363026, 29.15676, 36.87212, 28.67907);
((GeneralPath)shape).curveTo(34.56107, 26.430225, 32.847725, 27.868477, 31.520702, 29.118906);
((GeneralPath)shape).curveTo(30.193659, 30.36935, 30.233784, 32.606693, 31.64015, 34.145317);
((GeneralPath)shape).curveTo(32.217735, 34.77722, 32.501553, 36.296154, 33.238167, 36.47882);
((GeneralPath)shape).curveTo(34.295135, 36.740925, 33.22196, 34.48353, 35.474537, 34.48353);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(1.0f,0,1,5.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(35.474537, 34.48353);
((GeneralPath)shape).curveTo(36.369083, 34.48353, 37.870365, 34.634525, 38.265453, 35.099167);
((GeneralPath)shape).curveTo(39.18928, 36.185635, 40.008957, 34.75653, 39.306164, 34.139572);
((GeneralPath)shape).curveTo(38.496246, 33.428577, 37.419785, 33.019367, 37.366905, 32.717335);
((GeneralPath)shape).curveTo(37.06298, 30.98134, 39.391457, 32.134716, 38.782948, 30.769594);
((GeneralPath)shape).curveTo(38.418816, 29.952703, 37.287834, 31.107681, 36.815258, 30.01564);
((GeneralPath)shape).curveTo(36.43983, 29.148083, 37.363026, 29.15676, 36.87212, 28.67907);
((GeneralPath)shape).curveTo(34.56107, 26.430225, 32.847725, 27.868477, 31.520702, 29.118906);
((GeneralPath)shape).curveTo(30.193659, 30.36935, 30.233784, 32.606693, 31.64015, 34.145317);
((GeneralPath)shape).curveTo(32.217735, 34.77722, 32.501553, 36.296154, 33.238167, 36.47882);
((GeneralPath)shape).curveTo(34.295135, 36.740925, 33.22196, 34.48353, 35.474537, 34.48353);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_1_4_1;
g.setTransform(defaultTransform__0_0_1_4_1);
g.setClip(clip__0_0_1_4_1);
float alpha__0_0_1_4_2 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_4_2 = g.getClip();
AffineTransform defaultTransform__0_0_1_4_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_4_2 is ShapeNode
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(37.02061, 23.50048);
((GeneralPath)shape).curveTo(36.139675, 23.50048, 35.963245, 24.10843, 35.548122, 24.500086);
((GeneralPath)shape).curveTo(35.21889, 24.810703, 33.831154, 24.195639, 34.52325, 23.64745);
((GeneralPath)shape).curveTo(35.320843, 23.015705, 35.496048, 22.269438, 35.548122, 22.00107);
((GeneralPath)shape).curveTo(35.84742, 20.458572, 32.89206, 21.208471, 33.5, 20.0);
((GeneralPath)shape).curveTo(33.94268, 19.120033, 35.552494, 19.95787, 35.54812, 19.502056);
((GeneralPath)shape).curveTo(35.54142, 18.799225, 35.128017, 17.737432, 36.008762, 17.56379);
((GeneralPath)shape).curveTo(37.86193, 17.198433, 39.99518, 18.475962, 41.0, 19.5);
((GeneralPath)shape).curveTo(41.96073, 20.479103, 41.427845, 22.19124, 40.859806, 23.407469);
((GeneralPath)shape).curveTo(40.51274, 24.15057, 39.717464, 25.662941, 38.795277, 25.480877);
((GeneralPath)shape).curveTo(37.748856, 25.274286, 39.238914, 23.500483, 37.020607, 23.500483);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(1.0f,0,1,5.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(37.02061, 23.50048);
((GeneralPath)shape).curveTo(36.139675, 23.50048, 35.963245, 24.10843, 35.548122, 24.500086);
((GeneralPath)shape).curveTo(35.21889, 24.810703, 33.831154, 24.195639, 34.52325, 23.64745);
((GeneralPath)shape).curveTo(35.320843, 23.015705, 35.496048, 22.269438, 35.548122, 22.00107);
((GeneralPath)shape).curveTo(35.84742, 20.458572, 32.89206, 21.208471, 33.5, 20.0);
((GeneralPath)shape).curveTo(33.94268, 19.120033, 35.552494, 19.95787, 35.54812, 19.502056);
((GeneralPath)shape).curveTo(35.54142, 18.799225, 35.128017, 17.737432, 36.008762, 17.56379);
((GeneralPath)shape).curveTo(37.86193, 17.198433, 39.99518, 18.475962, 41.0, 19.5);
((GeneralPath)shape).curveTo(41.96073, 20.479103, 41.427845, 22.19124, 40.859806, 23.407469);
((GeneralPath)shape).curveTo(40.51274, 24.15057, 39.717464, 25.662941, 38.795277, 25.480877);
((GeneralPath)shape).curveTo(37.748856, 25.274286, 39.238914, 23.500483, 37.020607, 23.500483);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_1_4_2;
g.setTransform(defaultTransform__0_0_1_4_2);
g.setClip(clip__0_0_1_4_2);
origAlpha = alpha__0_0_1_4;
g.setTransform(defaultTransform__0_0_1_4);
g.setClip(clip__0_0_1_4);
float alpha__0_0_1_5 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_5 = g.getClip();
AffineTransform defaultTransform__0_0_1_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_5 is ShapeNode
paint = new RadialGradientPaint(new Point2D.Double(23.537704467773438, 36.02513122558594), 4.244812f, new Point2D.Double(23.537704467773438, 36.02513122558594), new float[] {0.0f,1.0f}, new Color[] {new Color(138, 226, 52, 255),new Color(115, 210, 22, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.9409276843070984f, 0.0f, 0.0f, 1.0482488870620728f, 1.6820708513259888f, -3.177501916885376f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(27.824406, 37.4965);
((GeneralPath)shape).curveTo(26.843367, 38.089333, 27.895935, 38.770348, 28.369354, 39.16708);
((GeneralPath)shape).curveTo(28.744822, 39.481724, 28.180958, 41.00241, 27.50854, 40.330387);
((GeneralPath)shape).curveTo(26.73363, 39.555927, 26.2347, 38.4785, 25.930767, 38.450886);
((GeneralPath)shape).curveTo(24.183844, 38.292164, 25.528898, 40.598656, 24.123135, 40.09139);
((GeneralPath)shape).curveTo(23.281927, 39.787838, 24.328562, 38.51355, 23.205242, 38.122463);
((GeneralPath)shape).curveTo(22.312836, 37.811768, 22.401499, 38.76652, 21.885483, 38.301308);
((GeneralPath)shape).curveTo(19.456207, 36.1112, 20.733095, 34.20872, 21.857332, 32.722958);
((GeneralPath)shape).curveTo(22.981585, 31.237171, 25.202526, 31.07784, 26.84944, 32.39534);
((GeneralPath)shape).curveTo(28.496351, 33.712837, 28.933138, 35.998158, 27.82441, 37.4965);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(78, 154, 6, 255);
stroke = new BasicStroke(0.9999998f,0,1,5.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(27.824406, 37.4965);
((GeneralPath)shape).curveTo(26.843367, 38.089333, 27.895935, 38.770348, 28.369354, 39.16708);
((GeneralPath)shape).curveTo(28.744822, 39.481724, 28.180958, 41.00241, 27.50854, 40.330387);
((GeneralPath)shape).curveTo(26.73363, 39.555927, 26.2347, 38.4785, 25.930767, 38.450886);
((GeneralPath)shape).curveTo(24.183844, 38.292164, 25.528898, 40.598656, 24.123135, 40.09139);
((GeneralPath)shape).curveTo(23.281927, 39.787838, 24.328562, 38.51355, 23.205242, 38.122463);
((GeneralPath)shape).curveTo(22.312836, 37.811768, 22.401499, 38.76652, 21.885483, 38.301308);
((GeneralPath)shape).curveTo(19.456207, 36.1112, 20.733095, 34.20872, 21.857332, 32.722958);
((GeneralPath)shape).curveTo(22.981585, 31.237171, 25.202526, 31.07784, 26.84944, 32.39534);
((GeneralPath)shape).curveTo(28.496351, 33.712837, 28.933138, 35.998158, 27.82441, 37.4965);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_1_5;
g.setTransform(defaultTransform__0_0_1_5);
g.setClip(clip__0_0_1_5);
float alpha__0_0_1_6 = origAlpha;
origAlpha = origAlpha * 0.77011496f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_6 = g.getClip();
AffineTransform defaultTransform__0_0_1_6 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_6 is ShapeNode
paint = new Color(238, 238, 236, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(25.71875, 32.75);
((GeneralPath)shape).curveTo(24.385471, 31.971878, 22.878695, 32.79794, 22.34375, 33.46875);
((GeneralPath)shape).curveTo(21.808805, 34.13956, 21.082611, 35.45121, 21.65625, 36.6875);
((GeneralPath)shape).curveTo(22.033056, 35.49819, 22.574726, 34.707554, 23.125, 34.09375);
((GeneralPath)shape).curveTo(23.676092, 33.47903, 24.318087, 32.81714, 25.71875, 32.75);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_1_6;
g.setTransform(defaultTransform__0_0_1_6);
g.setClip(clip__0_0_1_6);
float alpha__0_0_1_7 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_7 = g.getClip();
AffineTransform defaultTransform__0_0_1_7 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_7 is ShapeNode
paint = new RadialGradientPaint(new Point2D.Double(32.450172424316406, 33.60939407348633), 5.0383153f, new Point2D.Double(32.450172424316406, 33.60939407348633), new float[] {0.0f,1.0f}, new Color[] {new Color(239, 41, 41, 255),new Color(204, 0, 0, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.6369420289993286f, 0.696841299533844f, 0.5532571077346802f, -0.5101348161697388f, -5.00120210647583f, 25.28829002380371f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(36.474537, 34.48353);
((GeneralPath)shape).curveTo(37.369083, 34.48353, 38.870365, 34.634525, 39.265453, 35.099167);
((GeneralPath)shape).curveTo(40.18928, 36.185635, 41.008957, 34.75653, 40.306164, 34.139572);
((GeneralPath)shape).curveTo(39.496246, 33.428577, 38.419785, 33.019367, 38.366905, 32.717335);
((GeneralPath)shape).curveTo(38.06298, 30.98134, 40.391457, 32.134716, 39.782948, 30.769594);
((GeneralPath)shape).curveTo(39.418816, 29.952703, 38.287834, 31.107681, 37.815258, 30.01564);
((GeneralPath)shape).curveTo(37.43983, 29.148083, 38.363026, 29.15676, 37.87212, 28.67907);
((GeneralPath)shape).curveTo(35.56107, 26.430225, 33.847725, 27.868477, 32.520702, 29.118906);
((GeneralPath)shape).curveTo(31.193659, 30.36935, 31.233784, 32.606693, 32.640152, 34.145317);
((GeneralPath)shape).curveTo(33.21774, 34.77722, 33.501556, 36.296154, 34.238167, 36.47882);
((GeneralPath)shape).curveTo(35.295135, 36.740925, 34.22196, 34.48353, 36.474537, 34.48353);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(164, 0, 0, 255);
stroke = new BasicStroke(1.0f,0,1,5.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(36.474537, 34.48353);
((GeneralPath)shape).curveTo(37.369083, 34.48353, 38.870365, 34.634525, 39.265453, 35.099167);
((GeneralPath)shape).curveTo(40.18928, 36.185635, 41.008957, 34.75653, 40.306164, 34.139572);
((GeneralPath)shape).curveTo(39.496246, 33.428577, 38.419785, 33.019367, 38.366905, 32.717335);
((GeneralPath)shape).curveTo(38.06298, 30.98134, 40.391457, 32.134716, 39.782948, 30.769594);
((GeneralPath)shape).curveTo(39.418816, 29.952703, 38.287834, 31.107681, 37.815258, 30.01564);
((GeneralPath)shape).curveTo(37.43983, 29.148083, 38.363026, 29.15676, 37.87212, 28.67907);
((GeneralPath)shape).curveTo(35.56107, 26.430225, 33.847725, 27.868477, 32.520702, 29.118906);
((GeneralPath)shape).curveTo(31.193659, 30.36935, 31.233784, 32.606693, 32.640152, 34.145317);
((GeneralPath)shape).curveTo(33.21774, 34.77722, 33.501556, 36.296154, 34.238167, 36.47882);
((GeneralPath)shape).curveTo(35.295135, 36.740925, 34.22196, 34.48353, 36.474537, 34.48353);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_1_7;
g.setTransform(defaultTransform__0_0_1_7);
g.setClip(clip__0_0_1_7);
float alpha__0_0_1_8 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_8 = g.getClip();
AffineTransform defaultTransform__0_0_1_8 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_8 is ShapeNode
paint = new RadialGradientPaint(new Point2D.Double(33.29081344604492, 36.0700569152832), 5.0383153f, new Point2D.Double(33.29081344604492, 36.0700569152832), new float[] {0.0f,1.0f}, new Color[] {new Color(114, 159, 207, 255),new Color(52, 101, 164, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(-0.8570756912231445f, 0.7036892175674438f, -0.6305521726608276f, -0.6305521726608276f, 89.17596435546875f, 19.040700912475586f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(38.02061, 23.50048);
((GeneralPath)shape).curveTo(37.139675, 23.50048, 36.963245, 24.10843, 36.548122, 24.500086);
((GeneralPath)shape).curveTo(36.21889, 24.810703, 34.831154, 24.195639, 35.52325, 23.64745);
((GeneralPath)shape).curveTo(36.320843, 23.015705, 36.496048, 22.269438, 36.548122, 22.00107);
((GeneralPath)shape).curveTo(36.84742, 20.458572, 33.89206, 21.208471, 34.5, 20.0);
((GeneralPath)shape).curveTo(34.94268, 19.120033, 36.552494, 19.95787, 36.54812, 19.502056);
((GeneralPath)shape).curveTo(36.54142, 18.799225, 36.128017, 17.737432, 37.008762, 17.56379);
((GeneralPath)shape).curveTo(38.86193, 17.198433, 40.99518, 18.475962, 42.0, 19.5);
((GeneralPath)shape).curveTo(42.96073, 20.479103, 42.427845, 22.19124, 41.859806, 23.407469);
((GeneralPath)shape).curveTo(41.51274, 24.15057, 40.717464, 25.662941, 39.795277, 25.480877);
((GeneralPath)shape).curveTo(38.748856, 25.274286, 40.238914, 23.500483, 38.020607, 23.500483);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(32, 74, 135, 255);
stroke = new BasicStroke(1.0f,0,1,5.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(38.02061, 23.50048);
((GeneralPath)shape).curveTo(37.139675, 23.50048, 36.963245, 24.10843, 36.548122, 24.500086);
((GeneralPath)shape).curveTo(36.21889, 24.810703, 34.831154, 24.195639, 35.52325, 23.64745);
((GeneralPath)shape).curveTo(36.320843, 23.015705, 36.496048, 22.269438, 36.548122, 22.00107);
((GeneralPath)shape).curveTo(36.84742, 20.458572, 33.89206, 21.208471, 34.5, 20.0);
((GeneralPath)shape).curveTo(34.94268, 19.120033, 36.552494, 19.95787, 36.54812, 19.502056);
((GeneralPath)shape).curveTo(36.54142, 18.799225, 36.128017, 17.737432, 37.008762, 17.56379);
((GeneralPath)shape).curveTo(38.86193, 17.198433, 40.99518, 18.475962, 42.0, 19.5);
((GeneralPath)shape).curveTo(42.96073, 20.479103, 42.427845, 22.19124, 41.859806, 23.407469);
((GeneralPath)shape).curveTo(41.51274, 24.15057, 40.717464, 25.662941, 39.795277, 25.480877);
((GeneralPath)shape).curveTo(38.748856, 25.274286, 40.238914, 23.500483, 38.020607, 23.500483);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_1_8;
g.setTransform(defaultTransform__0_0_1_8);
g.setClip(clip__0_0_1_8);
float alpha__0_0_1_9 = origAlpha;
origAlpha = origAlpha * 0.77011496f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_9 = g.getClip();
AffineTransform defaultTransform__0_0_1_9 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_9 is ShapeNode
paint = new Color(238, 238, 236, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(38.0, 18.5);
((GeneralPath)shape).curveTo(37.144714, 18.887354, 37.0, 20.0, 37.5, 21.0);
((GeneralPath)shape).curveTo(38.033146, 19.553299, 39.5, 20.0, 41.0, 20.0);
((GeneralPath)shape).curveTo(40.5, 19.0, 38.596325, 18.30053, 38.0, 18.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_1_9;
g.setTransform(defaultTransform__0_0_1_9);
g.setClip(clip__0_0_1_9);
float alpha__0_0_1_10 = origAlpha;
origAlpha = origAlpha * 0.77011496f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_10 = g.getClip();
AffineTransform defaultTransform__0_0_1_10 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_10 is ShapeNode
paint = new Color(238, 238, 236, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(36.768036, 28.732128);
((GeneralPath)shape).curveTo(35.434757, 27.954006, 33.927982, 28.780067, 33.393036, 29.450878);
((GeneralPath)shape).curveTo(32.85809, 30.12169, 32.131897, 31.433338, 32.705536, 32.66963);
((GeneralPath)shape).curveTo(33.08234, 31.48032, 33.624012, 30.689684, 34.174286, 30.075878);
((GeneralPath)shape).curveTo(34.725376, 29.46116, 35.367374, 28.799267, 36.768036, 28.732128);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_1_10;
g.setTransform(defaultTransform__0_0_1_10);
g.setClip(clip__0_0_1_10);
origAlpha = alpha__0_0_1;
g.setTransform(defaultTransform__0_0_1);
g.setClip(clip__0_0_1);
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
        return 1;
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
	public FontColourIcon() {
        this.width = getOrigWidth();
        this.height = getOrigHeight();
	}
	
	/**
	 * Creates a new transcoded SVG image with the given dimensions.
	 *
	 * @param size the dimensions of the icon
	 */
	public FontColourIcon(Dimension size) {
	this.width = size.width;
	this.height = size.width;
	}

	public FontColourIcon(int width, int height) {
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

