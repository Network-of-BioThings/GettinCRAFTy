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
public class BackgroundColorIcon implements
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
g.transform(new AffineTransform(1.6164523363113403f, 0.0f, 0.0f, 1.6164523363113403f, -2.138885736465454f, -21.565214157104492f));
// _0_0 is CompositeGraphicsNode
float alpha__0_0_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0 = g.getClip();
AffineTransform defaultTransform__0_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 3.469669818878174f, -10.369165420532227f));
// _0_0_0 is CompositeGraphicsNode
float alpha__0_0_0_0 = origAlpha;
origAlpha = origAlpha * 0.3f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_0 = g.getTransform();
g.transform(new AffineTransform(0.41036099195480347f, 0.0f, 0.0f, 0.6798580288887024f, -0.07301999628543854f, 15.97854995727539f));
// _0_0_0_0 is ShapeNode
paint = new RadialGradientPaint(new Point2D.Double(23.9375, 42.6875), 23.75956f, new Point2D.Double(23.9375, 42.6875), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.24763000011444092f, 0.0f, 32.116798400878906f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(47.69706, 42.6875);
((GeneralPath)shape).curveTo(47.69706, 45.936913, 37.05954, 48.57108, 23.9375, 48.57108);
((GeneralPath)shape).curveTo(10.815458, 48.57108, 0.17794037, 45.936913, 0.17794037, 42.6875);
((GeneralPath)shape).curveTo(0.17794037, 39.438087, 10.815458, 36.80392, 23.9375, 36.80392);
((GeneralPath)shape).curveTo(37.05954, 36.80392, 47.69706, 39.438087, 47.69706, 42.6875);
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
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(0.9999998f,1,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(17.5, 34.5);
((GeneralPath)shape).lineTo(14.5, 31.5);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_0_1;
g.setTransform(defaultTransform__0_0_0_1);
g.setClip(clip__0_0_0_1);
float alpha__0_0_0_2 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_2 = g.getClip();
AffineTransform defaultTransform__0_0_0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_2 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(47.998985290527344, 47.27030944824219), new Point2D.Double(63.938480377197266, 47.27030944824219), new float[] {0.0f,0.3493976f,1.0f}, new Color[] {new Color(186, 189, 182, 255),new Color(238, 238, 236, 255),new Color(136, 138, 133, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0041179656982422f, 0.0f, 0.0f, 1.0234580039978027f, -47.19974136352539f, -10.879670143127441f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(8.999472, 27.50001);
((GeneralPath)shape).curveTo(4.8597455, 27.50001, 1.4999676, 28.744392, 1.4999676, 30.277649);
((GeneralPath)shape).lineTo(1.4999676, 43.721375);
((GeneralPath)shape).curveTo(1.4999676, 45.25463, 4.8597455, 46.499016, 8.999472, 46.499016);
((GeneralPath)shape).curveTo(13.139198, 46.499016, 16.49898, 45.25463, 16.49898, 43.721375);
((GeneralPath)shape).lineTo(16.49898, 30.277649);
((GeneralPath)shape).curveTo(16.49898, 28.744392, 13.139198, 27.50001, 8.999472, 27.50001);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new LinearGradientPaint(new Point2D.Double(5.3125, 26.99901008605957), new Point2D.Double(5.625, 39.0), new float[] {0.0f,1.0f}, new Color[] {new Color(46, 52, 54, 255),new Color(136, 138, 133, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.0019997f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(8.999472, 27.50001);
((GeneralPath)shape).curveTo(4.8597455, 27.50001, 1.4999676, 28.744392, 1.4999676, 30.277649);
((GeneralPath)shape).lineTo(1.4999676, 43.721375);
((GeneralPath)shape).curveTo(1.4999676, 45.25463, 4.8597455, 46.499016, 8.999472, 46.499016);
((GeneralPath)shape).curveTo(13.139198, 46.499016, 16.49898, 45.25463, 16.49898, 43.721375);
((GeneralPath)shape).lineTo(16.49898, 30.277649);
((GeneralPath)shape).curveTo(16.49898, 28.744392, 13.139198, 27.50001, 8.999472, 27.50001);
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
paint = new LinearGradientPaint(new Point2D.Double(47.52022171020508, 53.98938751220703), new Point2D.Double(51.531280517578125, 40.39101791381836), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -47.0f, -10.0f));
stroke = new BasicStroke(1.0019997f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(9.0, 28.5);
((GeneralPath)shape).curveTo(7.031106, 28.5, 5.264027, 28.804224, 4.0625, 29.25);
((GeneralPath)shape).curveTo(3.461736, 29.472889, 2.993416, 29.755526, 2.75, 29.96875);
((GeneralPath)shape).curveTo(2.506584, 30.181974, 2.5, 30.264948, 2.5, 30.28125);
((GeneralPath)shape).lineTo(2.5, 43.71875);
((GeneralPath)shape).curveTo(2.5, 43.73505, 2.50658, 43.81802, 2.75, 44.03125);
((GeneralPath)shape).curveTo(2.993416, 44.244476, 3.461736, 44.52711, 4.0625, 44.75);
((GeneralPath)shape).curveTo(5.264028, 45.195778, 7.031107, 45.5, 9.0, 45.5);
((GeneralPath)shape).curveTo(10.968893, 45.5, 12.735971, 45.195774, 13.9375, 44.75);
((GeneralPath)shape).curveTo(14.538264, 44.52711, 15.006584, 44.244476, 15.25, 44.03125);
((GeneralPath)shape).curveTo(15.493416, 43.818024, 15.5, 43.73505, 15.5, 43.71875);
((GeneralPath)shape).lineTo(15.5, 30.28125);
((GeneralPath)shape).curveTo(15.5, 30.26495, 15.4934, 30.18198, 15.25, 29.96875);
((GeneralPath)shape).curveTo(15.006584, 29.755526, 14.538264, 29.472889, 13.9375, 29.25);
((GeneralPath)shape).curveTo(12.735973, 28.804224, 10.968894, 28.5, 9.0, 28.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_0_3;
g.setTransform(defaultTransform__0_0_0_3);
g.setClip(clip__0_0_0_3);
float alpha__0_0_0_4 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_4 = g.getClip();
AffineTransform defaultTransform__0_0_0_4 = g.getTransform();
g.transform(new AffineTransform(0.9285699725151062f, 0.0f, 0.0f, 0.7996000051498413f, -56.46419143676758f, 5.712399959564209f));
// _0_0_0_4 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(62.9604606628418, 31.0), new Point2D.Double(76.70162200927734, 31.0), new float[] {0.0f,0.6626506f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(86, 88, 85, 255),new Color(46, 52, 54, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(77.5, 31.0);
((GeneralPath)shape).curveTo(77.5, 32.38071, 74.36599, 33.5, 70.5, 33.5);
((GeneralPath)shape).curveTo(66.63401, 33.5, 63.5, 32.38071, 63.5, 31.0);
((GeneralPath)shape).curveTo(63.5, 29.619287, 66.63401, 28.5, 70.5, 28.5);
((GeneralPath)shape).curveTo(74.36599, 28.5, 77.5, 29.619287, 77.5, 31.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(1.1628509f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(77.5, 31.0);
((GeneralPath)shape).curveTo(77.5, 32.38071, 74.36599, 33.5, 70.5, 33.5);
((GeneralPath)shape).curveTo(66.63401, 33.5, 63.5, 32.38071, 63.5, 31.0);
((GeneralPath)shape).curveTo(63.5, 29.619287, 66.63401, 28.5, 70.5, 28.5);
((GeneralPath)shape).curveTo(74.36599, 28.5, 77.5, 29.619287, 77.5, 31.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_0_4;
g.setTransform(defaultTransform__0_0_0_4);
g.setClip(clip__0_0_0_4);
float alpha__0_0_0_5 = origAlpha;
origAlpha = origAlpha * 0.1f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_5 = g.getClip();
AffineTransform defaultTransform__0_0_0_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_5 is ShapeNode
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(9.345005, 42.713127);
((GeneralPath)shape).curveTo(9.204923, 33.22204, 5.5778227, 33.856804, 7.334597, 32.244312);
((GeneralPath)shape).curveTo(12.218053, 32.56472, 14.649587, 31.727633, 15.3125, 30.84375);
((GeneralPath)shape).curveTo(17.886276, 34.05394, 10.425369, 32.933487, 11.3263645, 42.68907);
((GeneralPath)shape).curveTo(11.311034, 44.32013, 9.306448, 44.76434, 9.345005, 42.71313);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_5;
g.setTransform(defaultTransform__0_0_0_5);
g.setClip(clip__0_0_0_5);
float alpha__0_0_0_6 = origAlpha;
origAlpha = origAlpha * 0.6f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_6 = g.getClip();
AffineTransform defaultTransform__0_0_0_6 = g.getTransform();
g.transform(new AffineTransform(0.75f, 0.125f, 0.0f, 0.8585929870605469f, 1.75f, 4.182837963104248f));
// _0_0_0_6 is ShapeNode
paint = new Color(136, 138, 133, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(7.0, 35.747044);
((GeneralPath)shape).curveTo(7.0, 36.71191, 6.1045694, 37.494087, 5.0, 37.494087);
((GeneralPath)shape).curveTo(3.8954306, 37.494087, 3.0, 36.71191, 3.0, 35.747044);
((GeneralPath)shape).curveTo(3.0, 34.782177, 3.8954306, 34.0, 5.0, 34.0);
((GeneralPath)shape).curveTo(6.1045694, 34.0, 7.0, 34.782177, 7.0, 35.747044);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_6;
g.setTransform(defaultTransform__0_0_0_6);
g.setClip(clip__0_0_0_6);
float alpha__0_0_0_7 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_7 = g.getClip();
AffineTransform defaultTransform__0_0_0_7 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_7 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(11.269515037536621, 37.85743713378906), new Point2D.Double(10.562406539916992, 32.48784255981445), new float[] {0.0f,1.0f}, new Color[] {new Color(164, 0, 0, 255),new Color(239, 41, 41, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -2.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(9.032505, 42.369377);
((GeneralPath)shape).curveTo(9.283048, 30.94079, 3.624698, 33.71618, 5.709597, 31.728687);
((GeneralPath)shape).curveTo(11.354185, 32.403618, 14.372165, 31.395576, 15.0, 30.5);
((GeneralPath)shape).curveTo(17.573776, 33.71019, 10.112869, 32.589737, 11.0138645, 42.34532);
((GeneralPath)shape).curveTo(10.998534, 43.97638, 8.993948, 44.42059, 9.032505, 42.36938);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_7;
g.setTransform(defaultTransform__0_0_0_7);
g.setClip(clip__0_0_0_7);
float alpha__0_0_0_8 = origAlpha;
origAlpha = origAlpha * 0.3f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8 = g.getClip();
AffineTransform defaultTransform__0_0_0_8 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_8 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(11.296875, 37.5), new Point2D.Double(10.296875, 32.890625), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -2.0f, 0.0f));
stroke = new BasicStroke(0.4f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(7.718751, 33.531254);
((GeneralPath)shape).curveTo(8.311668, 34.52206, 9.518863, 36.40045, 9.593751, 41.734383);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_0_8;
g.setTransform(defaultTransform__0_0_0_8);
g.setClip(clip__0_0_0_8);
float alpha__0_0_0_9 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9 = g.getClip();
AffineTransform defaultTransform__0_0_0_9 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_9 is ShapeNode
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(1.0000001f,1,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(5.5, 35.5);
((GeneralPath)shape).curveTo(5.5, 35.5, 10.361633, 41.44325, 13.611814, 41.498775);
((GeneralPath)shape).curveTo(16.861994, 41.554306, 18.47873, 39.728474, 18.5, 37.422268);
((GeneralPath)shape).curveTo(18.52129, 35.116177, 17.5, 34.500008, 17.5, 34.500008);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_0_9;
g.setTransform(defaultTransform__0_0_0_9);
g.setClip(clip__0_0_0_9);
float alpha__0_0_0_10 = origAlpha;
origAlpha = origAlpha * 0.6f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_10 = g.getClip();
AffineTransform defaultTransform__0_0_0_10 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_10 is ShapeNode
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(0.3f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(6.03865, 32.310688);
((GeneralPath)shape).curveTo(10.705692, 32.89853, 14.322287, 32.21565, 14.955822, 31.212723);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_0_10;
g.setTransform(defaultTransform__0_0_0_10);
g.setClip(clip__0_0_0_10);
float alpha__0_0_0_11 = origAlpha;
origAlpha = origAlpha * 0.3f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_11 = g.getClip();
AffineTransform defaultTransform__0_0_0_11 = g.getTransform();
g.transform(new AffineTransform(0.7735850214958191f, 0.0f, 0.0f, 1.1914889812469482f, 0.5613210201263428f, -8.249003410339355f));
// _0_0_0_11 is ShapeNode
paint = new Color(255, 255, 255, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(12.140624, 42.710938);
((GeneralPath)shape).curveTo(12.140624, 42.91373, 11.955242, 43.078125, 11.7265625, 43.078125);
((GeneralPath)shape).curveTo(11.497882, 43.078125, 11.312499, 42.91373, 11.312499, 42.710938);
((GeneralPath)shape).curveTo(11.312499, 42.508144, 11.497881, 42.34375, 11.7265625, 42.34375);
((GeneralPath)shape).curveTo(11.955242, 42.34375, 12.140624, 42.508144, 12.140624, 42.710938);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_11;
g.setTransform(defaultTransform__0_0_0_11);
g.setClip(clip__0_0_0_11);
origAlpha = alpha__0_0_0;
g.setTransform(defaultTransform__0_0_0);
g.setClip(clip__0_0_0);
origAlpha = alpha__0_0;
g.setTransform(defaultTransform__0_0);
g.setClip(clip__0_0);
float alpha__0_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_1 = g.getClip();
AffineTransform defaultTransform__0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 3.358757257461548f, 20.329320907592773f));
// _0_1 is CompositeGraphicsNode
float alpha__0_1_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_1_0 = g.getClip();
AffineTransform defaultTransform__0_1_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 3.707106828689575f, -22.445436477661133f));
// _0_1_0 is CompositeGraphicsNode
float alpha__0_1_0_0 = origAlpha;
origAlpha = origAlpha * 0.3f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_1_0_0 = g.getClip();
AffineTransform defaultTransform__0_1_0_0 = g.getTransform();
g.transform(new AffineTransform(0.2309119999408722f, 0.0f, 0.0f, 0.4249109923839569f, 23.95890998840332f, 27.361600875854492f));
// _0_1_0_0 is ShapeNode
paint = new RadialGradientPaint(new Point2D.Double(23.9375, 42.6875), 23.75956f, new Point2D.Double(23.9375, 42.6875), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.24763000011444092f, 0.0f, 32.116798400878906f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(47.69706, 42.6875);
((GeneralPath)shape).curveTo(47.69706, 45.936913, 37.05954, 48.57108, 23.9375, 48.57108);
((GeneralPath)shape).curveTo(10.815458, 48.57108, 0.17794037, 45.936913, 0.17794037, 42.6875);
((GeneralPath)shape).curveTo(0.17794037, 39.438087, 10.815458, 36.80392, 23.9375, 36.80392);
((GeneralPath)shape).curveTo(37.05954, 36.80392, 47.69706, 39.438087, 47.69706, 42.6875);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_1_0_0;
g.setTransform(defaultTransform__0_1_0_0);
g.setClip(clip__0_1_0_0);
float alpha__0_1_0_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_1_0_1 = g.getClip();
AffineTransform defaultTransform__0_1_0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(27.65625, 40.4375), new Point2D.Double(32.46925354003906, 40.4375), new float[] {0.0f,1.0f}, new Color[] {new Color(233, 185, 110, 255),new Color(193, 125, 17, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(31.011063, 32.499992);
((GeneralPath)shape).curveTo(31.011063, 32.499992, 32.528572, 40.253124, 32.5, 42.424072);
((GeneralPath)shape).curveTo(32.4719, 44.559433, 31.819656, 46.43749, 29.469381, 46.43749);
((GeneralPath)shape).curveTo(27.15003, 46.43749, 26.531536, 44.704422, 26.50061, 42.424072);
((GeneralPath)shape).curveTo(26.46969, 40.144012, 27.95765, 32.562492, 27.95765, 32.562492);
((GeneralPath)shape).lineTo(31.01106, 32.499992);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new LinearGradientPaint(new Point2D.Double(32.0, 37.25), new Point2D.Double(32.0, 34.2707405090332), new float[] {0.0f,1.0f}, new Color[] {new Color(143, 89, 2, 255),new Color(99, 61, 0, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f));
stroke = new BasicStroke(0.9999998f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(31.011063, 32.499992);
((GeneralPath)shape).curveTo(31.011063, 32.499992, 32.528572, 40.253124, 32.5, 42.424072);
((GeneralPath)shape).curveTo(32.4719, 44.559433, 31.819656, 46.43749, 29.469381, 46.43749);
((GeneralPath)shape).curveTo(27.15003, 46.43749, 26.531536, 44.704422, 26.50061, 42.424072);
((GeneralPath)shape).curveTo(26.46969, 40.144012, 27.95765, 32.562492, 27.95765, 32.562492);
((GeneralPath)shape).lineTo(31.01106, 32.499992);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_1_0_1;
g.setTransform(defaultTransform__0_1_0_1);
g.setClip(clip__0_1_0_1);
float alpha__0_1_0_2 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_1_0_2 = g.getClip();
AffineTransform defaultTransform__0_1_0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_2 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(29.410438537597656, 20.64676856994629), new Point2D.Double(30.096174240112305, 25.90407371520996), new float[] {0.0f,1.0f}, new Color[] {new Color(73, 83, 86, 255),new Color(30, 34, 36, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(22.5, 27.5);
((GeneralPath)shape).curveTo(22.45849, 23.0625, 20.41699, 19.4375, 20.5, 19.5);
((GeneralPath)shape).curveTo(21.994165, 20.8125, 23.986385, 21.0, 24.982494, 23.4375);
((GeneralPath)shape).curveTo(24.982494, 23.5, 26.974714, 22.0, 26.538916, 20.4375);
((GeneralPath)shape).curveTo(27.970825, 21.0, 28.157595, 22.145832, 28.966934, 23.0);
((GeneralPath)shape).lineTo(29.963045, 20.0);
((GeneralPath)shape).lineTo(32.45052, 23.0);
((GeneralPath)shape).lineTo(35.509438, 20.0);
((GeneralPath)shape).curveTo(35.509438, 20.0, 36.437744, 24.9375, 36.5, 27.5);
((GeneralPath)shape).lineTo(22.5, 27.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new LinearGradientPaint(new Point2D.Double(27.510536193847656, 25.36113739013672), new Point2D.Double(28.02859878540039, 20.057836532592773), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(212, 40, 40, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.0000001f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(22.5, 27.5);
((GeneralPath)shape).curveTo(22.45849, 23.0625, 20.41699, 19.4375, 20.5, 19.5);
((GeneralPath)shape).curveTo(21.994165, 20.8125, 23.986385, 21.0, 24.982494, 23.4375);
((GeneralPath)shape).curveTo(24.982494, 23.5, 26.974714, 22.0, 26.538916, 20.4375);
((GeneralPath)shape).curveTo(27.970825, 21.0, 28.157595, 22.145832, 28.966934, 23.0);
((GeneralPath)shape).lineTo(29.963045, 20.0);
((GeneralPath)shape).lineTo(32.45052, 23.0);
((GeneralPath)shape).lineTo(35.509438, 20.0);
((GeneralPath)shape).curveTo(35.509438, 20.0, 36.437744, 24.9375, 36.5, 27.5);
((GeneralPath)shape).lineTo(22.5, 27.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_1_0_2;
g.setTransform(defaultTransform__0_1_0_2);
g.setClip(clip__0_1_0_2);
float alpha__0_1_0_3 = origAlpha;
origAlpha = origAlpha * 0.6f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_1_0_3 = g.getClip();
AffineTransform defaultTransform__0_1_0_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f));
// _0_1_0_3 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(28.5, 44.0), new Point2D.Double(28.0, 30.375), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(0.9999998f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(28.78125, 32.53125);
((GeneralPath)shape).curveTo(28.676458, 33.07113, 28.516066, 34.062725, 28.21875, 35.8125);
((GeneralPath)shape).curveTo(27.8503, 37.980934, 27.487719, 40.531914, 27.5, 41.4375);
((GeneralPath)shape).curveTo(27.51428, 42.490627, 27.68577, 43.2988, 27.96875, 43.75);
((GeneralPath)shape).curveTo(28.25173, 44.2012, 28.556435, 44.4375, 29.46875, 44.4375);
((GeneralPath)shape).curveTo(30.379202, 44.4375, 30.69847, 44.198746, 31.0, 43.71875);
((GeneralPath)shape).curveTo(31.30153, 43.238754, 31.487242, 42.407036, 31.5, 41.4375);
((GeneralPath)shape).curveTo(31.51099, 40.602573, 31.157352, 38.01107, 30.78125, 35.8125);
((GeneralPath)shape).curveTo(30.482124, 34.063904, 30.298449, 33.104626, 30.1875, 32.53125);
((GeneralPath)shape).lineTo(28.78125, 32.53125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_1_0_3;
g.setTransform(defaultTransform__0_1_0_3);
g.setClip(clip__0_1_0_3);
float alpha__0_1_0_4 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_1_0_4 = g.getClip();
AffineTransform defaultTransform__0_1_0_4 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_4 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(28.747844696044922, 28.779314041137695), new Point2D.Double(28.747844696044922, 32.069236755371094), new float[] {0.0f,1.0f}, new Color[] {new Color(193, 125, 17, 255),new Color(233, 185, 110, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(22.504711, 32.99529);
((GeneralPath)shape).lineTo(22.5, 28.5);
((GeneralPath)shape).lineTo(36.5, 28.5);
((GeneralPath)shape).lineTo(36.5047, 32.99529);
((GeneralPath)shape).curveTo(36.50471, 33.99529, 36.0, 34.520832, 35.0, 34.5);
((GeneralPath)shape).lineTo(24.0, 34.5);
((GeneralPath)shape).curveTo(23.0, 34.5, 22.5, 34.0, 22.504711, 32.99529);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(143, 89, 2, 255);
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(22.504711, 32.99529);
((GeneralPath)shape).lineTo(22.5, 28.5);
((GeneralPath)shape).lineTo(36.5, 28.5);
((GeneralPath)shape).lineTo(36.5047, 32.99529);
((GeneralPath)shape).curveTo(36.50471, 33.99529, 36.0, 34.520832, 35.0, 34.5);
((GeneralPath)shape).lineTo(24.0, 34.5);
((GeneralPath)shape).curveTo(23.0, 34.5, 22.5, 34.0, 22.504711, 32.99529);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_1_0_4;
g.setTransform(defaultTransform__0_1_0_4);
g.setClip(clip__0_1_0_4);
float alpha__0_1_0_5 = origAlpha;
origAlpha = origAlpha * 0.3608247f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_1_0_5 = g.getClip();
AffineTransform defaultTransform__0_1_0_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_5 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(29.258052825927734, 33.98051834106445), new Point2D.Double(29.15077781677246, 35.60707092285156), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(-1.0f, 0.0f, 0.0f, 1.0f, 58.984371185302734f, 1.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(27.984375, 35.0);
((GeneralPath)shape).lineTo(30.96875, 34.9922);
((GeneralPath)shape).curveTo(31.21286, 36.31806, 31.160522, 35.96878, 31.363445, 37.31524);
((GeneralPath)shape).curveTo(30.723043, 35.335163, 28.484375, 35.0, 27.984375, 35.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_1_0_5;
g.setTransform(defaultTransform__0_1_0_5);
g.setClip(clip__0_1_0_5);
float alpha__0_1_0_6 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_1_0_6 = g.getClip();
AffineTransform defaultTransform__0_1_0_6 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_6 is ShapeNode
paint = new Color(239, 41, 41, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(21.743534, 20.953165);
((GeneralPath)shape).lineTo(22.804193, 25.107418);
((GeneralPath)shape).lineTo(22.936775, 22.698835);
((GeneralPath)shape).curveTo(23.934671, 22.956944, 24.241106, 24.509592, 24.925512, 25.39468);
((GeneralPath)shape).curveTo(25.679361, 24.879639, 26.703506, 24.284899, 27.400387, 23.42804);
((GeneralPath)shape).lineTo(29.389124, 24.886448);
((GeneralPath)shape).lineTo(30.847532, 22.9861);
((GeneralPath)shape).lineTo(32.968853, 25.019032);
((GeneralPath)shape).lineTo(35.708893, 23.162876);
((GeneralPath)shape).lineTo(35.266953, 21.041555);
((GeneralPath)shape).lineTo(32.438526, 23.781595);
((GeneralPath)shape).lineTo(30.140429, 20.997362);
((GeneralPath)shape).curveTo(29.80531, 22.035887, 29.448578, 23.117634, 29.168156, 24.04676);
((GeneralPath)shape).curveTo(28.40532, 23.250467, 27.937128, 22.15954, 27.046835, 21.32882);
((GeneralPath)shape).curveTo(26.696201, 22.181229, 26.418604, 23.131428, 24.704542, 24.04676);
((GeneralPath)shape).curveTo(24.091263, 22.70779, 23.345703, 21.477758, 21.743534, 20.953169);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_1_0_6;
g.setTransform(defaultTransform__0_1_0_6);
g.setClip(clip__0_1_0_6);
float alpha__0_1_0_7 = origAlpha;
origAlpha = origAlpha * 0.6f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_1_0_7 = g.getClip();
AffineTransform defaultTransform__0_1_0_7 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f));
// _0_1_0_7 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(26.282012939453125, 28.0), new Point2D.Double(26.229612350463867, 34.544891357421875), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(23.5, 28.5);
((GeneralPath)shape).lineTo(23.5, 32.0);
((GeneralPath)shape).curveTo(23.4984, 32.335457, 23.54743, 32.422497, 23.5625, 32.4375);
((GeneralPath)shape).curveTo(23.57757, 32.4525, 23.663807, 32.5, 24.0, 32.5);
((GeneralPath)shape).lineTo(35.0, 32.5);
((GeneralPath)shape).curveTo(35.010414, 32.499836, 35.020836, 32.499836, 35.03125, 32.5);
((GeneralPath)shape).curveTo(35.355663, 32.5068, 35.390083, 32.45384, 35.40625, 32.4375);
((GeneralPath)shape).curveTo(35.422417, 32.421165, 35.5, 32.334637, 35.5, 32.0);
((GeneralPath)shape).lineTo(35.5, 28.5);
((GeneralPath)shape).lineTo(23.5, 28.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_1_0_7;
g.setTransform(defaultTransform__0_1_0_7);
g.setClip(clip__0_1_0_7);
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
paint = new LinearGradientPaint(new Point2D.Double(38.5, 26.718740463256836), new Point2D.Double(26.499988555908203, 23.9999942779541), new float[] {0.0f,1.0f}, new Color[] {new Color(186, 189, 182, 255),new Color(211, 215, 207, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.7071067690849304f, -19.445436477661133f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(40.207108, 6.0804515);
((GeneralPath)shape).lineTo(40.207108, 7.0286756);
((GeneralPath)shape).curveTo(40.207108, 7.5970173, 39.74956, 8.0545635, 39.18122, 8.0545635);
((GeneralPath)shape).lineTo(27.232998, 8.0545635);
((GeneralPath)shape).curveTo(26.664656, 8.0545635, 26.20711, 7.5970173, 26.20711, 7.0286756);
((GeneralPath)shape).lineTo(26.20711, 6.0804515);
((GeneralPath)shape).lineTo(40.207108, 6.0804515);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new LinearGradientPaint(new Point2D.Double(26.084016799926758, 25.42251205444336), new Point2D.Double(26.084016799926758, 28.000019073486328), new float[] {0.0f,1.0f}, new Color[] {new Color(85, 87, 83, 255),new Color(136, 138, 133, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.7071067690849304f, -19.445436477661133f));
stroke = new BasicStroke(0.9999997f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(40.207108, 6.0804515);
((GeneralPath)shape).lineTo(40.207108, 7.0286756);
((GeneralPath)shape).curveTo(40.207108, 7.5970173, 39.74956, 8.0545635, 39.18122, 8.0545635);
((GeneralPath)shape).lineTo(27.232998, 8.0545635);
((GeneralPath)shape).curveTo(26.664656, 8.0545635, 26.20711, 7.5970173, 26.20711, 7.0286756);
((GeneralPath)shape).lineTo(26.20711, 6.0804515);
((GeneralPath)shape).lineTo(40.207108, 6.0804515);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_1_1;
g.setTransform(defaultTransform__0_1_1);
g.setClip(clip__0_1_1);
origAlpha = alpha__0_1;
g.setTransform(defaultTransform__0_1);
g.setClip(clip__0_1);
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
        return 4;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     * 
     * @return The Y of the bounding box of the original SVG image.
     */
    public static int getOrigY() {
        return 6;
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
	public BackgroundColorIcon() {
        this.width = getOrigWidth();
        this.height = getOrigHeight();
	}
	
	/**
	 * Creates a new transcoded SVG image with the given dimensions.
	 *
	 * @param size the dimensions of the icon
	 */
	public BackgroundColorIcon(Dimension size) {
	this.width = size.width;
	this.height = size.width;
	}

	public BackgroundColorIcon(int width, int height) {
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

