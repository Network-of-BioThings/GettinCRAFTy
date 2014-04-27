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
public class CloudyIcon implements
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
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -263.989990234375f, 459.9855041503906f));
// _0_0_0 is CompositeGraphicsNode
float alpha__0_0_0_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_0 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(280.5, -445.5);
((GeneralPath)shape).curveTo(278.22916, -445.5, 276.39008, -443.9497, 275.78125, -441.875);
((GeneralPath)shape).curveTo(275.088, -442.23883, 274.33673, -442.5, 273.5, -442.5);
((GeneralPath)shape).curveTo(270.74, -442.5, 268.5, -440.26, 268.5, -437.5);
((GeneralPath)shape).curveTo(268.5, -436.92108, 268.6625, -436.3923, 268.84375, -435.875);
((GeneralPath)shape).curveTo(267.47028, -435.10425, 266.5, -433.686, 266.5, -432.0);
((GeneralPath)shape).curveTo(266.5, -429.516, 268.516, -427.5, 271.0, -427.5);
((GeneralPath)shape).curveTo(271.17712, -427.5, 289.82288, -427.5, 290.0, -427.5);
((GeneralPath)shape).curveTo(292.48398, -427.5, 294.5, -429.516, 294.5, -432.0);
((GeneralPath)shape).curveTo(294.5, -433.686, 293.52972, -435.10425, 292.15625, -435.875);
((GeneralPath)shape).curveTo(292.3375, -436.3923, 292.5, -436.92108, 292.5, -437.5);
((GeneralPath)shape).curveTo(292.5, -440.26, 290.26, -442.5, 287.5, -442.5);
((GeneralPath)shape).curveTo(286.66327, -442.5, 285.912, -442.23883, 285.21875, -441.875);
((GeneralPath)shape).curveTo(284.60992, -443.9497, 282.77084, -445.5, 280.5, -445.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(136, 138, 133, 255);
stroke = new BasicStroke(1.0f,0,0,2.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(280.5, -445.5);
((GeneralPath)shape).curveTo(278.22916, -445.5, 276.39008, -443.9497, 275.78125, -441.875);
((GeneralPath)shape).curveTo(275.088, -442.23883, 274.33673, -442.5, 273.5, -442.5);
((GeneralPath)shape).curveTo(270.74, -442.5, 268.5, -440.26, 268.5, -437.5);
((GeneralPath)shape).curveTo(268.5, -436.92108, 268.6625, -436.3923, 268.84375, -435.875);
((GeneralPath)shape).curveTo(267.47028, -435.10425, 266.5, -433.686, 266.5, -432.0);
((GeneralPath)shape).curveTo(266.5, -429.516, 268.516, -427.5, 271.0, -427.5);
((GeneralPath)shape).curveTo(271.17712, -427.5, 289.82288, -427.5, 290.0, -427.5);
((GeneralPath)shape).curveTo(292.48398, -427.5, 294.5, -429.516, 294.5, -432.0);
((GeneralPath)shape).curveTo(294.5, -433.686, 293.52972, -435.10425, 292.15625, -435.875);
((GeneralPath)shape).curveTo(292.3375, -436.3923, 292.5, -436.92108, 292.5, -437.5);
((GeneralPath)shape).curveTo(292.5, -440.26, 290.26, -442.5, 287.5, -442.5);
((GeneralPath)shape).curveTo(286.66327, -442.5, 285.912, -442.23883, 285.21875, -441.875);
((GeneralPath)shape).curveTo(284.60992, -443.9497, 282.77084, -445.5, 280.5, -445.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
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
paint = new LinearGradientPaint(new Point2D.Double(271.0216979980469, -441.05181884765625), new Point2D.Double(285.0285949707031, -431.96990966796875), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(280.5, -445.0);
((GeneralPath)shape).curveTo(278.31027, -445.0, 276.7764, -443.6642, 276.10446, -441.1565);
((GeneralPath)shape).curveTo(275.436, -441.5001, 274.55685, -441.98984, 273.75, -441.98984);
((GeneralPath)shape).curveTo(271.03348, -441.98984, 268.99487, -440.051, 268.99487, -437.44427);
((GeneralPath)shape).curveTo(268.99487, -436.89752, 269.2621, -436.11084, 269.43683, -435.62228);
((GeneralPath)shape).curveTo(268.1124, -434.89432, 267.0, -433.73178, 267.0, -432.24973);
((GeneralPath)shape).curveTo(267.0, -429.9037, 268.54617, -427.99963, 271.3393, -427.99963);
((GeneralPath)shape).curveTo(271.5101, -427.99963, 289.48993, -427.99963, 289.6607, -427.99963);
((GeneralPath)shape).curveTo(292.43173, -427.99963, 294.0, -429.9037, 294.0, -432.24973);
((GeneralPath)shape).curveTo(294.0, -433.8421, 292.8876, -434.9164, 291.56317, -435.64438);
((GeneralPath)shape).curveTo(291.7379, -436.13293, 292.02725, -436.89752, 292.02725, -437.44427);
((GeneralPath)shape).curveTo(292.02725, -440.051, 289.91144, -442.01193, 287.25, -442.01193);
((GeneralPath)shape).curveTo(286.44315, -442.01193, 285.6082, -441.5222, 284.93973, -441.17856);
((GeneralPath)shape).curveTo(284.2909, -443.6001, 282.68973, -445.0, 280.5, -445.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_1;
g.setTransform(defaultTransform__0_0_0_1);
g.setClip(clip__0_0_0_1);
float alpha__0_0_0_2 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_2 = g.getClip();
AffineTransform defaultTransform__0_0_0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_2 is CompositeGraphicsNode
float alpha__0_0_0_2_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_2_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_2_0 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -24.198179244995117f, 21.863309860229492f));
// _0_0_0_2_0 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_2_0;
g.setTransform(defaultTransform__0_0_0_2_0);
g.setClip(clip__0_0_0_2_0);
float alpha__0_0_0_2_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_2_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_2_1 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -24.198179244995117f, 21.863309860229492f));
// _0_0_0_2_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(287.5173034667969, -439.7528076171875), new Point2D.Double(289.67633056640625, -436.3219909667969), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_2_1;
g.setTransform(defaultTransform__0_0_0_2_1);
g.setClip(clip__0_0_0_2_1);
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
paint = new Color(196, 197, 194, 255);
shape = new Rectangle2D.Double(271.0, -438.0, 20.0, 9.0);
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_3;
g.setTransform(defaultTransform__0_0_0_3);
g.setClip(clip__0_0_0_3);
float alpha__0_0_0_4 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_4 = g.getClip();
AffineTransform defaultTransform__0_0_0_4 = g.getTransform();
g.transform(new AffineTransform(0.9056599736213684f, 0.0f, 0.0f, 0.9056599736213684f, 9.830195426940918f, -35.688690185546875f));
// _0_0_0_4 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_4;
g.setTransform(defaultTransform__0_0_0_4);
g.setClip(clip__0_0_0_4);
float alpha__0_0_0_5 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_5 = g.getClip();
AffineTransform defaultTransform__0_0_0_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_5 is CompositeGraphicsNode
float alpha__0_0_0_5_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_5_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_5_0 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -17.198110580444336f, 24.863210678100586f));
// _0_0_0_5_0 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_5_0;
g.setTransform(defaultTransform__0_0_0_5_0);
g.setClip(clip__0_0_0_5_0);
float alpha__0_0_0_5_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_5_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_5_1 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -17.198179244995117f, 24.863309860229492f));
// _0_0_0_5_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(286.51171875, -441.2907409667969), new Point2D.Double(289.8537902832031, -436.14453125), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_5_1;
g.setTransform(defaultTransform__0_0_0_5_1);
g.setClip(clip__0_0_0_5_1);
origAlpha = alpha__0_0_0_5;
g.setTransform(defaultTransform__0_0_0_5);
g.setClip(clip__0_0_0_5);
float alpha__0_0_0_6 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_6 = g.getClip();
AffineTransform defaultTransform__0_0_0_6 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_6 is CompositeGraphicsNode
float alpha__0_0_0_6_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_6_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_6_0 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -31.198179244995117f, 24.863309860229492f));
// _0_0_0_6_0 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_6_0;
g.setTransform(defaultTransform__0_0_0_6_0);
g.setClip(clip__0_0_0_6_0);
float alpha__0_0_0_6_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_6_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_6_1 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -31.198179244995117f, 24.863309860229492f));
// _0_0_0_6_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(285.94085693359375, -439.9389953613281), new Point2D.Double(289.3912353515625, -436.4429016113281), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_6_1;
g.setTransform(defaultTransform__0_0_0_6_1);
g.setClip(clip__0_0_0_6_1);
origAlpha = alpha__0_0_0_6;
g.setTransform(defaultTransform__0_0_0_6);
g.setClip(clip__0_0_0_6);
float alpha__0_0_0_7 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_7 = g.getClip();
AffineTransform defaultTransform__0_0_0_7 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 0.0f));
// _0_0_0_7 is CompositeGraphicsNode
float alpha__0_0_0_7_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_7_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_7_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_7_0 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(280.46875, -440.96875);
((GeneralPath)shape).curveTo(276.88937, -440.96875, 274.0, -438.04813, 274.0, -434.46875);
((GeneralPath)shape).curveTo(274.0, -432.09808, 275.34943, -430.13095, 277.25, -429.0);
((GeneralPath)shape).lineTo(283.71875, -429.0);
((GeneralPath)shape).curveTo(285.61932, -430.13095, 286.96875, -432.1293, 286.96875, -434.5);
((GeneralPath)shape).curveTo(286.96875, -438.07938, 284.04813, -440.96875, 280.46875, -440.96875);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_7_0;
g.setTransform(defaultTransform__0_0_0_7_0);
g.setClip(clip__0_0_0_7_0);
float alpha__0_0_0_7_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_7_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_7_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_7_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(275.9419250488281, -437.1050109863281), new Point2D.Double(279.9754638671875, -431.9183349609375), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(280.5, -441.0);
((GeneralPath)shape).curveTo(276.912, -441.0, 274.0, -438.08798, 274.0, -434.5);
((GeneralPath)shape).curveTo(274.0, -432.1236, 275.34485, -430.13367, 277.25, -429.0);
((GeneralPath)shape).lineTo(283.75, -429.0);
((GeneralPath)shape).curveTo(285.65515, -430.13367, 287.0, -432.1236, 287.0, -434.5);
((GeneralPath)shape).curveTo(287.0, -438.088, 284.088, -441.0, 280.5, -441.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_7_1;
g.setTransform(defaultTransform__0_0_0_7_1);
g.setClip(clip__0_0_0_7_1);
origAlpha = alpha__0_0_0_7;
g.setTransform(defaultTransform__0_0_0_7);
g.setClip(clip__0_0_0_7);
float alpha__0_0_0_8 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8 = g.getClip();
AffineTransform defaultTransform__0_0_0_8 = g.getTransform();
g.transform(new AffineTransform(0.9056599736213684f, 0.0f, 0.0f, 0.9056599736213684f, 9.83029556274414f, -35.688838958740234f));
// _0_0_0_8 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(286.6658935546875, -439.48358154296875), new Point2D.Double(289.765625, -436.70703125), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
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
paint = new Color(136, 138, 133, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(292.9564, -437.33395);
((GeneralPath)shape).curveTo(292.95486, -434.6494, 289.68713, -433.62, 289.68713, -433.62);
((GeneralPath)shape).curveTo(289.68713, -433.62, 292.0359, -435.24597, 292.024, -437.325);
((GeneralPath)shape).curveTo(292.024, -437.325, 292.9564, -437.33395, 292.9564, -437.33395);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_9;
g.setTransform(defaultTransform__0_0_0_9);
g.setClip(clip__0_0_0_9);
float alpha__0_0_0_10 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_10 = g.getClip();
AffineTransform defaultTransform__0_0_0_10 = g.getTransform();
g.transform(new AffineTransform(1.1428569555282593f, 0.0f, 0.0f, 1.1428569555282593f, -28.57139015197754f, 67.00007629394531f));
// _0_0_0_10 is CompositeGraphicsNode
float alpha__0_0_0_10_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_10_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_10_0 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -31.198179244995117f, 24.863309860229492f));
// _0_0_0_10_0 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_10_0;
g.setTransform(defaultTransform__0_0_0_10_0);
g.setClip(clip__0_0_0_10_0);
float alpha__0_0_0_10_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_10_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_10_1 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -31.198179244995117f, 24.863309860229492f));
// _0_0_0_10_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(284.80218505859375, -441.2329406738281), new Point2D.Double(288.8995361328125, -436.8310852050781), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_10_1;
g.setTransform(defaultTransform__0_0_0_10_1);
g.setClip(clip__0_0_0_10_1);
origAlpha = alpha__0_0_0_10;
g.setTransform(defaultTransform__0_0_0_10);
g.setClip(clip__0_0_0_10);
origAlpha = alpha__0_0_0;
g.setTransform(defaultTransform__0_0_0);
g.setClip(clip__0_0_0);
float alpha__0_0_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1 = g.getClip();
AffineTransform defaultTransform__0_0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1 is CompositeGraphicsNode
float alpha__0_0_1_0 = origAlpha;
origAlpha = origAlpha * 0.7f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_0 = g.getClip();
AffineTransform defaultTransform__0_0_1_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_0 is CompositeGraphicsNode
float alpha__0_0_1_0_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_0_0 = g.getClip();
AffineTransform defaultTransform__0_0_1_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_0_0 is ShapeNode
paint = new Color(252, 233, 79, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(24.0, 2.5);
((GeneralPath)shape).lineTo(21.625, 9.1875);
((GeneralPath)shape).curveTo(22.399035, 9.064132, 23.191406, 9.0, 24.0, 9.0);
((GeneralPath)shape).curveTo(24.808594, 9.0, 25.600965, 9.064132, 26.375, 9.1875);
((GeneralPath)shape).lineTo(24.0, 2.5);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(8.8125, 8.78125);
((GeneralPath)shape).lineTo(11.84375, 15.21875);
((GeneralPath)shape).curveTo(12.779034, 13.928569, 13.928569, 12.779034, 15.21875, 11.84375);
((GeneralPath)shape).lineTo(8.8125, 8.78125);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(39.21875, 8.78125);
((GeneralPath)shape).lineTo(32.78125, 11.84375);
((GeneralPath)shape).curveTo(34.07143, 12.779034, 35.220966, 13.928569, 36.15625, 15.21875);
((GeneralPath)shape).lineTo(39.21875, 8.78125);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(9.1875, 21.59375);
((GeneralPath)shape).lineTo(2.5, 23.96875);
((GeneralPath)shape).lineTo(9.1875, 26.34375);
((GeneralPath)shape).curveTo(9.067337, 25.57952, 9.0, 24.797813, 9.0, 24.0);
((GeneralPath)shape).curveTo(9.0, 23.180626, 9.060885, 22.377571, 9.1875, 21.59375);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(38.8125, 21.625);
((GeneralPath)shape).curveTo(38.935867, 22.399035, 39.0, 23.191406, 39.0, 24.0);
((GeneralPath)shape).curveTo(39.0, 24.808594, 38.935867, 25.600965, 38.8125, 26.375);
((GeneralPath)shape).lineTo(45.5, 24.0);
((GeneralPath)shape).lineTo(38.8125, 21.625);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(11.84375, 32.78125);
((GeneralPath)shape).lineTo(8.8125, 39.1875);
((GeneralPath)shape).lineTo(15.21875, 36.15625);
((GeneralPath)shape).curveTo(13.928569, 35.220966, 12.779034, 34.07143, 11.84375, 32.78125);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(36.15625, 32.78125);
((GeneralPath)shape).curveTo(35.22979, 34.05926, 34.087616, 35.194798, 32.8125, 36.125);
((GeneralPath)shape).lineTo(39.21875, 39.1875);
((GeneralPath)shape).lineTo(36.15625, 32.78125);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(21.625, 38.8125);
((GeneralPath)shape).lineTo(24.0, 45.5);
((GeneralPath)shape).lineTo(26.375, 38.8125);
((GeneralPath)shape).curveTo(25.600965, 38.935867, 24.808594, 39.0, 24.0, 39.0);
((GeneralPath)shape).curveTo(23.191406, 39.0, 22.399035, 38.935867, 21.625, 38.8125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(252, 175, 62, 255);
stroke = new BasicStroke(0.73732895f,2,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(24.0, 2.5);
((GeneralPath)shape).lineTo(21.625, 9.1875);
((GeneralPath)shape).curveTo(22.399035, 9.064132, 23.191406, 9.0, 24.0, 9.0);
((GeneralPath)shape).curveTo(24.808594, 9.0, 25.600965, 9.064132, 26.375, 9.1875);
((GeneralPath)shape).lineTo(24.0, 2.5);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(8.8125, 8.78125);
((GeneralPath)shape).lineTo(11.84375, 15.21875);
((GeneralPath)shape).curveTo(12.779034, 13.928569, 13.928569, 12.779034, 15.21875, 11.84375);
((GeneralPath)shape).lineTo(8.8125, 8.78125);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(39.21875, 8.78125);
((GeneralPath)shape).lineTo(32.78125, 11.84375);
((GeneralPath)shape).curveTo(34.07143, 12.779034, 35.220966, 13.928569, 36.15625, 15.21875);
((GeneralPath)shape).lineTo(39.21875, 8.78125);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(9.1875, 21.59375);
((GeneralPath)shape).lineTo(2.5, 23.96875);
((GeneralPath)shape).lineTo(9.1875, 26.34375);
((GeneralPath)shape).curveTo(9.067337, 25.57952, 9.0, 24.797813, 9.0, 24.0);
((GeneralPath)shape).curveTo(9.0, 23.180626, 9.060885, 22.377571, 9.1875, 21.59375);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(38.8125, 21.625);
((GeneralPath)shape).curveTo(38.935867, 22.399035, 39.0, 23.191406, 39.0, 24.0);
((GeneralPath)shape).curveTo(39.0, 24.808594, 38.935867, 25.600965, 38.8125, 26.375);
((GeneralPath)shape).lineTo(45.5, 24.0);
((GeneralPath)shape).lineTo(38.8125, 21.625);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(11.84375, 32.78125);
((GeneralPath)shape).lineTo(8.8125, 39.1875);
((GeneralPath)shape).lineTo(15.21875, 36.15625);
((GeneralPath)shape).curveTo(13.928569, 35.220966, 12.779034, 34.07143, 11.84375, 32.78125);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(36.15625, 32.78125);
((GeneralPath)shape).curveTo(35.22979, 34.05926, 34.087616, 35.194798, 32.8125, 36.125);
((GeneralPath)shape).lineTo(39.21875, 39.1875);
((GeneralPath)shape).lineTo(36.15625, 32.78125);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(21.625, 38.8125);
((GeneralPath)shape).lineTo(24.0, 45.5);
((GeneralPath)shape).lineTo(26.375, 38.8125);
((GeneralPath)shape).curveTo(25.600965, 38.935867, 24.808594, 39.0, 24.0, 39.0);
((GeneralPath)shape).curveTo(23.191406, 39.0, 22.399035, 38.935867, 21.625, 38.8125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_1_0_0;
g.setTransform(defaultTransform__0_0_1_0_0);
g.setClip(clip__0_0_1_0_0);
float alpha__0_0_1_0_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_0_1 = g.getClip();
AffineTransform defaultTransform__0_0_1_0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_0_1 is ShapeNode
paint = new RadialGradientPaint(new Point2D.Double(23.999990463256836, 23.381505966186523), 19.141981f, new Point2D.Double(23.999990463256836, 23.381505966186523), new float[] {0.0f,0.75f,1.0f}, new Color[] {new Color(255, 255, 255, 0),new Color(255, 255, 255, 0),new Color(255, 255, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0067009925842285f, 2.235326035853407E-16f, -2.23715006836576E-16f, 1.0075219869613647f, -0.1608159989118576f, 0.42698100209236145f));
stroke = new BasicStroke(0.8464625f,2,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(24.0, 5.25);
((GeneralPath)shape).lineTo(22.65625, 9.0625);
((GeneralPath)shape).curveTo(23.098888, 9.023149, 23.547188, 9.0, 24.0, 9.0);
((GeneralPath)shape).curveTo(24.452812, 9.0, 24.901112, 9.023149, 25.34375, 9.0625);
((GeneralPath)shape).lineTo(24.0, 5.25);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(10.78125, 10.75);
((GeneralPath)shape).lineTo(12.5, 14.375);
((GeneralPath)shape).curveTo(13.071538, 13.694089, 13.724004, 13.038745, 14.40625, 12.46875);
((GeneralPath)shape).lineTo(10.78125, 10.75);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(37.25, 10.75);
((GeneralPath)shape).lineTo(33.625, 12.46875);
((GeneralPath)shape).curveTo(34.304676, 13.038189, 34.96181, 13.695325, 35.53125, 14.375);
((GeneralPath)shape).lineTo(37.25, 10.75);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(9.0625, 22.625);
((GeneralPath)shape).lineTo(5.28125, 23.96875);
((GeneralPath)shape).lineTo(9.0625, 25.3125);
((GeneralPath)shape).curveTo(9.024981, 24.880146, 9.0, 24.442032, 9.0, 24.0);
((GeneralPath)shape).curveTo(9.0, 23.536406, 9.021274, 23.077908, 9.0625, 22.625);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(38.9375, 22.65625);
((GeneralPath)shape).curveTo(38.976852, 23.098888, 39.0, 23.547188, 39.0, 24.0);
((GeneralPath)shape).curveTo(39.0, 24.452812, 38.976852, 24.901112, 38.9375, 25.34375);
((GeneralPath)shape).lineTo(42.71875, 24.0);
((GeneralPath)shape).lineTo(38.9375, 22.65625);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(35.53125, 33.59375);
((GeneralPath)shape).curveTo(34.958294, 34.27954, 34.309986, 34.957363, 33.625, 35.53125);
((GeneralPath)shape).lineTo(37.25, 37.25);
((GeneralPath)shape).lineTo(35.53125, 33.59375);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(12.5, 33.625);
((GeneralPath)shape).lineTo(10.78125, 37.21875);
((GeneralPath)shape).lineTo(14.375, 35.5);
((GeneralPath)shape).curveTo(13.702932, 34.935883, 13.064116, 34.29707, 12.5, 33.625);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(22.65625, 38.9375);
((GeneralPath)shape).lineTo(24.0, 42.71875);
((GeneralPath)shape).lineTo(25.34375, 38.9375);
((GeneralPath)shape).curveTo(24.901112, 38.976852, 24.452812, 39.0, 24.0, 39.0);
((GeneralPath)shape).curveTo(23.547188, 39.0, 23.098888, 38.976852, 22.65625, 38.9375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
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
// _0_0_1_1 is CompositeGraphicsNode
float alpha__0_0_1_1_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_1_0 = g.getClip();
AffineTransform defaultTransform__0_0_1_1_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_1_0 is CompositeGraphicsNode
float alpha__0_0_1_1_0_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_1_0_0 = g.getClip();
AffineTransform defaultTransform__0_0_1_1_0_0 = g.getTransform();
g.transform(new AffineTransform(0.7780619859695435f, -1.0612850189208984f, 1.0612870454788208f, 0.7780619859695435f, 67.47952270507812f, 3.641324043273926f));
// _0_0_1_1_0_0 is ShapeNode
paint = new Color(255, 238, 84, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(-22.5, -17.5);
((GeneralPath)shape).curveTo(-22.5, -12.253295, -26.753294, -8.0, -32.0, -8.0);
((GeneralPath)shape).curveTo(-37.246704, -8.0, -41.5, -12.253295, -41.5, -17.5);
((GeneralPath)shape).curveTo(-41.5, -22.746706, -37.246704, -27.0, -32.0, -27.0);
((GeneralPath)shape).curveTo(-26.753294, -27.0, -22.5, -22.746706, -22.5, -17.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(252, 175, 62, 255);
stroke = new BasicStroke(0.7599118f,2,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(-22.5, -17.5);
((GeneralPath)shape).curveTo(-22.5, -12.253295, -26.753294, -8.0, -32.0, -8.0);
((GeneralPath)shape).curveTo(-37.246704, -8.0, -41.5, -12.253295, -41.5, -17.5);
((GeneralPath)shape).curveTo(-41.5, -22.746706, -37.246704, -27.0, -32.0, -27.0);
((GeneralPath)shape).curveTo(-26.753294, -27.0, -22.5, -22.746706, -22.5, -17.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_1_1_0_0;
g.setTransform(defaultTransform__0_0_1_1_0_0);
g.setClip(clip__0_0_1_1_0_0);
float alpha__0_0_1_1_0_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_1_0_1 = g.getClip();
AffineTransform defaultTransform__0_0_1_1_0_1 = g.getTransform();
g.transform(new AffineTransform(1.2442569732666016f, -0.16770699620246887f, 0.21664200723171234f, 1.251844048500061f, 67.6164779663086f, 40.527000427246094f));
// _0_0_1_1_0_1 is ShapeNode
paint = new RadialGradientPaint(new Point2D.Double(-33.519073486328125, -22.113296508789062), 9.5f, new Point2D.Double(-33.519073486328125, -22.113296508789062), new float[] {0.0f,0.5939414f,0.8385055f,1.0f}, new Color[] {new Color(255, 247, 194, 163),new Color(252, 175, 62, 47),new Color(252, 175, 62, 129),new Color(252, 175, 62, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.4877389967441559f, 1.2924020290374756f, -1.1026699542999268f, 0.4972420036792755f, -41.773929595947266f, 32.414920806884766f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(-22.5, -17.5);
((GeneralPath)shape).curveTo(-22.5, -12.253295, -26.753294, -8.0, -32.0, -8.0);
((GeneralPath)shape).curveTo(-37.246704, -8.0, -41.5, -12.253295, -41.5, -17.5);
((GeneralPath)shape).curveTo(-41.5, -22.746706, -37.246704, -27.0, -32.0, -27.0);
((GeneralPath)shape).curveTo(-26.753294, -27.0, -22.5, -22.746706, -22.5, -17.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_1_1_0_1;
g.setTransform(defaultTransform__0_0_1_1_0_1);
g.setClip(clip__0_0_1_1_0_1);
float alpha__0_0_1_1_0_2 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_1_0_2 = g.getClip();
AffineTransform defaultTransform__0_0_1_1_0_2 = g.getTransform();
g.transform(new AffineTransform(0.7157909870147705f, -0.9763489961624146f, 0.9763500094413757f, 0.7157920002937317f, 64.00044250488281f, 5.2695441246032715f));
// _0_0_1_1_0_2 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(-28.968944549560547, -25.326814651489258), new Point2D.Double(-37.19697952270508, -9.559050559997559), new float[] {0.0f,0.5416667f,1.0f}, new Color[] {new Color(255, 249, 198, 255),new Color(255, 242, 140, 255),new Color(255, 234, 133, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(0.82601947f,2,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(-22.5, -17.5);
((GeneralPath)shape).curveTo(-22.5, -12.253295, -26.753294, -8.0, -32.0, -8.0);
((GeneralPath)shape).curveTo(-37.246704, -8.0, -41.5, -12.253295, -41.5, -17.5);
((GeneralPath)shape).curveTo(-41.5, -22.746706, -37.246704, -27.0, -32.0, -27.0);
((GeneralPath)shape).curveTo(-26.753294, -27.0, -22.5, -22.746706, -22.5, -17.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_1_1_0_2;
g.setTransform(defaultTransform__0_0_1_1_0_2);
g.setClip(clip__0_0_1_1_0_2);
origAlpha = alpha__0_0_1_1_0;
g.setTransform(defaultTransform__0_0_1_1_0);
g.setClip(clip__0_0_1_1_0);
origAlpha = alpha__0_0_1_1;
g.setTransform(defaultTransform__0_0_1_1);
g.setClip(clip__0_0_1_1);
origAlpha = alpha__0_0_1;
g.setTransform(defaultTransform__0_0_1);
g.setClip(clip__0_0_1);
float alpha__0_0_2 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2 = g.getClip();
AffineTransform defaultTransform__0_0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -248.99000549316406f, 467.9855041503906f));
// _0_0_2 is CompositeGraphicsNode
float alpha__0_0_2_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_0 = g.getClip();
AffineTransform defaultTransform__0_0_2_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2_0 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(280.5, -445.5);
((GeneralPath)shape).curveTo(278.22916, -445.5, 276.39008, -443.9497, 275.78125, -441.875);
((GeneralPath)shape).curveTo(275.088, -442.23883, 274.33673, -442.5, 273.5, -442.5);
((GeneralPath)shape).curveTo(270.74, -442.5, 268.5, -440.26, 268.5, -437.5);
((GeneralPath)shape).curveTo(268.5, -436.92108, 268.6625, -436.3923, 268.84375, -435.875);
((GeneralPath)shape).curveTo(267.47028, -435.10425, 266.5, -433.686, 266.5, -432.0);
((GeneralPath)shape).curveTo(266.5, -429.516, 268.516, -427.5, 271.0, -427.5);
((GeneralPath)shape).curveTo(271.17712, -427.5, 289.82288, -427.5, 290.0, -427.5);
((GeneralPath)shape).curveTo(292.48398, -427.5, 294.5, -429.516, 294.5, -432.0);
((GeneralPath)shape).curveTo(294.5, -433.686, 293.52972, -435.10425, 292.15625, -435.875);
((GeneralPath)shape).curveTo(292.3375, -436.3923, 292.5, -436.92108, 292.5, -437.5);
((GeneralPath)shape).curveTo(292.5, -440.26, 290.26, -442.5, 287.5, -442.5);
((GeneralPath)shape).curveTo(286.66327, -442.5, 285.912, -442.23883, 285.21875, -441.875);
((GeneralPath)shape).curveTo(284.60992, -443.9497, 282.77084, -445.5, 280.5, -445.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(136, 138, 133, 255);
stroke = new BasicStroke(1.0f,0,0,2.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(280.5, -445.5);
((GeneralPath)shape).curveTo(278.22916, -445.5, 276.39008, -443.9497, 275.78125, -441.875);
((GeneralPath)shape).curveTo(275.088, -442.23883, 274.33673, -442.5, 273.5, -442.5);
((GeneralPath)shape).curveTo(270.74, -442.5, 268.5, -440.26, 268.5, -437.5);
((GeneralPath)shape).curveTo(268.5, -436.92108, 268.6625, -436.3923, 268.84375, -435.875);
((GeneralPath)shape).curveTo(267.47028, -435.10425, 266.5, -433.686, 266.5, -432.0);
((GeneralPath)shape).curveTo(266.5, -429.516, 268.516, -427.5, 271.0, -427.5);
((GeneralPath)shape).curveTo(271.17712, -427.5, 289.82288, -427.5, 290.0, -427.5);
((GeneralPath)shape).curveTo(292.48398, -427.5, 294.5, -429.516, 294.5, -432.0);
((GeneralPath)shape).curveTo(294.5, -433.686, 293.52972, -435.10425, 292.15625, -435.875);
((GeneralPath)shape).curveTo(292.3375, -436.3923, 292.5, -436.92108, 292.5, -437.5);
((GeneralPath)shape).curveTo(292.5, -440.26, 290.26, -442.5, 287.5, -442.5);
((GeneralPath)shape).curveTo(286.66327, -442.5, 285.912, -442.23883, 285.21875, -441.875);
((GeneralPath)shape).curveTo(284.60992, -443.9497, 282.77084, -445.5, 280.5, -445.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_2_0;
g.setTransform(defaultTransform__0_0_2_0);
g.setClip(clip__0_0_2_0);
float alpha__0_0_2_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_1 = g.getClip();
AffineTransform defaultTransform__0_0_2_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(271.0216979980469, -441.05181884765625), new Point2D.Double(285.0285949707031, -431.96990966796875), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(280.5, -445.0);
((GeneralPath)shape).curveTo(278.31027, -445.0, 276.7764, -443.6642, 276.10446, -441.1565);
((GeneralPath)shape).curveTo(275.436, -441.5001, 274.55685, -441.98984, 273.75, -441.98984);
((GeneralPath)shape).curveTo(271.03348, -441.98984, 268.99487, -440.051, 268.99487, -437.44427);
((GeneralPath)shape).curveTo(268.99487, -436.89752, 269.2621, -436.11084, 269.43683, -435.62228);
((GeneralPath)shape).curveTo(268.1124, -434.89432, 267.0, -433.73178, 267.0, -432.24973);
((GeneralPath)shape).curveTo(267.0, -429.9037, 268.54617, -427.99963, 271.3393, -427.99963);
((GeneralPath)shape).curveTo(271.5101, -427.99963, 289.48993, -427.99963, 289.6607, -427.99963);
((GeneralPath)shape).curveTo(292.43173, -427.99963, 294.0, -429.9037, 294.0, -432.24973);
((GeneralPath)shape).curveTo(294.0, -433.8421, 292.8876, -434.9164, 291.56317, -435.64438);
((GeneralPath)shape).curveTo(291.7379, -436.13293, 292.02725, -436.89752, 292.02725, -437.44427);
((GeneralPath)shape).curveTo(292.02725, -440.051, 289.91144, -442.01193, 287.25, -442.01193);
((GeneralPath)shape).curveTo(286.44315, -442.01193, 285.6082, -441.5222, 284.93973, -441.17856);
((GeneralPath)shape).curveTo(284.2909, -443.6001, 282.68973, -445.0, 280.5, -445.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_2_1;
g.setTransform(defaultTransform__0_0_2_1);
g.setClip(clip__0_0_2_1);
float alpha__0_0_2_2 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_2 = g.getClip();
AffineTransform defaultTransform__0_0_2_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2_2 is CompositeGraphicsNode
float alpha__0_0_2_2_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_2_0 = g.getClip();
AffineTransform defaultTransform__0_0_2_2_0 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -24.198179244995117f, 21.863309860229492f));
// _0_0_2_2_0 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_2_2_0;
g.setTransform(defaultTransform__0_0_2_2_0);
g.setClip(clip__0_0_2_2_0);
float alpha__0_0_2_2_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_2_1 = g.getClip();
AffineTransform defaultTransform__0_0_2_2_1 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -24.198179244995117f, 21.863309860229492f));
// _0_0_2_2_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(287.5173034667969, -439.7528076171875), new Point2D.Double(289.67633056640625, -436.3219909667969), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_2_2_1;
g.setTransform(defaultTransform__0_0_2_2_1);
g.setClip(clip__0_0_2_2_1);
origAlpha = alpha__0_0_2_2;
g.setTransform(defaultTransform__0_0_2_2);
g.setClip(clip__0_0_2_2);
float alpha__0_0_2_3 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_3 = g.getClip();
AffineTransform defaultTransform__0_0_2_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2_3 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new Rectangle2D.Double(271.0, -438.0, 20.0, 9.0);
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_2_3;
g.setTransform(defaultTransform__0_0_2_3);
g.setClip(clip__0_0_2_3);
float alpha__0_0_2_4 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_4 = g.getClip();
AffineTransform defaultTransform__0_0_2_4 = g.getTransform();
g.transform(new AffineTransform(0.9056599736213684f, 0.0f, 0.0f, 0.9056599736213684f, 9.830195426940918f, -35.688690185546875f));
// _0_0_2_4 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_2_4;
g.setTransform(defaultTransform__0_0_2_4);
g.setClip(clip__0_0_2_4);
float alpha__0_0_2_5 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_5 = g.getClip();
AffineTransform defaultTransform__0_0_2_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2_5 is CompositeGraphicsNode
float alpha__0_0_2_5_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_5_0 = g.getClip();
AffineTransform defaultTransform__0_0_2_5_0 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -17.198110580444336f, 24.863210678100586f));
// _0_0_2_5_0 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_2_5_0;
g.setTransform(defaultTransform__0_0_2_5_0);
g.setClip(clip__0_0_2_5_0);
float alpha__0_0_2_5_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_5_1 = g.getClip();
AffineTransform defaultTransform__0_0_2_5_1 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -17.198179244995117f, 24.863309860229492f));
// _0_0_2_5_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(286.51171875, -441.2907409667969), new Point2D.Double(289.8537902832031, -436.14453125), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_2_5_1;
g.setTransform(defaultTransform__0_0_2_5_1);
g.setClip(clip__0_0_2_5_1);
origAlpha = alpha__0_0_2_5;
g.setTransform(defaultTransform__0_0_2_5);
g.setClip(clip__0_0_2_5);
float alpha__0_0_2_6 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_6 = g.getClip();
AffineTransform defaultTransform__0_0_2_6 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2_6 is CompositeGraphicsNode
float alpha__0_0_2_6_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_6_0 = g.getClip();
AffineTransform defaultTransform__0_0_2_6_0 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -31.198179244995117f, 24.863309860229492f));
// _0_0_2_6_0 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_2_6_0;
g.setTransform(defaultTransform__0_0_2_6_0);
g.setClip(clip__0_0_2_6_0);
float alpha__0_0_2_6_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_6_1 = g.getClip();
AffineTransform defaultTransform__0_0_2_6_1 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -31.198179244995117f, 24.863309860229492f));
// _0_0_2_6_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(285.94085693359375, -439.9389953613281), new Point2D.Double(289.3912353515625, -436.4429016113281), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_2_6_1;
g.setTransform(defaultTransform__0_0_2_6_1);
g.setClip(clip__0_0_2_6_1);
origAlpha = alpha__0_0_2_6;
g.setTransform(defaultTransform__0_0_2_6);
g.setClip(clip__0_0_2_6);
float alpha__0_0_2_7 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_7 = g.getClip();
AffineTransform defaultTransform__0_0_2_7 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 0.0f));
// _0_0_2_7 is CompositeGraphicsNode
float alpha__0_0_2_7_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_7_0 = g.getClip();
AffineTransform defaultTransform__0_0_2_7_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2_7_0 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(280.46875, -440.96875);
((GeneralPath)shape).curveTo(276.88937, -440.96875, 274.0, -438.04813, 274.0, -434.46875);
((GeneralPath)shape).curveTo(274.0, -432.09808, 275.34943, -430.13095, 277.25, -429.0);
((GeneralPath)shape).lineTo(283.71875, -429.0);
((GeneralPath)shape).curveTo(285.61932, -430.13095, 286.96875, -432.1293, 286.96875, -434.5);
((GeneralPath)shape).curveTo(286.96875, -438.07938, 284.04813, -440.96875, 280.46875, -440.96875);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_2_7_0;
g.setTransform(defaultTransform__0_0_2_7_0);
g.setClip(clip__0_0_2_7_0);
float alpha__0_0_2_7_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_7_1 = g.getClip();
AffineTransform defaultTransform__0_0_2_7_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2_7_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(275.9419250488281, -437.1050109863281), new Point2D.Double(279.9754638671875, -431.9183349609375), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(280.5, -441.0);
((GeneralPath)shape).curveTo(276.912, -441.0, 274.0, -438.08798, 274.0, -434.5);
((GeneralPath)shape).curveTo(274.0, -432.1236, 275.34485, -430.13367, 277.25, -429.0);
((GeneralPath)shape).lineTo(283.75, -429.0);
((GeneralPath)shape).curveTo(285.65515, -430.13367, 287.0, -432.1236, 287.0, -434.5);
((GeneralPath)shape).curveTo(287.0, -438.088, 284.088, -441.0, 280.5, -441.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_2_7_1;
g.setTransform(defaultTransform__0_0_2_7_1);
g.setClip(clip__0_0_2_7_1);
origAlpha = alpha__0_0_2_7;
g.setTransform(defaultTransform__0_0_2_7);
g.setClip(clip__0_0_2_7);
float alpha__0_0_2_8 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_8 = g.getClip();
AffineTransform defaultTransform__0_0_2_8 = g.getTransform();
g.transform(new AffineTransform(0.9056599736213684f, 0.0f, 0.0f, 0.9056599736213684f, 9.83029556274414f, -35.688838958740234f));
// _0_0_2_8 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(286.6658935546875, -439.48358154296875), new Point2D.Double(289.765625, -436.70703125), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_2_8;
g.setTransform(defaultTransform__0_0_2_8);
g.setClip(clip__0_0_2_8);
float alpha__0_0_2_9 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_9 = g.getClip();
AffineTransform defaultTransform__0_0_2_9 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2_9 is ShapeNode
paint = new Color(136, 138, 133, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(292.9564, -437.33395);
((GeneralPath)shape).curveTo(292.95486, -434.6494, 289.68713, -433.62, 289.68713, -433.62);
((GeneralPath)shape).curveTo(289.68713, -433.62, 292.0359, -435.24597, 292.024, -437.325);
((GeneralPath)shape).curveTo(292.024, -437.325, 292.9564, -437.33395, 292.9564, -437.33395);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_2_9;
g.setTransform(defaultTransform__0_0_2_9);
g.setClip(clip__0_0_2_9);
float alpha__0_0_2_10 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_10 = g.getClip();
AffineTransform defaultTransform__0_0_2_10 = g.getTransform();
g.transform(new AffineTransform(1.1428569555282593f, 0.0f, 0.0f, 1.1428569555282593f, -28.57139015197754f, 67.00007629394531f));
// _0_0_2_10 is CompositeGraphicsNode
float alpha__0_0_2_10_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_10_0 = g.getClip();
AffineTransform defaultTransform__0_0_2_10_0 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -31.198179244995117f, 24.863309860229492f));
// _0_0_2_10_0 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_2_10_0;
g.setTransform(defaultTransform__0_0_2_10_0);
g.setClip(clip__0_0_2_10_0);
float alpha__0_0_2_10_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_10_1 = g.getClip();
AffineTransform defaultTransform__0_0_2_10_1 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -31.198179244995117f, 24.863309860229492f));
// _0_0_2_10_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(284.80218505859375, -441.2329406738281), new Point2D.Double(288.8995361328125, -436.8310852050781), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(291.6875, -437.59375);
((GeneralPath)shape).curveTo(291.6875, -435.7643, 290.20444, -434.28125, 288.375, -434.28125);
((GeneralPath)shape).curveTo(286.54556, -434.28125, 285.0625, -435.7643, 285.0625, -437.59375);
((GeneralPath)shape).curveTo(285.0625, -439.4232, 286.54556, -440.90625, 288.375, -440.90625);
((GeneralPath)shape).curveTo(290.20444, -440.90625, 291.6875, -439.4232, 291.6875, -437.59375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_2_10_1;
g.setTransform(defaultTransform__0_0_2_10_1);
g.setClip(clip__0_0_2_10_1);
origAlpha = alpha__0_0_2_10;
g.setTransform(defaultTransform__0_0_2_10);
g.setClip(clip__0_0_2_10);
origAlpha = alpha__0_0_2;
g.setTransform(defaultTransform__0_0_2);
g.setClip(clip__0_0_2);
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
        return 2;
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
	public CloudyIcon() {
        this.width = getOrigWidth();
        this.height = getOrigHeight();
	}
	
	/**
	 * Creates a new transcoded SVG image with the given dimensions.
	 *
	 * @param size the dimensions of the icon
	 */
	public CloudyIcon(Dimension size) {
	this.width = size.width;
	this.height = size.width;
	}

	public CloudyIcon(int width, int height) {
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

