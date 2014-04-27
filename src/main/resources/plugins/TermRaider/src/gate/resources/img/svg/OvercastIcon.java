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
public class OvercastIcon implements
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
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -287.0f, 298.0f));
// _0_0_0 is CompositeGraphicsNode
float alpha__0_0_0_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_0 is ShapeNode
paint = new Color(136, 138, 133, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(311.5026, -296.0);
((GeneralPath)shape).curveTo(308.73016, -296.0, 306.39435, -294.4263, 305.09634, -292.1875);
((GeneralPath)shape).curveTo(304.15198, -292.66254, 303.13116, -293.0, 302.0026, -293.0);
((GeneralPath)shape).curveTo(298.13858, -293.0, 295.0026, -289.864, 295.0026, -286.0);
((GeneralPath)shape).curveTo(295.0026, -282.136, 298.13858, -279.0, 302.0026, -279.0);
((GeneralPath)shape).curveTo(304.42227, -279.0, 306.43268, -280.3193, 307.6901, -282.1875);
((GeneralPath)shape).curveTo(308.82428, -281.4979, 310.07907, -281.0, 311.5026, -281.0);
((GeneralPath)shape).curveTo(312.4157, -281.0, 313.25555, -281.23203, 314.0651, -281.53125);
((GeneralPath)shape).curveTo(314.57504, -280.6635, 315.2442, -279.95154, 316.0651, -279.375);
((GeneralPath)shape).curveTo(316.05786, -279.24463, 316.0026, -279.13217, 316.0026, -279.0);
((GeneralPath)shape).curveTo(316.0026, -275.136, 319.13858, -272.0, 323.0026, -272.0);
((GeneralPath)shape).curveTo(326.86658, -272.0, 330.0026, -275.136, 330.0026, -279.0);
((GeneralPath)shape).curveTo(330.0026, -281.3697, 328.74362, -283.35834, 326.9401, -284.625);
((GeneralPath)shape).curveTo(326.94733, -284.75537, 327.0026, -284.86783, 327.0026, -285.0);
((GeneralPath)shape).curveTo(327.0026, -288.864, 323.8666, -292.0, 320.0026, -292.0);
((GeneralPath)shape).curveTo(319.37988, -292.0, 318.8274, -291.7778, 318.2526, -291.625);
((GeneralPath)shape).curveTo(317.05807, -294.18384, 314.51126, -296.0, 311.5026, -296.0);
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
paint = new LinearGradientPaint(new Point2D.Double(228.5026092529297, -392.305908203125), new Point2D.Double(278.91510009765625, -375.3795166015625), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 69.0025863647461f, 102.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(311.5026, -295.0);
((GeneralPath)shape).curveTo(308.7221, -295.0, 306.36807, -293.23816, 305.4401, -290.78125);
((GeneralPath)shape).curveTo(304.45468, -291.4907, 303.30865, -292.0, 302.0026, -292.0);
((GeneralPath)shape).curveTo(298.69058, -292.0, 296.0026, -289.312, 296.0026, -286.0);
((GeneralPath)shape).curveTo(296.0026, -282.688, 298.69058, -280.0, 302.0026, -280.0);
((GeneralPath)shape).curveTo(304.43033, -280.0, 306.49582, -281.45557, 307.4401, -283.53125);
((GeneralPath)shape).curveTo(308.56085, -282.61368, 309.94223, -282.0, 311.5026, -282.0);
((GeneralPath)shape).curveTo(312.57712, -282.0, 313.54688, -282.31897, 314.4401, -282.78125);
((GeneralPath)shape).curveTo(314.8385, -281.7815, 315.54123, -280.99493, 316.3776, -280.34375);
((GeneralPath)shape).curveTo(316.19757, -279.74814, 316.0026, -279.1541, 316.0026, -278.5);
((GeneralPath)shape).curveTo(316.0026, -274.912, 318.91458, -272.0, 322.5026, -272.0);
((GeneralPath)shape).curveTo(326.09058, -272.0, 329.0026, -274.912, 329.0026, -278.5);
((GeneralPath)shape).curveTo(329.0026, -280.86078, 327.66827, -282.8302, 325.78384, -283.96875);
((GeneralPath)shape).curveTo(325.84644, -284.31598, 326.0026, -284.63483, 326.0026, -285.0);
((GeneralPath)shape).curveTo(326.0026, -288.312, 323.31458, -291.0, 320.0026, -291.0);
((GeneralPath)shape).curveTo(319.1496, -291.0, 318.3313, -290.82132, 317.59634, -290.5);
((GeneralPath)shape).curveTo(316.74258, -293.09387, 314.3811, -295.0, 311.5026, -295.0);
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
g.transform(new AffineTransform(0.964447021484375f, 0.0f, 0.0f, 0.964447021484375f, 89.29110717773438f, 91.52620697021484f));
// _0_0_0_2 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(248.54803, -383.6666);
((GeneralPath)shape).curveTo(248.55025, -381.2573, 247.26617, -379.03003, 245.18002, -377.82474);
((GeneralPath)shape).curveTo(243.09387, -376.61945, 240.52298, -376.61945, 238.43683, -377.82474);
((GeneralPath)shape).curveTo(236.35068, -379.03003, 235.0666, -381.2573, 235.06882, -383.6666);
((GeneralPath)shape).curveTo(235.0666, -386.0759, 236.35068, -388.30316, 238.43683, -389.50845);
((GeneralPath)shape).curveTo(240.52298, -390.71375, 243.09387, -390.71375, 245.18002, -389.50845);
((GeneralPath)shape).curveTo(247.26617, -388.30316, 248.55025, -386.0759, 248.54803, -383.6666);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_2;
g.setTransform(defaultTransform__0_0_0_2);
g.setClip(clip__0_0_0_2);
float alpha__0_0_0_3 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_3 = g.getClip();
AffineTransform defaultTransform__0_0_0_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 69.0025863647461f, 102.0f));
// _0_0_0_3 is CompositeGraphicsNode
float alpha__0_0_0_3_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_3_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_3_0 = g.getTransform();
g.transform(new AffineTransform(0.8826299905776978f, 0.0f, 0.0f, 0.8826299905776978f, 27.1807804107666f, -46.89094161987305f));
// _0_0_0_3_0 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(250.18323, -389.30136);
((GeneralPath)shape).curveTo(250.18323, -385.85986, 247.39334, -383.06998, 243.95184, -383.06998);
((GeneralPath)shape).curveTo(240.51035, -383.06998, 237.72046, -385.85986, 237.72046, -389.30136);
((GeneralPath)shape).curveTo(237.72046, -392.74286, 240.51035, -395.53275, 243.95184, -395.53275);
((GeneralPath)shape).curveTo(247.39334, -395.53275, 250.18323, -392.74286, 250.18323, -389.30136);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_3_0;
g.setTransform(defaultTransform__0_0_0_3_0);
g.setClip(clip__0_0_0_3_0);
float alpha__0_0_0_3_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_3_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_3_1 = g.getTransform();
g.transform(new AffineTransform(0.8826299905776978f, 0.0f, 0.0f, 0.8826299905776978f, 27.1807804107666f, -46.89094161987305f));
// _0_0_0_3_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(240.07379150390625, -393.4071960449219), new Point2D.Double(245.82705688476562, -388.55029296875), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(250.18323, -389.30136);
((GeneralPath)shape).curveTo(250.18323, -385.85986, 247.39334, -383.06998, 243.95184, -383.06998);
((GeneralPath)shape).curveTo(240.51035, -383.06998, 237.72046, -385.85986, 237.72046, -389.30136);
((GeneralPath)shape).curveTo(237.72046, -392.74286, 240.51035, -395.53275, 243.95184, -395.53275);
((GeneralPath)shape).curveTo(247.39334, -395.53275, 250.18323, -392.74286, 250.18323, -389.30136);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_3_1;
g.setTransform(defaultTransform__0_0_0_3_1);
g.setClip(clip__0_0_0_3_1);
origAlpha = alpha__0_0_0_3;
g.setTransform(defaultTransform__0_0_0_3);
g.setClip(clip__0_0_0_3);
float alpha__0_0_0_4 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_4 = g.getClip();
AffineTransform defaultTransform__0_0_0_4 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 69.0025863647461f, 102.0f));
// _0_0_0_4 is CompositeGraphicsNode
float alpha__0_0_0_4_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_4_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_4_0 = g.getTransform();
g.transform(new AffineTransform(0.911728024482727f, 0.0f, 0.0f, 0.911728024482727f, 21.454069137573242f, -34.7663688659668f));
// _0_0_0_4_0 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(257.2543, -385.7879);
((GeneralPath)shape).curveTo(257.2543, -382.45624, 254.55345, -379.7554, 251.2218, -379.7554);
((GeneralPath)shape).curveTo(247.89014, -379.7554, 245.18929, -382.45624, 245.18929, -385.7879);
((GeneralPath)shape).curveTo(245.18929, -389.11957, 247.89014, -391.8204, 251.2218, -391.8204);
((GeneralPath)shape).curveTo(254.55345, -391.8204, 257.2543, -389.11957, 257.2543, -385.7879);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_4_0;
g.setTransform(defaultTransform__0_0_0_4_0);
g.setClip(clip__0_0_0_4_0);
float alpha__0_0_0_4_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_4_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_4_1 = g.getTransform();
g.transform(new AffineTransform(0.911728024482727f, 0.0f, 0.0f, 0.911728024482727f, 21.454069137573242f, -34.7663688659668f));
// _0_0_0_4_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(246.74041748046875, -391.3138122558594), new Point2D.Double(252.69784545898438, -385.3516540527344), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(257.2543, -385.7879);
((GeneralPath)shape).curveTo(257.2543, -382.45624, 254.55345, -379.7554, 251.2218, -379.7554);
((GeneralPath)shape).curveTo(247.89014, -379.7554, 245.18929, -382.45624, 245.18929, -385.7879);
((GeneralPath)shape).curveTo(245.18929, -389.11957, 247.89014, -391.8204, 251.2218, -391.8204);
((GeneralPath)shape).curveTo(254.55345, -391.8204, 257.2543, -389.11957, 257.2543, -385.7879);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_4_1;
g.setTransform(defaultTransform__0_0_0_4_1);
g.setClip(clip__0_0_0_4_1);
origAlpha = alpha__0_0_0_4;
g.setTransform(defaultTransform__0_0_0_4);
g.setClip(clip__0_0_0_4);
float alpha__0_0_0_5 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_5 = g.getClip();
AffineTransform defaultTransform__0_0_0_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 69.0025863647461f, 102.0f));
// _0_0_0_5 is CompositeGraphicsNode
float alpha__0_0_0_5_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_5_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_5_0 = g.getTransform();
g.transform(new AffineTransform(1.1427990198135376f, 0.0f, 0.0f, 1.1427990198135376f, -33.7677116394043f, 55.27703857421875f));
// _0_0_0_5_0 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(237.80885, -387.88715);
((GeneralPath)shape).curveTo(237.80885, -385.47076, 235.84999, -383.5119, 233.43362, -383.5119);
((GeneralPath)shape).curveTo(231.01726, -383.5119, 229.0584, -385.47076, 229.0584, -387.88715);
((GeneralPath)shape).curveTo(229.0584, -390.30353, 231.01726, -392.2624, 233.43362, -392.2624);
((GeneralPath)shape).curveTo(235.84999, -392.2624, 237.80885, -390.30353, 237.80885, -387.88715);
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
g.transform(new AffineTransform(1.1427990198135376f, 0.0f, 0.0f, 1.1427990198135376f, -33.7677116394043f, 55.27703857421875f));
// _0_0_0_5_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(230.8759765625, -390.43951416015625), new Point2D.Double(235.2565155029297, -386.9590148925781), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(237.80885, -387.88715);
((GeneralPath)shape).curveTo(237.80885, -385.47076, 235.84999, -383.5119, 233.43362, -383.5119);
((GeneralPath)shape).curveTo(231.01726, -383.5119, 229.0584, -385.47076, 229.0584, -387.88715);
((GeneralPath)shape).curveTo(229.0584, -390.30353, 231.01726, -392.2624, 233.43362, -392.2624);
((GeneralPath)shape).curveTo(235.84999, -392.2624, 237.80885, -390.30353, 237.80885, -387.88715);
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
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 69.0025863647461f, 102.0f));
// _0_0_0_6 is CompositeGraphicsNode
float alpha__0_0_0_6_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_6_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_6_0 = g.getTransform();
g.transform(new AffineTransform(1.0386359691619873f, 0.0f, 0.0f, 1.0386359691619873f, -9.15093994140625f, 14.48993968963623f));
// _0_0_0_6_0 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(248.54803, -383.6666);
((GeneralPath)shape).curveTo(248.55025, -381.2573, 247.26617, -379.03003, 245.18002, -377.82474);
((GeneralPath)shape).curveTo(243.09387, -376.61945, 240.52298, -376.61945, 238.43683, -377.82474);
((GeneralPath)shape).curveTo(236.35068, -379.03003, 235.0666, -381.2573, 235.06882, -383.6666);
((GeneralPath)shape).curveTo(235.0666, -386.0759, 236.35068, -388.30316, 238.43683, -389.50845);
((GeneralPath)shape).curveTo(240.52298, -390.71375, 243.09387, -390.71375, 245.18002, -389.50845);
((GeneralPath)shape).curveTo(247.26617, -388.30316, 248.55025, -386.0759, 248.54803, -383.6666);
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
g.transform(new AffineTransform(1.0386359691619873f, 0.0f, 0.0f, 1.0386359691619873f, -9.150933265686035f, 14.489930152893066f));
// _0_0_0_6_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(238.00477600097656, -388.4747619628906), new Point2D.Double(245.6546173095703, -382.6453857421875), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(248.54803, -383.6666);
((GeneralPath)shape).curveTo(248.55025, -381.2573, 247.26617, -379.03003, 245.18002, -377.82474);
((GeneralPath)shape).curveTo(243.09387, -376.61945, 240.52298, -376.61945, 238.43683, -377.82474);
((GeneralPath)shape).curveTo(236.35068, -379.03003, 235.0666, -381.2573, 235.06882, -383.6666);
((GeneralPath)shape).curveTo(235.0666, -386.0759, 236.35068, -388.30316, 238.43683, -389.50845);
((GeneralPath)shape).curveTo(240.52298, -390.71375, 243.09387, -390.71375, 245.18002, -389.50845);
((GeneralPath)shape).curveTo(247.26617, -388.30316, 248.55025, -386.0759, 248.54803, -383.6666);
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
g.transform(new AffineTransform(0.9350280165672302f, 0.0f, 0.0f, 0.9350280165672302f, 446.8280029296875f, -187.61619567871094f));
// _0_0_0_7 is CompositeGraphicsNode
float alpha__0_0_0_7_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_7_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_7_0 = g.getTransform();
g.transform(new AffineTransform(1.737733006477356f, 0.0f, 0.0f, 1.737733006477356f, 110.83219909667969f, 70.07649230957031f));
// _0_0_0_7_0 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(-151.9375, -96.9375);
((GeneralPath)shape).curveTo(-151.9375, -95.21161, -153.33661, -93.8125, -155.0625, -93.8125);
((GeneralPath)shape).curveTo(-156.78839, -93.8125, -158.1875, -95.21161, -158.1875, -96.9375);
((GeneralPath)shape).curveTo(-158.1875, -98.66339, -156.78839, -100.0625, -155.0625, -100.0625);
((GeneralPath)shape).curveTo(-153.33661, -100.0625, -151.9375, -98.66339, -151.9375, -96.9375);
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
g.transform(new AffineTransform(1.737733006477356f, 0.0f, 0.0f, 1.737733006477356f, 110.8947982788086f, 70.01402282714844f));
// _0_0_0_7_1 is ShapeNode
paint = new LinearGradientPaint(new Point2D.Double(-156.29043579101562, -100.53421020507812), new Point2D.Double(-153.0980987548828, -96.5445556640625), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(-151.9375, -96.9375);
((GeneralPath)shape).curveTo(-151.9375, -95.21161, -153.33661, -93.8125, -155.0625, -93.8125);
((GeneralPath)shape).curveTo(-156.78839, -93.8125, -158.1875, -95.21161, -158.1875, -96.9375);
((GeneralPath)shape).curveTo(-158.1875, -98.66339, -156.78839, -100.0625, -155.0625, -100.0625);
((GeneralPath)shape).curveTo(-153.33661, -100.0625, -151.9375, -98.66339, -151.9375, -96.9375);
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
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 38.00259017944336f, 162.0f));
// _0_0_0_8 is CompositeGraphicsNode
float alpha__0_0_0_8_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_8_0 is ShapeNode
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
origAlpha = alpha__0_0_0_8_0;
g.setTransform(defaultTransform__0_0_0_8_0);
g.setClip(clip__0_0_0_8_0);
float alpha__0_0_0_8_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_8_1 is ShapeNode
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
origAlpha = alpha__0_0_0_8_1;
g.setTransform(defaultTransform__0_0_0_8_1);
g.setClip(clip__0_0_0_8_1);
float alpha__0_0_0_8_2 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_2 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_8_2 is CompositeGraphicsNode
float alpha__0_0_0_8_2_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_2_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_2_0 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -24.198179244995117f, 21.863309860229492f));
// _0_0_0_8_2_0 is ShapeNode
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
origAlpha = alpha__0_0_0_8_2_0;
g.setTransform(defaultTransform__0_0_0_8_2_0);
g.setClip(clip__0_0_0_8_2_0);
float alpha__0_0_0_8_2_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_2_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_2_1 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -24.198179244995117f, 21.863309860229492f));
// _0_0_0_8_2_1 is ShapeNode
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
origAlpha = alpha__0_0_0_8_2_1;
g.setTransform(defaultTransform__0_0_0_8_2_1);
g.setClip(clip__0_0_0_8_2_1);
origAlpha = alpha__0_0_0_8_2;
g.setTransform(defaultTransform__0_0_0_8_2);
g.setClip(clip__0_0_0_8_2);
float alpha__0_0_0_8_3 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_3 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_8_3 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new Rectangle2D.Double(271.0, -438.0, 20.0, 9.0);
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_8_3;
g.setTransform(defaultTransform__0_0_0_8_3);
g.setClip(clip__0_0_0_8_3);
float alpha__0_0_0_8_4 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_4 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_4 = g.getTransform();
g.transform(new AffineTransform(0.9056599736213684f, 0.0f, 0.0f, 0.9056599736213684f, 9.830195426940918f, -35.688690185546875f));
// _0_0_0_8_4 is ShapeNode
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
origAlpha = alpha__0_0_0_8_4;
g.setTransform(defaultTransform__0_0_0_8_4);
g.setClip(clip__0_0_0_8_4);
float alpha__0_0_0_8_5 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_5 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_8_5 is CompositeGraphicsNode
float alpha__0_0_0_8_5_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_5_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_5_0 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -17.198110580444336f, 24.863210678100586f));
// _0_0_0_8_5_0 is ShapeNode
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
origAlpha = alpha__0_0_0_8_5_0;
g.setTransform(defaultTransform__0_0_0_8_5_0);
g.setClip(clip__0_0_0_8_5_0);
float alpha__0_0_0_8_5_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_5_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_5_1 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -17.198179244995117f, 24.863309860229492f));
// _0_0_0_8_5_1 is ShapeNode
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
origAlpha = alpha__0_0_0_8_5_1;
g.setTransform(defaultTransform__0_0_0_8_5_1);
g.setClip(clip__0_0_0_8_5_1);
origAlpha = alpha__0_0_0_8_5;
g.setTransform(defaultTransform__0_0_0_8_5);
g.setClip(clip__0_0_0_8_5);
float alpha__0_0_0_8_6 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_6 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_6 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_8_6 is CompositeGraphicsNode
float alpha__0_0_0_8_6_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_6_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_6_0 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -31.198179244995117f, 24.863309860229492f));
// _0_0_0_8_6_0 is ShapeNode
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
origAlpha = alpha__0_0_0_8_6_0;
g.setTransform(defaultTransform__0_0_0_8_6_0);
g.setClip(clip__0_0_0_8_6_0);
float alpha__0_0_0_8_6_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_6_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_6_1 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -31.198179244995117f, 24.863309860229492f));
// _0_0_0_8_6_1 is ShapeNode
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
origAlpha = alpha__0_0_0_8_6_1;
g.setTransform(defaultTransform__0_0_0_8_6_1);
g.setClip(clip__0_0_0_8_6_1);
origAlpha = alpha__0_0_0_8_6;
g.setTransform(defaultTransform__0_0_0_8_6);
g.setClip(clip__0_0_0_8_6);
float alpha__0_0_0_8_7 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_7 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_7 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 0.0f));
// _0_0_0_8_7 is CompositeGraphicsNode
float alpha__0_0_0_8_7_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_7_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_7_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_8_7_0 is ShapeNode
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
origAlpha = alpha__0_0_0_8_7_0;
g.setTransform(defaultTransform__0_0_0_8_7_0);
g.setClip(clip__0_0_0_8_7_0);
float alpha__0_0_0_8_7_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_7_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_7_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_8_7_1 is ShapeNode
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
origAlpha = alpha__0_0_0_8_7_1;
g.setTransform(defaultTransform__0_0_0_8_7_1);
g.setClip(clip__0_0_0_8_7_1);
origAlpha = alpha__0_0_0_8_7;
g.setTransform(defaultTransform__0_0_0_8_7);
g.setClip(clip__0_0_0_8_7);
float alpha__0_0_0_8_8 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_8 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_8 = g.getTransform();
g.transform(new AffineTransform(0.9056599736213684f, 0.0f, 0.0f, 0.9056599736213684f, 9.83029556274414f, -35.688838958740234f));
// _0_0_0_8_8 is ShapeNode
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
origAlpha = alpha__0_0_0_8_8;
g.setTransform(defaultTransform__0_0_0_8_8);
g.setClip(clip__0_0_0_8_8);
float alpha__0_0_0_8_9 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_9 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_9 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_8_9 is ShapeNode
paint = new Color(136, 138, 133, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(292.9564, -437.33395);
((GeneralPath)shape).curveTo(292.95486, -434.6494, 289.68713, -433.62, 289.68713, -433.62);
((GeneralPath)shape).curveTo(289.68713, -433.62, 292.0359, -435.24597, 292.024, -437.325);
((GeneralPath)shape).curveTo(292.024, -437.325, 292.9564, -437.33395, 292.9564, -437.33395);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_8_9;
g.setTransform(defaultTransform__0_0_0_8_9);
g.setClip(clip__0_0_0_8_9);
float alpha__0_0_0_8_10 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_10 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_10 = g.getTransform();
g.transform(new AffineTransform(1.1428569555282593f, 0.0f, 0.0f, 1.1428569555282593f, -28.57139015197754f, 67.00007629394531f));
// _0_0_0_8_10 is CompositeGraphicsNode
float alpha__0_0_0_8_10_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_10_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_10_0 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -31.198179244995117f, 24.863309860229492f));
// _0_0_0_8_10_0 is ShapeNode
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
origAlpha = alpha__0_0_0_8_10_0;
g.setTransform(defaultTransform__0_0_0_8_10_0);
g.setClip(clip__0_0_0_8_10_0);
float alpha__0_0_0_8_10_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_8_10_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_8_10_1 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -31.198179244995117f, 24.863309860229492f));
// _0_0_0_8_10_1 is ShapeNode
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
origAlpha = alpha__0_0_0_8_10_1;
g.setTransform(defaultTransform__0_0_0_8_10_1);
g.setClip(clip__0_0_0_8_10_1);
origAlpha = alpha__0_0_0_8_10;
g.setTransform(defaultTransform__0_0_0_8_10);
g.setClip(clip__0_0_0_8_10);
origAlpha = alpha__0_0_0_8;
g.setTransform(defaultTransform__0_0_0_8);
g.setClip(clip__0_0_0_8);
float alpha__0_0_0_9 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9 = g.getClip();
AffineTransform defaultTransform__0_0_0_9 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 23.0f, 158.0f));
// _0_0_0_9 is CompositeGraphicsNode
float alpha__0_0_0_9_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_9_0 is ShapeNode
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
origAlpha = alpha__0_0_0_9_0;
g.setTransform(defaultTransform__0_0_0_9_0);
g.setClip(clip__0_0_0_9_0);
float alpha__0_0_0_9_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_9_1 is ShapeNode
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
origAlpha = alpha__0_0_0_9_1;
g.setTransform(defaultTransform__0_0_0_9_1);
g.setClip(clip__0_0_0_9_1);
float alpha__0_0_0_9_2 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_2 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_9_2 is CompositeGraphicsNode
float alpha__0_0_0_9_2_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_2_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_2_0 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -24.198179244995117f, 21.863309860229492f));
// _0_0_0_9_2_0 is ShapeNode
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
origAlpha = alpha__0_0_0_9_2_0;
g.setTransform(defaultTransform__0_0_0_9_2_0);
g.setClip(clip__0_0_0_9_2_0);
float alpha__0_0_0_9_2_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_2_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_2_1 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -24.198179244995117f, 21.863309860229492f));
// _0_0_0_9_2_1 is ShapeNode
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
origAlpha = alpha__0_0_0_9_2_1;
g.setTransform(defaultTransform__0_0_0_9_2_1);
g.setClip(clip__0_0_0_9_2_1);
origAlpha = alpha__0_0_0_9_2;
g.setTransform(defaultTransform__0_0_0_9_2);
g.setClip(clip__0_0_0_9_2);
float alpha__0_0_0_9_3 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_3 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_9_3 is ShapeNode
paint = new Color(196, 197, 194, 255);
shape = new Rectangle2D.Double(271.0, -438.0, 20.0, 9.0);
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_9_3;
g.setTransform(defaultTransform__0_0_0_9_3);
g.setClip(clip__0_0_0_9_3);
float alpha__0_0_0_9_4 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_4 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_4 = g.getTransform();
g.transform(new AffineTransform(0.9056599736213684f, 0.0f, 0.0f, 0.9056599736213684f, 9.830195426940918f, -35.688690185546875f));
// _0_0_0_9_4 is ShapeNode
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
origAlpha = alpha__0_0_0_9_4;
g.setTransform(defaultTransform__0_0_0_9_4);
g.setClip(clip__0_0_0_9_4);
float alpha__0_0_0_9_5 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_5 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_9_5 is CompositeGraphicsNode
float alpha__0_0_0_9_5_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_5_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_5_0 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -17.198110580444336f, 24.863210678100586f));
// _0_0_0_9_5_0 is ShapeNode
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
origAlpha = alpha__0_0_0_9_5_0;
g.setTransform(defaultTransform__0_0_0_9_5_0);
g.setClip(clip__0_0_0_9_5_0);
float alpha__0_0_0_9_5_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_5_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_5_1 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -17.198179244995117f, 24.863309860229492f));
// _0_0_0_9_5_1 is ShapeNode
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
origAlpha = alpha__0_0_0_9_5_1;
g.setTransform(defaultTransform__0_0_0_9_5_1);
g.setClip(clip__0_0_0_9_5_1);
origAlpha = alpha__0_0_0_9_5;
g.setTransform(defaultTransform__0_0_0_9_5);
g.setClip(clip__0_0_0_9_5);
float alpha__0_0_0_9_6 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_6 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_6 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_9_6 is CompositeGraphicsNode
float alpha__0_0_0_9_6_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_6_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_6_0 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -31.198179244995117f, 24.863309860229492f));
// _0_0_0_9_6_0 is ShapeNode
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
origAlpha = alpha__0_0_0_9_6_0;
g.setTransform(defaultTransform__0_0_0_9_6_0);
g.setClip(clip__0_0_0_9_6_0);
float alpha__0_0_0_9_6_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_6_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_6_1 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -31.198179244995117f, 24.863309860229492f));
// _0_0_0_9_6_1 is ShapeNode
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
origAlpha = alpha__0_0_0_9_6_1;
g.setTransform(defaultTransform__0_0_0_9_6_1);
g.setClip(clip__0_0_0_9_6_1);
origAlpha = alpha__0_0_0_9_6;
g.setTransform(defaultTransform__0_0_0_9_6);
g.setClip(clip__0_0_0_9_6);
float alpha__0_0_0_9_7 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_7 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_7 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 0.0f));
// _0_0_0_9_7 is CompositeGraphicsNode
float alpha__0_0_0_9_7_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_7_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_7_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_9_7_0 is ShapeNode
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
origAlpha = alpha__0_0_0_9_7_0;
g.setTransform(defaultTransform__0_0_0_9_7_0);
g.setClip(clip__0_0_0_9_7_0);
float alpha__0_0_0_9_7_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_7_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_7_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_9_7_1 is ShapeNode
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
origAlpha = alpha__0_0_0_9_7_1;
g.setTransform(defaultTransform__0_0_0_9_7_1);
g.setClip(clip__0_0_0_9_7_1);
origAlpha = alpha__0_0_0_9_7;
g.setTransform(defaultTransform__0_0_0_9_7);
g.setClip(clip__0_0_0_9_7);
float alpha__0_0_0_9_8 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_8 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_8 = g.getTransform();
g.transform(new AffineTransform(0.9056599736213684f, 0.0f, 0.0f, 0.9056599736213684f, 9.83029556274414f, -35.688838958740234f));
// _0_0_0_9_8 is ShapeNode
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
origAlpha = alpha__0_0_0_9_8;
g.setTransform(defaultTransform__0_0_0_9_8);
g.setClip(clip__0_0_0_9_8);
float alpha__0_0_0_9_9 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_9 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_9 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_9_9 is ShapeNode
paint = new Color(136, 138, 133, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(292.9564, -437.33395);
((GeneralPath)shape).curveTo(292.95486, -434.6494, 289.68713, -433.62, 289.68713, -433.62);
((GeneralPath)shape).curveTo(289.68713, -433.62, 292.0359, -435.24597, 292.024, -437.325);
((GeneralPath)shape).curveTo(292.024, -437.325, 292.9564, -437.33395, 292.9564, -437.33395);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_9_9;
g.setTransform(defaultTransform__0_0_0_9_9);
g.setClip(clip__0_0_0_9_9);
float alpha__0_0_0_9_10 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_10 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_10 = g.getTransform();
g.transform(new AffineTransform(1.1428569555282593f, 0.0f, 0.0f, 1.1428569555282593f, -28.57139015197754f, 67.00007629394531f));
// _0_0_0_9_10 is CompositeGraphicsNode
float alpha__0_0_0_9_10_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_10_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_10_0 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -31.198179244995117f, 24.863309860229492f));
// _0_0_0_9_10_0 is ShapeNode
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
origAlpha = alpha__0_0_0_9_10_0;
g.setTransform(defaultTransform__0_0_0_9_10_0);
g.setClip(clip__0_0_0_9_10_0);
float alpha__0_0_0_9_10_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_9_10_1 = g.getClip();
AffineTransform defaultTransform__0_0_0_9_10_1 = g.getTransform();
g.transform(new AffineTransform(1.056604027748108f, 0.0f, 0.0f, 1.056604027748108f, -31.198179244995117f, 24.863309860229492f));
// _0_0_0_9_10_1 is ShapeNode
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
origAlpha = alpha__0_0_0_9_10_1;
g.setTransform(defaultTransform__0_0_0_9_10_1);
g.setClip(clip__0_0_0_9_10_1);
origAlpha = alpha__0_0_0_9_10;
g.setTransform(defaultTransform__0_0_0_9_10);
g.setClip(clip__0_0_0_9_10);
origAlpha = alpha__0_0_0_9;
g.setTransform(defaultTransform__0_0_0_9);
g.setClip(clip__0_0_0_9);
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
	public OvercastIcon() {
        this.width = getOrigWidth();
        this.height = getOrigHeight();
	}
	
	/**
	 * Creates a new transcoded SVG image with the given dimensions.
	 *
	 * @param size the dimensions of the icon
	 */
	public OvercastIcon(Dimension size) {
	this.width = size.width;
	this.height = size.width;
	}

	public OvercastIcon(int width, int height) {
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

