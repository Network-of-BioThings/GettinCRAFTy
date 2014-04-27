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
public class TermRaiderAppIcon implements
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
clip.intersect(new Area(new Rectangle2D.Double(0.0,0.0,64.0,64.0)));
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
g.transform(new AffineTransform(0.5379909873008728f, 0.0f, 0.0f, 0.530135989189148f, 17.2419490814209f, 12.596540451049805f));
// _0_0_0 is CompositeGraphicsNode
float alpha__0_0_0_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0_0 = g.getClip();
AffineTransform defaultTransform__0_0_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_0 is ShapeNode
paint = new Color(255, 97, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(4.042755, -19.365057);
((GeneralPath)shape).curveTo(-7.8215437, -17.997686, 9.529157, -10.300208, -0.89474475, -4.615057);
((GeneralPath)shape).curveTo(-11.318646, 1.0700942, -8.966936, -17.377476, -16.363495, -8.240057);
((GeneralPath)shape).curveTo(-23.760054, 0.8973612, -5.9015875, -5.6261516, -9.144745, 5.572443);
((GeneralPath)shape).curveTo(-12.387902, 16.771038, -24.11123, 2.111303, -22.707245, 13.666193);
((GeneralPath)shape).curveTo(-21.303259, 25.221083, -13.419629, 8.326636, -7.582245, 18.478693);
((GeneralPath)shape).curveTo(-1.7448606, 28.630749, -20.683088, 26.36878, -11.300995, 33.57244);
((GeneralPath)shape).curveTo(-1.9189007, 40.776108, -8.58071, 23.351362, 2.9177554, 26.509943);
((GeneralPath)shape).curveTo(14.41622, 29.668522, -0.63404477, 41.096066, 11.230255, 39.72869);
((GeneralPath)shape).curveTo(23.094555, 38.36132, 5.7126045, 30.663845, 16.136505, 24.978693);
((GeneralPath)shape).curveTo(26.560406, 19.293543, 24.239948, 37.741108, 31.636505, 28.603693);
((GeneralPath)shape).curveTo(39.033062, 19.466274, 21.143349, 25.98979, 24.386505, 14.791193);
((GeneralPath)shape).curveTo(27.629662, 3.5925972, 39.38424, 18.252333, 37.980255, 6.697443);
((GeneralPath)shape).curveTo(36.576267, -4.8574457, 28.66139, 12.036998, 22.824005, 1.8849432);
((GeneralPath)shape).curveTo(16.986622, -8.267112, 35.924847, -5.9738936, 26.542755, -13.177557);
((GeneralPath)shape).curveTo(17.160662, -20.38122, 23.85372, -2.9877279, 12.355255, -6.146307);
((GeneralPath)shape).curveTo(0.85679024, -9.304885, 15.907053, -20.732428, 4.042755, -19.365057);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(7.7927556, -1.1150568);
((GeneralPath)shape).curveTo(13.463302, -1.1150568, 18.074005, 3.769375, 18.074005, 9.791193);
((GeneralPath)shape).curveTo(18.074005, 15.81301, 13.4633, 20.697443, 7.7927556, 20.697443);
((GeneralPath)shape).curveTo(2.1222112, 20.697443, -2.4884946, 15.813011, -2.4884946, 9.791193);
((GeneralPath)shape).curveTo(-2.4884946, 3.7693741, 2.1222103, -1.1150568, 7.7927556, -1.1150568);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(1.013f,0,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(4.042755, -19.365057);
((GeneralPath)shape).curveTo(-7.8215437, -17.997686, 9.529157, -10.300208, -0.89474475, -4.615057);
((GeneralPath)shape).curveTo(-11.318646, 1.0700942, -8.966936, -17.377476, -16.363495, -8.240057);
((GeneralPath)shape).curveTo(-23.760054, 0.8973612, -5.9015875, -5.6261516, -9.144745, 5.572443);
((GeneralPath)shape).curveTo(-12.387902, 16.771038, -24.11123, 2.111303, -22.707245, 13.666193);
((GeneralPath)shape).curveTo(-21.303259, 25.221083, -13.419629, 8.326636, -7.582245, 18.478693);
((GeneralPath)shape).curveTo(-1.7448606, 28.630749, -20.683088, 26.36878, -11.300995, 33.57244);
((GeneralPath)shape).curveTo(-1.9189007, 40.776108, -8.58071, 23.351362, 2.9177554, 26.509943);
((GeneralPath)shape).curveTo(14.41622, 29.668522, -0.63404477, 41.096066, 11.230255, 39.72869);
((GeneralPath)shape).curveTo(23.094555, 38.36132, 5.7126045, 30.663845, 16.136505, 24.978693);
((GeneralPath)shape).curveTo(26.560406, 19.293543, 24.239948, 37.741108, 31.636505, 28.603693);
((GeneralPath)shape).curveTo(39.033062, 19.466274, 21.143349, 25.98979, 24.386505, 14.791193);
((GeneralPath)shape).curveTo(27.629662, 3.5925972, 39.38424, 18.252333, 37.980255, 6.697443);
((GeneralPath)shape).curveTo(36.576267, -4.8574457, 28.66139, 12.036998, 22.824005, 1.8849432);
((GeneralPath)shape).curveTo(16.986622, -8.267112, 35.924847, -5.9738936, 26.542755, -13.177557);
((GeneralPath)shape).curveTo(17.160662, -20.38122, 23.85372, -2.9877279, 12.355255, -6.146307);
((GeneralPath)shape).curveTo(0.85679024, -9.304885, 15.907053, -20.732428, 4.042755, -19.365057);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(7.7927556, -1.1150568);
((GeneralPath)shape).curveTo(13.463302, -1.1150568, 18.074005, 3.769375, 18.074005, 9.791193);
((GeneralPath)shape).curveTo(18.074005, 15.81301, 13.4633, 20.697443, 7.7927556, 20.697443);
((GeneralPath)shape).curveTo(2.1222112, 20.697443, -2.4884946, 15.813011, -2.4884946, 9.791193);
((GeneralPath)shape).curveTo(-2.4884946, 3.7693741, 2.1222103, -1.1150568, 7.7927556, -1.1150568);
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
paint = new LinearGradientPaint(new Point2D.Double(41.272727966308594, 20.0), new Point2D.Double(9.090909004211426, 59.73617935180664), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 173, 93, 240),new Color(255, 173, 93, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.8587690591812134f, 0.0f, 0.0f, 1.886309027671814f, -32.048789978027344f, -23.760940551757812f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(1.8552552, -16.615057);
((GeneralPath)shape).curveTo(4.3268423, -12.389599, 6.242616, -5.570594, 0.26150525, -2.8650568);
((GeneralPath)shape).curveTo(-5.1689677, 1.0453773, -10.645211, -3.5740318, -12.988495, -8.458807);
((GeneralPath)shape).curveTo(-19.622936, -4.5127587, -12.27151, -3.6567068, -8.957245, -1.5213068);
((GeneralPath)shape).curveTo(-3.2207208, 4.286322, -10.468309, 15.572569, -18.207245, 11.603693);
((GeneralPath)shape).curveTo(-23.99832, 10.346797, -19.449268, 19.469612, -15.488495, 13.916193);
((GeneralPath)shape).curveTo(-6.724279, 9.93738, 0.04226026, 25.43532, -9.401818, 28.759712);
((GeneralPath)shape).curveTo(-15.546361, 29.988605, -5.8175397, 36.645508, -6.6217256, 30.13041);
((GeneralPath)shape).curveTo(-4.8541536, 20.229397, 12.328064, 24.536419, 9.105716, 34.04833);
((GeneralPath)shape).curveTo(5.100879, 40.209793, 17.702448, 38.351902, 11.460828, 33.28359);
((GeneralPath)shape).curveTo(6.6353965, 24.179485, 23.370386, 17.270996, 26.605255, 27.197443);
((GeneralPath)shape).curveTo(30.13475, 31.97409, 33.165424, 22.257687, 26.011505, 23.541193);
((GeneralPath)shape).curveTo(17.15425, 19.561945, 24.492996, 4.430459, 33.261505, 9.134943);
((GeneralPath)shape).curveTo(39.271023, 10.077248, 34.20037, 1.1481862, 30.105255, 6.9786935);
((GeneralPath)shape).curveTo(21.087711, 10.299201, 15.245074, -5.318059, 24.886505, -8.302557);
((GeneralPath)shape).curveTo(29.8154, -11.232163, 20.011862, -14.728683, 21.199005, -8.052557);
((GeneralPath)shape).curveTo(17.609556, 1.1128032, 1.6049923, -5.5786176, 6.292755, -14.740057);
((GeneralPath)shape).curveTo(8.431012, -18.796041, 3.7487464, -16.879204, 1.8552552, -16.615057);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(8.105255, -2.8963068);
((GeneralPath)shape).curveTo(20.221607, -2.7038107, 24.260914, 15.523604, 13.660882, 21.061708);
((GeneralPath)shape).curveTo(4.0272064, 27.488108, -8.142912, 15.464196, -3.7384946, 5.353693);
((GeneralPath)shape).curveTo(-1.8222617, 0.4545682, 2.7822824, -2.9495928, 8.105255, -2.8963068);
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
g.transform(new AffineTransform(0.8440920114517212f, 1.037574052810669f, -1.071671962738037f, 0.8172360062599182f, 14.446829795837402f, -50.05820083618164f));
// _0_0_0_2 is ShapeNode
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(35.454544, 31.636364);
((GeneralPath)shape).curveTo(35.454544, 33.343426, 34.111397, 34.727272, 32.454544, 34.727272);
((GeneralPath)shape).curveTo(30.79769, 34.727272, 29.454544, 33.343426, 29.454544, 31.636364);
((GeneralPath)shape).curveTo(29.454544, 29.929302, 30.79769, 28.545456, 32.454544, 28.545456);
((GeneralPath)shape).curveTo(34.111397, 28.545456, 35.454544, 29.929302, 35.454544, 31.636364);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0_2;
g.setTransform(defaultTransform__0_0_0_2);
g.setClip(clip__0_0_0_2);
origAlpha = alpha__0_0_0;
g.setTransform(defaultTransform__0_0_0);
g.setClip(clip__0_0_0);
float alpha__0_0_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1 = g.getClip();
AffineTransform defaultTransform__0_0_1 = g.getTransform();
g.transform(new AffineTransform(0.5379909873008728f, 0.0f, 0.0f, 0.530135989189148f, 43.549278259277344f, 25.596540451049805f));
// _0_0_1 is CompositeGraphicsNode
float alpha__0_0_1_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_0 = g.getClip();
AffineTransform defaultTransform__0_0_1_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_0 is ShapeNode
paint = new Color(255, 97, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(4.042755, -19.365057);
((GeneralPath)shape).curveTo(-7.8215437, -17.997686, 9.529157, -10.300208, -0.89474475, -4.615057);
((GeneralPath)shape).curveTo(-11.318646, 1.0700942, -8.966936, -17.377476, -16.363495, -8.240057);
((GeneralPath)shape).curveTo(-23.760054, 0.8973612, -5.9015875, -5.6261516, -9.144745, 5.572443);
((GeneralPath)shape).curveTo(-12.387902, 16.771038, -24.11123, 2.111303, -22.707245, 13.666193);
((GeneralPath)shape).curveTo(-21.303259, 25.221083, -13.419629, 8.326636, -7.582245, 18.478693);
((GeneralPath)shape).curveTo(-1.7448606, 28.630749, -20.683088, 26.36878, -11.300995, 33.57244);
((GeneralPath)shape).curveTo(-1.9189007, 40.776108, -8.58071, 23.351362, 2.9177554, 26.509943);
((GeneralPath)shape).curveTo(14.41622, 29.668522, -0.63404477, 41.096066, 11.230255, 39.72869);
((GeneralPath)shape).curveTo(23.094555, 38.36132, 5.7126045, 30.663845, 16.136505, 24.978693);
((GeneralPath)shape).curveTo(26.560406, 19.293543, 24.239948, 37.741108, 31.636505, 28.603693);
((GeneralPath)shape).curveTo(39.033062, 19.466274, 21.143349, 25.98979, 24.386505, 14.791193);
((GeneralPath)shape).curveTo(27.629662, 3.5925972, 39.38424, 18.252333, 37.980255, 6.697443);
((GeneralPath)shape).curveTo(36.576267, -4.8574457, 28.66139, 12.036998, 22.824005, 1.8849432);
((GeneralPath)shape).curveTo(16.986622, -8.267112, 35.924847, -5.9738936, 26.542755, -13.177557);
((GeneralPath)shape).curveTo(17.160662, -20.38122, 23.85372, -2.9877279, 12.355255, -6.146307);
((GeneralPath)shape).curveTo(0.85679024, -9.304885, 15.907053, -20.732428, 4.042755, -19.365057);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(7.7927556, -1.1150568);
((GeneralPath)shape).curveTo(13.463302, -1.1150568, 18.074005, 3.769375, 18.074005, 9.791193);
((GeneralPath)shape).curveTo(18.074005, 15.81301, 13.4633, 20.697443, 7.7927556, 20.697443);
((GeneralPath)shape).curveTo(2.1222112, 20.697443, -2.4884946, 15.813011, -2.4884946, 9.791193);
((GeneralPath)shape).curveTo(-2.4884946, 3.7693741, 2.1222103, -1.1150568, 7.7927556, -1.1150568);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(1.013f,0,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(4.042755, -19.365057);
((GeneralPath)shape).curveTo(-7.8215437, -17.997686, 9.529157, -10.300208, -0.89474475, -4.615057);
((GeneralPath)shape).curveTo(-11.318646, 1.0700942, -8.966936, -17.377476, -16.363495, -8.240057);
((GeneralPath)shape).curveTo(-23.760054, 0.8973612, -5.9015875, -5.6261516, -9.144745, 5.572443);
((GeneralPath)shape).curveTo(-12.387902, 16.771038, -24.11123, 2.111303, -22.707245, 13.666193);
((GeneralPath)shape).curveTo(-21.303259, 25.221083, -13.419629, 8.326636, -7.582245, 18.478693);
((GeneralPath)shape).curveTo(-1.7448606, 28.630749, -20.683088, 26.36878, -11.300995, 33.57244);
((GeneralPath)shape).curveTo(-1.9189007, 40.776108, -8.58071, 23.351362, 2.9177554, 26.509943);
((GeneralPath)shape).curveTo(14.41622, 29.668522, -0.63404477, 41.096066, 11.230255, 39.72869);
((GeneralPath)shape).curveTo(23.094555, 38.36132, 5.7126045, 30.663845, 16.136505, 24.978693);
((GeneralPath)shape).curveTo(26.560406, 19.293543, 24.239948, 37.741108, 31.636505, 28.603693);
((GeneralPath)shape).curveTo(39.033062, 19.466274, 21.143349, 25.98979, 24.386505, 14.791193);
((GeneralPath)shape).curveTo(27.629662, 3.5925972, 39.38424, 18.252333, 37.980255, 6.697443);
((GeneralPath)shape).curveTo(36.576267, -4.8574457, 28.66139, 12.036998, 22.824005, 1.8849432);
((GeneralPath)shape).curveTo(16.986622, -8.267112, 35.924847, -5.9738936, 26.542755, -13.177557);
((GeneralPath)shape).curveTo(17.160662, -20.38122, 23.85372, -2.9877279, 12.355255, -6.146307);
((GeneralPath)shape).curveTo(0.85679024, -9.304885, 15.907053, -20.732428, 4.042755, -19.365057);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(7.7927556, -1.1150568);
((GeneralPath)shape).curveTo(13.463302, -1.1150568, 18.074005, 3.769375, 18.074005, 9.791193);
((GeneralPath)shape).curveTo(18.074005, 15.81301, 13.4633, 20.697443, 7.7927556, 20.697443);
((GeneralPath)shape).curveTo(2.1222112, 20.697443, -2.4884946, 15.813011, -2.4884946, 9.791193);
((GeneralPath)shape).curveTo(-2.4884946, 3.7693741, 2.1222103, -1.1150568, 7.7927556, -1.1150568);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
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
paint = new LinearGradientPaint(new Point2D.Double(41.272727966308594, 20.0), new Point2D.Double(9.090909004211426, 59.73617935180664), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 173, 93, 240),new Color(255, 173, 93, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.8587690591812134f, 0.0f, 0.0f, 1.886309027671814f, -80.94799041748047f, -48.28293991088867f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(1.8552552, -16.615057);
((GeneralPath)shape).curveTo(4.3268423, -12.389599, 6.242616, -5.570594, 0.26150525, -2.8650568);
((GeneralPath)shape).curveTo(-5.1689677, 1.0453773, -10.645211, -3.5740318, -12.988495, -8.458807);
((GeneralPath)shape).curveTo(-19.622936, -4.5127587, -12.27151, -3.6567068, -8.957245, -1.5213068);
((GeneralPath)shape).curveTo(-3.2207208, 4.286322, -10.468309, 15.572569, -18.207245, 11.603693);
((GeneralPath)shape).curveTo(-23.99832, 10.346797, -19.449268, 19.469612, -15.488495, 13.916193);
((GeneralPath)shape).curveTo(-6.724279, 9.93738, 0.04226026, 25.43532, -9.401818, 28.759712);
((GeneralPath)shape).curveTo(-15.546361, 29.988605, -5.8175397, 36.645508, -6.6217256, 30.13041);
((GeneralPath)shape).curveTo(-4.8541536, 20.229397, 12.328064, 24.536419, 9.105716, 34.04833);
((GeneralPath)shape).curveTo(5.100879, 40.209793, 17.702448, 38.351902, 11.460828, 33.28359);
((GeneralPath)shape).curveTo(6.6353965, 24.179485, 23.370386, 17.270996, 26.605255, 27.197443);
((GeneralPath)shape).curveTo(30.13475, 31.97409, 33.165424, 22.257687, 26.011505, 23.541193);
((GeneralPath)shape).curveTo(17.15425, 19.561945, 24.492996, 4.430459, 33.261505, 9.134943);
((GeneralPath)shape).curveTo(39.271023, 10.077248, 34.20037, 1.1481862, 30.105255, 6.9786935);
((GeneralPath)shape).curveTo(21.087711, 10.299201, 15.245074, -5.318059, 24.886505, -8.302557);
((GeneralPath)shape).curveTo(29.8154, -11.232163, 20.011862, -14.728683, 21.199005, -8.052557);
((GeneralPath)shape).curveTo(17.609556, 1.1128032, 1.6049923, -5.5786176, 6.292755, -14.740057);
((GeneralPath)shape).curveTo(8.431012, -18.796041, 3.7487464, -16.879204, 1.8552552, -16.615057);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(8.105255, -2.8963068);
((GeneralPath)shape).curveTo(20.221607, -2.7038107, 24.260914, 15.523604, 13.660882, 21.061708);
((GeneralPath)shape).curveTo(4.0272064, 27.488108, -8.142912, 15.464196, -3.7384946, 5.353693);
((GeneralPath)shape).curveTo(-1.8222617, 0.4545682, 2.7822824, -2.9495928, 8.105255, -2.8963068);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_1_1;
g.setTransform(defaultTransform__0_0_1_1);
g.setClip(clip__0_0_1_1);
float alpha__0_0_1_2 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_1_2 = g.getClip();
AffineTransform defaultTransform__0_0_1_2 = g.getTransform();
g.transform(new AffineTransform(0.8440920114517212f, 1.037574052810669f, -1.071671962738037f, 0.8172360062599182f, 14.446829795837402f, -50.05820083618164f));
// _0_0_1_2 is ShapeNode
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(35.454544, 31.636364);
((GeneralPath)shape).curveTo(35.454544, 33.343426, 34.111397, 34.727272, 32.454544, 34.727272);
((GeneralPath)shape).curveTo(30.79769, 34.727272, 29.454544, 33.343426, 29.454544, 31.636364);
((GeneralPath)shape).curveTo(29.454544, 29.929302, 30.79769, 28.545456, 32.454544, 28.545456);
((GeneralPath)shape).curveTo(34.111397, 28.545456, 35.454544, 29.929302, 35.454544, 31.636364);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_1_2;
g.setTransform(defaultTransform__0_0_1_2);
g.setClip(clip__0_0_1_2);
origAlpha = alpha__0_0_1;
g.setTransform(defaultTransform__0_0_1);
g.setClip(clip__0_0_1);
float alpha__0_0_2 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2 = g.getClip();
AffineTransform defaultTransform__0_0_2 = g.getTransform();
g.transform(new AffineTransform(0.5252659916877747f, 0.11632200330495834f, -0.11462400108575821f, 0.5175960063934326f, 13.507800102233887f, 39.80751037597656f));
// _0_0_2 is CompositeGraphicsNode
float alpha__0_0_2_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_0 = g.getClip();
AffineTransform defaultTransform__0_0_2_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2_0 is ShapeNode
paint = new Color(255, 97, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(4.042755, -19.365057);
((GeneralPath)shape).curveTo(-7.8215437, -17.997686, 9.529157, -10.300208, -0.89474475, -4.615057);
((GeneralPath)shape).curveTo(-11.318646, 1.0700942, -8.966936, -17.377476, -16.363495, -8.240057);
((GeneralPath)shape).curveTo(-23.760054, 0.8973612, -5.9015875, -5.6261516, -9.144745, 5.572443);
((GeneralPath)shape).curveTo(-12.387902, 16.771038, -24.11123, 2.111303, -22.707245, 13.666193);
((GeneralPath)shape).curveTo(-21.303259, 25.221083, -13.419629, 8.326636, -7.582245, 18.478693);
((GeneralPath)shape).curveTo(-1.7448606, 28.630749, -20.683088, 26.36878, -11.300995, 33.57244);
((GeneralPath)shape).curveTo(-1.9189007, 40.776108, -8.58071, 23.351362, 2.9177554, 26.509943);
((GeneralPath)shape).curveTo(14.41622, 29.668522, -0.63404477, 41.096066, 11.230255, 39.72869);
((GeneralPath)shape).curveTo(23.094555, 38.36132, 5.7126045, 30.663845, 16.136505, 24.978693);
((GeneralPath)shape).curveTo(26.560406, 19.293543, 24.239948, 37.741108, 31.636505, 28.603693);
((GeneralPath)shape).curveTo(39.033062, 19.466274, 21.143349, 25.98979, 24.386505, 14.791193);
((GeneralPath)shape).curveTo(27.629662, 3.5925972, 39.38424, 18.252333, 37.980255, 6.697443);
((GeneralPath)shape).curveTo(36.576267, -4.8574457, 28.66139, 12.036998, 22.824005, 1.8849432);
((GeneralPath)shape).curveTo(16.986622, -8.267112, 35.924847, -5.9738936, 26.542755, -13.177557);
((GeneralPath)shape).curveTo(17.160662, -20.38122, 23.85372, -2.9877279, 12.355255, -6.146307);
((GeneralPath)shape).curveTo(0.85679024, -9.304885, 15.907053, -20.732428, 4.042755, -19.365057);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(7.7927556, -1.1150568);
((GeneralPath)shape).curveTo(13.463302, -1.1150568, 18.074005, 3.769375, 18.074005, 9.791193);
((GeneralPath)shape).curveTo(18.074005, 15.81301, 13.4633, 20.697443, 7.7927556, 20.697443);
((GeneralPath)shape).curveTo(2.1222112, 20.697443, -2.4884946, 15.813011, -2.4884946, 9.791193);
((GeneralPath)shape).curveTo(-2.4884946, 3.7693741, 2.1222103, -1.1150568, 7.7927556, -1.1150568);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(1.013f,0,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(4.042755, -19.365057);
((GeneralPath)shape).curveTo(-7.8215437, -17.997686, 9.529157, -10.300208, -0.89474475, -4.615057);
((GeneralPath)shape).curveTo(-11.318646, 1.0700942, -8.966936, -17.377476, -16.363495, -8.240057);
((GeneralPath)shape).curveTo(-23.760054, 0.8973612, -5.9015875, -5.6261516, -9.144745, 5.572443);
((GeneralPath)shape).curveTo(-12.387902, 16.771038, -24.11123, 2.111303, -22.707245, 13.666193);
((GeneralPath)shape).curveTo(-21.303259, 25.221083, -13.419629, 8.326636, -7.582245, 18.478693);
((GeneralPath)shape).curveTo(-1.7448606, 28.630749, -20.683088, 26.36878, -11.300995, 33.57244);
((GeneralPath)shape).curveTo(-1.9189007, 40.776108, -8.58071, 23.351362, 2.9177554, 26.509943);
((GeneralPath)shape).curveTo(14.41622, 29.668522, -0.63404477, 41.096066, 11.230255, 39.72869);
((GeneralPath)shape).curveTo(23.094555, 38.36132, 5.7126045, 30.663845, 16.136505, 24.978693);
((GeneralPath)shape).curveTo(26.560406, 19.293543, 24.239948, 37.741108, 31.636505, 28.603693);
((GeneralPath)shape).curveTo(39.033062, 19.466274, 21.143349, 25.98979, 24.386505, 14.791193);
((GeneralPath)shape).curveTo(27.629662, 3.5925972, 39.38424, 18.252333, 37.980255, 6.697443);
((GeneralPath)shape).curveTo(36.576267, -4.8574457, 28.66139, 12.036998, 22.824005, 1.8849432);
((GeneralPath)shape).curveTo(16.986622, -8.267112, 35.924847, -5.9738936, 26.542755, -13.177557);
((GeneralPath)shape).curveTo(17.160662, -20.38122, 23.85372, -2.9877279, 12.355255, -6.146307);
((GeneralPath)shape).curveTo(0.85679024, -9.304885, 15.907053, -20.732428, 4.042755, -19.365057);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(7.7927556, -1.1150568);
((GeneralPath)shape).curveTo(13.463302, -1.1150568, 18.074005, 3.769375, 18.074005, 9.791193);
((GeneralPath)shape).curveTo(18.074005, 15.81301, 13.4633, 20.697443, 7.7927556, 20.697443);
((GeneralPath)shape).curveTo(2.1222112, 20.697443, -2.4884946, 15.813011, -2.4884946, 9.791193);
((GeneralPath)shape).curveTo(-2.4884946, 3.7693741, 2.1222103, -1.1150568, 7.7927556, -1.1150568);
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
paint = new LinearGradientPaint(new Point2D.Double(41.272727966308594, 20.0), new Point2D.Double(9.090909004211426, 59.73617935180664), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 173, 93, 240),new Color(255, 173, 93, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.8148020505905151f, -0.4078499972820282f, 0.4018949866294861f, 1.8416889905929565f, -40.51237869262695f, -67.80384826660156f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(1.8552552, -16.615057);
((GeneralPath)shape).curveTo(4.3268423, -12.389599, 6.242616, -5.570594, 0.26150525, -2.8650568);
((GeneralPath)shape).curveTo(-5.1689677, 1.0453773, -10.645211, -3.5740318, -12.988495, -8.458807);
((GeneralPath)shape).curveTo(-19.622936, -4.5127587, -12.27151, -3.6567068, -8.957245, -1.5213068);
((GeneralPath)shape).curveTo(-3.2207208, 4.286322, -10.468309, 15.572569, -18.207245, 11.603693);
((GeneralPath)shape).curveTo(-23.99832, 10.346797, -19.449268, 19.469612, -15.488495, 13.916193);
((GeneralPath)shape).curveTo(-6.724279, 9.93738, 0.04226026, 25.43532, -9.401818, 28.759712);
((GeneralPath)shape).curveTo(-15.546361, 29.988605, -5.8175397, 36.645508, -6.6217256, 30.13041);
((GeneralPath)shape).curveTo(-4.8541536, 20.229397, 12.328064, 24.536419, 9.105716, 34.04833);
((GeneralPath)shape).curveTo(5.100879, 40.209793, 17.702448, 38.351902, 11.460828, 33.28359);
((GeneralPath)shape).curveTo(6.6353965, 24.179485, 23.370386, 17.270996, 26.605255, 27.197443);
((GeneralPath)shape).curveTo(30.13475, 31.97409, 33.165424, 22.257687, 26.011505, 23.541193);
((GeneralPath)shape).curveTo(17.15425, 19.561945, 24.492996, 4.430459, 33.261505, 9.134943);
((GeneralPath)shape).curveTo(39.271023, 10.077248, 34.20037, 1.1481862, 30.105255, 6.9786935);
((GeneralPath)shape).curveTo(21.087711, 10.299201, 15.245074, -5.318059, 24.886505, -8.302557);
((GeneralPath)shape).curveTo(29.8154, -11.232163, 20.011862, -14.728683, 21.199005, -8.052557);
((GeneralPath)shape).curveTo(17.609556, 1.1128032, 1.6049923, -5.5786176, 6.292755, -14.740057);
((GeneralPath)shape).curveTo(8.431012, -18.796041, 3.7487464, -16.879204, 1.8552552, -16.615057);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(8.105255, -2.8963068);
((GeneralPath)shape).curveTo(20.221607, -2.7038107, 24.260914, 15.523604, 13.660882, 21.061708);
((GeneralPath)shape).curveTo(4.0272064, 27.488108, -8.142912, 15.464196, -3.7384946, 5.353693);
((GeneralPath)shape).curveTo(-1.8222617, 0.4545682, 2.7822824, -2.9495928, 8.105255, -2.8963068);
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
g.transform(new AffineTransform(0.8440920114517212f, 1.037574052810669f, -1.071671962738037f, 0.8172360062599182f, 14.446829795837402f, -50.05820083618164f));
// _0_0_2_2 is ShapeNode
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(35.454544, 31.636364);
((GeneralPath)shape).curveTo(35.454544, 33.343426, 34.111397, 34.727272, 32.454544, 34.727272);
((GeneralPath)shape).curveTo(30.79769, 34.727272, 29.454544, 33.343426, 29.454544, 31.636364);
((GeneralPath)shape).curveTo(29.454544, 29.929302, 30.79769, 28.545456, 32.454544, 28.545456);
((GeneralPath)shape).curveTo(34.111397, 28.545456, 35.454544, 29.929302, 35.454544, 31.636364);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_2_2;
g.setTransform(defaultTransform__0_0_2_2);
g.setClip(clip__0_0_2_2);
float alpha__0_0_2_3 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_2_3 = g.getClip();
AffineTransform defaultTransform__0_0_2_3 = g.getTransform();
g.transform(new AffineTransform(1.8147963285446167f, -0.40784844756126404f, 0.4018949270248413f, 1.8416887521743774f, -40.5123405456543f, -67.80390930175781f));
// _0_0_2_3 is TextNode of ''
origAlpha = alpha__0_0_2_3;
g.setTransform(defaultTransform__0_0_2_3);
g.setClip(clip__0_0_2_3);
origAlpha = alpha__0_0_2;
g.setTransform(defaultTransform__0_0_2);
g.setClip(clip__0_0_2);
float alpha__0_0_3 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_3 = g.getClip();
AffineTransform defaultTransform__0_0_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_3 is CompositeGraphicsNode
float alpha__0_0_3_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_3_0 = g.getClip();
AffineTransform defaultTransform__0_0_3_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_3_0 is CompositeGraphicsNode
float alpha__0_0_3_0_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_3_0_0 = g.getClip();
AffineTransform defaultTransform__0_0_3_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_3_0_0 is ShapeNode
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(6.769885, 34.2429);
((GeneralPath)shape).lineTo(28.07848, 34.2429);
((GeneralPath)shape).lineTo(26.066761, 41.371807);
((GeneralPath)shape).lineTo(25.363636, 41.371807);
((GeneralPath)shape).curveTo(25.389656, 40.994225, 25.402676, 40.662193, 25.402697, 40.375713);
((GeneralPath)shape).curveTo(25.402672, 39.02157, 25.005537, 37.973392, 24.21129, 37.231182);
((GeneralPath)shape).curveTo(23.416996, 36.475998, 22.075851, 36.04631, 20.187853, 35.94212);
((GeneralPath)shape).lineTo(14.289415, 55.883526);
((GeneralPath)shape).curveTo(13.9638815, 56.9903, 13.801122, 57.81061, 13.801134, 58.344463);
((GeneralPath)shape).curveTo(13.801121, 58.839256, 14.002943, 59.236393, 14.406603, 59.53587);
((GeneralPath)shape).curveTo(14.810234, 59.82233, 15.630546, 59.97858, 16.86754, 60.00462);
((GeneralPath)shape).lineTo(16.69176, 60.727276);
((GeneralPath)shape).lineTo(3.5472279, 60.727276);
((GeneralPath)shape).lineTo(3.7816029, 60.00462);
((GeneralPath)shape).curveTo(5.122744, 60.00462, 6.099305, 59.757225, 6.7112904, 59.262432);
((GeneralPath)shape).curveTo(7.3232627, 58.754623, 7.8831577, 57.628323, 8.390978, 55.883526);
((GeneralPath)shape).lineTo(14.289416, 35.94212);
((GeneralPath)shape).curveTo(12.258154, 36.007248, 10.558937, 36.449955, 9.191761, 37.270245);
((GeneralPath)shape).curveTo(7.824566, 38.09058, 6.5745673, 39.457764, 5.441761, 41.371807);
((GeneralPath)shape).lineTo(4.6800423, 41.371807);
((GeneralPath)shape).lineTo(6.769886, 34.2429);
g.setPaint(paint);
g.fill(shape);
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(2.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(6.769885, 34.2429);
((GeneralPath)shape).lineTo(28.07848, 34.2429);
((GeneralPath)shape).lineTo(26.066761, 41.371807);
((GeneralPath)shape).lineTo(25.363636, 41.371807);
((GeneralPath)shape).curveTo(25.389656, 40.994225, 25.402676, 40.662193, 25.402697, 40.375713);
((GeneralPath)shape).curveTo(25.402672, 39.02157, 25.005537, 37.973392, 24.21129, 37.231182);
((GeneralPath)shape).curveTo(23.416996, 36.475998, 22.075851, 36.04631, 20.187853, 35.94212);
((GeneralPath)shape).lineTo(14.289415, 55.883526);
((GeneralPath)shape).curveTo(13.9638815, 56.9903, 13.801122, 57.81061, 13.801134, 58.344463);
((GeneralPath)shape).curveTo(13.801121, 58.839256, 14.002943, 59.236393, 14.406603, 59.53587);
((GeneralPath)shape).curveTo(14.810234, 59.82233, 15.630546, 59.97858, 16.86754, 60.00462);
((GeneralPath)shape).lineTo(16.69176, 60.727276);
((GeneralPath)shape).lineTo(3.5472279, 60.727276);
((GeneralPath)shape).lineTo(3.7816029, 60.00462);
((GeneralPath)shape).curveTo(5.122744, 60.00462, 6.099305, 59.757225, 6.7112904, 59.262432);
((GeneralPath)shape).curveTo(7.3232627, 58.754623, 7.8831577, 57.628323, 8.390978, 55.883526);
((GeneralPath)shape).lineTo(14.289416, 35.94212);
((GeneralPath)shape).curveTo(12.258154, 36.007248, 10.558937, 36.449955, 9.191761, 37.270245);
((GeneralPath)shape).curveTo(7.824566, 38.09058, 6.5745673, 39.457764, 5.441761, 41.371807);
((GeneralPath)shape).lineTo(4.6800423, 41.371807);
((GeneralPath)shape).lineTo(6.769886, 34.2429);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_3_0_0;
g.setTransform(defaultTransform__0_0_3_0_0);
g.setClip(clip__0_0_3_0_0);
float alpha__0_0_3_0_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_3_0_1 = g.getClip();
AffineTransform defaultTransform__0_0_3_0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_3_0_1 is ShapeNode
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(36.301136, 47.79759);
((GeneralPath)shape).lineTo(33.957386, 55.824932);
((GeneralPath)shape).curveTo(33.618835, 56.95775, 33.449566, 57.79108, 33.449574, 58.324932);
((GeneralPath)shape).curveTo(33.449566, 58.819725, 33.65139, 59.21686, 34.055042, 59.51634);
((GeneralPath)shape).curveTo(34.45868, 59.81582, 35.298523, 59.97858, 36.574574, 60.00462);
((GeneralPath)shape).lineTo(36.301136, 60.727276);
((GeneralPath)shape).lineTo(23.54723, 60.727276);
((GeneralPath)shape).lineTo(23.781605, 60.00462);
((GeneralPath)shape).curveTo(25.057646, 60.00462, 25.988634, 59.757225, 26.574574, 59.262432);
((GeneralPath)shape).curveTo(27.173529, 58.767643, 27.720404, 57.660873, 28.215199, 55.94212);
((GeneralPath)shape).lineTo(33.137074, 38.949932);
((GeneralPath)shape).curveTo(33.410503, 37.999435, 33.547222, 37.250736, 33.54723, 36.70384);
((GeneralPath)shape).curveTo(33.547222, 36.18303, 33.37795, 35.785896, 33.039417, 35.512432);
((GeneralPath)shape).curveTo(32.700867, 35.23902, 31.913109, 35.05673, 30.676136, 34.965557);
((GeneralPath)shape).lineTo(30.871449, 34.2429);
((GeneralPath)shape).lineTo(41.84801, 34.2429);
((GeneralPath)shape).curveTo(44.98601, 34.242928, 47.19304, 34.789803, 48.469105, 35.883526);
((GeneralPath)shape).curveTo(49.74512, 36.9773, 50.38314, 38.38355, 50.383167, 40.102276);
((GeneralPath)shape).curveTo(50.38314, 41.91219, 49.640957, 43.546303, 48.156605, 45.00462);
((GeneralPath)shape).curveTo(47.21908, 45.916092, 45.55893, 46.70385, 43.176136, 47.3679);
((GeneralPath)shape).lineTo(45.55895, 55.395245);
((GeneralPath)shape).curveTo(46.19695, 57.543686, 46.75033, 58.839256, 47.219105, 59.281963);
((GeneralPath)shape).curveTo(47.70085, 59.724674, 48.49512, 59.965557, 49.601917, 60.00462);
((GeneralPath)shape).lineTo(49.601917, 60.727276);
((GeneralPath)shape).lineTo(41.35973, 60.727276);
((GeneralPath)shape).lineTo(37.60973, 47.79759);
((GeneralPath)shape).lineTo(36.9652, 47.836647);
((GeneralPath)shape).curveTo(36.821957, 47.83666, 36.600605, 47.82364, 36.301136, 47.79759);
((GeneralPath)shape).moveTo(36.574574, 46.625713);
((GeneralPath)shape).curveTo(37.03029, 46.65177, 37.414406, 46.664795, 37.726917, 46.664772);
((GeneralPath)shape).curveTo(39.627945, 46.664787, 41.190445, 46.013744, 42.414417, 44.711647);
((GeneralPath)shape).curveTo(43.63836, 43.40958, 44.250336, 41.703854, 44.250355, 39.59446);
((GeneralPath)shape).curveTo(44.250336, 38.344482, 43.924816, 37.393963, 43.273792, 36.742897);
((GeneralPath)shape).curveTo(42.622734, 36.09188, 41.76336, 35.766357, 40.695667, 35.766335);
((GeneralPath)shape).curveTo(40.435234, 35.76636, 40.129246, 35.772835, 39.7777, 35.785866);
((GeneralPath)shape).lineTo(36.574574, 46.62571);
g.setPaint(paint);
g.fill(shape);
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(2.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(36.301136, 47.79759);
((GeneralPath)shape).lineTo(33.957386, 55.824932);
((GeneralPath)shape).curveTo(33.618835, 56.95775, 33.449566, 57.79108, 33.449574, 58.324932);
((GeneralPath)shape).curveTo(33.449566, 58.819725, 33.65139, 59.21686, 34.055042, 59.51634);
((GeneralPath)shape).curveTo(34.45868, 59.81582, 35.298523, 59.97858, 36.574574, 60.00462);
((GeneralPath)shape).lineTo(36.301136, 60.727276);
((GeneralPath)shape).lineTo(23.54723, 60.727276);
((GeneralPath)shape).lineTo(23.781605, 60.00462);
((GeneralPath)shape).curveTo(25.057646, 60.00462, 25.988634, 59.757225, 26.574574, 59.262432);
((GeneralPath)shape).curveTo(27.173529, 58.767643, 27.720404, 57.660873, 28.215199, 55.94212);
((GeneralPath)shape).lineTo(33.137074, 38.949932);
((GeneralPath)shape).curveTo(33.410503, 37.999435, 33.547222, 37.250736, 33.54723, 36.70384);
((GeneralPath)shape).curveTo(33.547222, 36.18303, 33.37795, 35.785896, 33.039417, 35.512432);
((GeneralPath)shape).curveTo(32.700867, 35.23902, 31.913109, 35.05673, 30.676136, 34.965557);
((GeneralPath)shape).lineTo(30.871449, 34.2429);
((GeneralPath)shape).lineTo(41.84801, 34.2429);
((GeneralPath)shape).curveTo(44.98601, 34.242928, 47.19304, 34.789803, 48.469105, 35.883526);
((GeneralPath)shape).curveTo(49.74512, 36.9773, 50.38314, 38.38355, 50.383167, 40.102276);
((GeneralPath)shape).curveTo(50.38314, 41.91219, 49.640957, 43.546303, 48.156605, 45.00462);
((GeneralPath)shape).curveTo(47.21908, 45.916092, 45.55893, 46.70385, 43.176136, 47.3679);
((GeneralPath)shape).lineTo(45.55895, 55.395245);
((GeneralPath)shape).curveTo(46.19695, 57.543686, 46.75033, 58.839256, 47.219105, 59.281963);
((GeneralPath)shape).curveTo(47.70085, 59.724674, 48.49512, 59.965557, 49.601917, 60.00462);
((GeneralPath)shape).lineTo(49.601917, 60.727276);
((GeneralPath)shape).lineTo(41.35973, 60.727276);
((GeneralPath)shape).lineTo(37.60973, 47.79759);
((GeneralPath)shape).lineTo(36.9652, 47.836647);
((GeneralPath)shape).curveTo(36.821957, 47.83666, 36.600605, 47.82364, 36.301136, 47.79759);
((GeneralPath)shape).moveTo(36.574574, 46.625713);
((GeneralPath)shape).curveTo(37.03029, 46.65177, 37.414406, 46.664795, 37.726917, 46.664772);
((GeneralPath)shape).curveTo(39.627945, 46.664787, 41.190445, 46.013744, 42.414417, 44.711647);
((GeneralPath)shape).curveTo(43.63836, 43.40958, 44.250336, 41.703854, 44.250355, 39.59446);
((GeneralPath)shape).curveTo(44.250336, 38.344482, 43.924816, 37.393963, 43.273792, 36.742897);
((GeneralPath)shape).curveTo(42.622734, 36.09188, 41.76336, 35.766357, 40.695667, 35.766335);
((GeneralPath)shape).curveTo(40.435234, 35.76636, 40.129246, 35.772835, 39.7777, 35.785866);
((GeneralPath)shape).lineTo(36.574574, 46.62571);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
origAlpha = alpha__0_0_3_0_1;
g.setTransform(defaultTransform__0_0_3_0_1);
g.setClip(clip__0_0_3_0_1);
origAlpha = alpha__0_0_3_0;
g.setTransform(defaultTransform__0_0_3_0);
g.setClip(clip__0_0_3_0);
origAlpha = alpha__0_0_3;
g.setTransform(defaultTransform__0_0_3);
g.setClip(clip__0_0_3);
float alpha__0_0_4 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_4 = g.getClip();
AffineTransform defaultTransform__0_0_4 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_4 is CompositeGraphicsNode
float alpha__0_0_4_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_4_0 = g.getClip();
AffineTransform defaultTransform__0_0_4_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_4_0 is ShapeNode
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(6.769885, 34.2429);
((GeneralPath)shape).lineTo(28.07848, 34.2429);
((GeneralPath)shape).lineTo(26.066761, 41.371807);
((GeneralPath)shape).lineTo(25.363636, 41.371807);
((GeneralPath)shape).curveTo(25.389656, 40.994225, 25.402676, 40.662193, 25.402697, 40.375713);
((GeneralPath)shape).curveTo(25.402672, 39.02157, 25.005537, 37.973392, 24.21129, 37.231182);
((GeneralPath)shape).curveTo(23.416996, 36.475998, 22.075851, 36.04631, 20.187853, 35.94212);
((GeneralPath)shape).lineTo(14.289415, 55.883526);
((GeneralPath)shape).curveTo(13.9638815, 56.9903, 13.801122, 57.81061, 13.801134, 58.344463);
((GeneralPath)shape).curveTo(13.801121, 58.839256, 14.002943, 59.236393, 14.406603, 59.53587);
((GeneralPath)shape).curveTo(14.810234, 59.82233, 15.630546, 59.97858, 16.86754, 60.00462);
((GeneralPath)shape).lineTo(16.69176, 60.727276);
((GeneralPath)shape).lineTo(3.5472279, 60.727276);
((GeneralPath)shape).lineTo(3.7816029, 60.00462);
((GeneralPath)shape).curveTo(5.122744, 60.00462, 6.099305, 59.757225, 6.7112904, 59.262432);
((GeneralPath)shape).curveTo(7.3232627, 58.754623, 7.8831577, 57.628323, 8.390978, 55.883526);
((GeneralPath)shape).lineTo(14.289416, 35.94212);
((GeneralPath)shape).curveTo(12.258154, 36.007248, 10.558937, 36.449955, 9.191761, 37.270245);
((GeneralPath)shape).curveTo(7.824566, 38.09058, 6.5745673, 39.457764, 5.441761, 41.371807);
((GeneralPath)shape).lineTo(4.6800423, 41.371807);
((GeneralPath)shape).lineTo(6.769886, 34.2429);
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_4_0;
g.setTransform(defaultTransform__0_0_4_0);
g.setClip(clip__0_0_4_0);
float alpha__0_0_4_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_4_1 = g.getClip();
AffineTransform defaultTransform__0_0_4_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_4_1 is ShapeNode
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(36.301136, 47.79759);
((GeneralPath)shape).lineTo(33.957386, 55.824932);
((GeneralPath)shape).curveTo(33.618835, 56.95775, 33.449566, 57.79108, 33.449574, 58.324932);
((GeneralPath)shape).curveTo(33.449566, 58.819725, 33.65139, 59.21686, 34.055042, 59.51634);
((GeneralPath)shape).curveTo(34.45868, 59.81582, 35.298523, 59.97858, 36.574574, 60.00462);
((GeneralPath)shape).lineTo(36.301136, 60.727276);
((GeneralPath)shape).lineTo(23.54723, 60.727276);
((GeneralPath)shape).lineTo(23.781605, 60.00462);
((GeneralPath)shape).curveTo(25.057646, 60.00462, 25.988634, 59.757225, 26.574574, 59.262432);
((GeneralPath)shape).curveTo(27.173529, 58.767643, 27.720404, 57.660873, 28.215199, 55.94212);
((GeneralPath)shape).lineTo(33.137074, 38.949932);
((GeneralPath)shape).curveTo(33.410503, 37.999435, 33.547222, 37.250736, 33.54723, 36.70384);
((GeneralPath)shape).curveTo(33.547222, 36.18303, 33.37795, 35.785896, 33.039417, 35.512432);
((GeneralPath)shape).curveTo(32.700867, 35.23902, 31.913109, 35.05673, 30.676136, 34.965557);
((GeneralPath)shape).lineTo(30.871449, 34.2429);
((GeneralPath)shape).lineTo(41.84801, 34.2429);
((GeneralPath)shape).curveTo(44.98601, 34.242928, 47.19304, 34.789803, 48.469105, 35.883526);
((GeneralPath)shape).curveTo(49.74512, 36.9773, 50.38314, 38.38355, 50.383167, 40.102276);
((GeneralPath)shape).curveTo(50.38314, 41.91219, 49.640957, 43.546303, 48.156605, 45.00462);
((GeneralPath)shape).curveTo(47.21908, 45.916092, 45.55893, 46.70385, 43.176136, 47.3679);
((GeneralPath)shape).lineTo(45.55895, 55.395245);
((GeneralPath)shape).curveTo(46.19695, 57.543686, 46.75033, 58.839256, 47.219105, 59.281963);
((GeneralPath)shape).curveTo(47.70085, 59.724674, 48.49512, 59.965557, 49.601917, 60.00462);
((GeneralPath)shape).lineTo(49.601917, 60.727276);
((GeneralPath)shape).lineTo(41.35973, 60.727276);
((GeneralPath)shape).lineTo(37.60973, 47.79759);
((GeneralPath)shape).lineTo(36.9652, 47.836647);
((GeneralPath)shape).curveTo(36.821957, 47.83666, 36.600605, 47.82364, 36.301136, 47.79759);
((GeneralPath)shape).moveTo(36.574574, 46.625713);
((GeneralPath)shape).curveTo(37.03029, 46.65177, 37.414406, 46.664795, 37.726917, 46.664772);
((GeneralPath)shape).curveTo(39.627945, 46.664787, 41.190445, 46.013744, 42.414417, 44.711647);
((GeneralPath)shape).curveTo(43.63836, 43.40958, 44.250336, 41.703854, 44.250355, 39.59446);
((GeneralPath)shape).curveTo(44.250336, 38.344482, 43.924816, 37.393963, 43.273792, 36.742897);
((GeneralPath)shape).curveTo(42.622734, 36.09188, 41.76336, 35.766357, 40.695667, 35.766335);
((GeneralPath)shape).curveTo(40.435234, 35.76636, 40.129246, 35.772835, 39.7777, 35.785866);
((GeneralPath)shape).lineTo(36.574574, 46.62571);
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_4_1;
g.setTransform(defaultTransform__0_0_4_1);
g.setClip(clip__0_0_4_1);
origAlpha = alpha__0_0_4;
g.setTransform(defaultTransform__0_0_4);
g.setClip(clip__0_0_4);
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
        return 0;
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
		return 64;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static int getOrigHeight() {
		return 64;
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
	public TermRaiderAppIcon() {
        this.width = getOrigWidth();
        this.height = getOrigHeight();
	}
	
	/**
	 * Creates a new transcoded SVG image with the given dimensions.
	 *
	 * @param size the dimensions of the icon
	 */
	public TermRaiderAppIcon(Dimension size) {
	this.width = size.width;
	this.height = size.width;
	}

	public TermRaiderAppIcon(int width, int height) {
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

