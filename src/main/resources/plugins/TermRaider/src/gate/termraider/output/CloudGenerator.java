/*
 * Copyright (c) 2008--2012, The University of Sheffield. See the file
 * COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 * 
 * This file is part of GATE (see http://gate.ac.uk/), and is free software,
 * licenced under the GNU Library General Public License, Version 2, June 1991
 * (in the distribution as file licence.html, and also available at
 * http://gate.ac.uk/gate/licence.html).
 */

package gate.termraider.output;

import gate.termraider.util.Term;

import java.awt.Color;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringEscapeUtils;

public class CloudGenerator {

  private Map<Term, ? extends Number> termNumbers;

  private double min = Double.MAX_VALUE;

  private double max = Double.MIN_VALUE;

  private Color foreground = Color.BLACK;

  private Color background = Color.WHITE;

  private String[] blended = new String[7];

  private Set<String> languages = null;

  private Set<String> types = null;

  public void setLanguages(Set<String> languages) {
    this.languages = languages;
    findRange();
  }

  public void includeLanguage(String lang, boolean show) {
    if (show) {
      languages.add(lang);
    }
    else {
      languages.remove(lang);
    }
    
    findRange();
  }
  
  public void setTermTypes(Set<String> types) {
    this.types = types;
    findRange();
  }
  
  public void includeTermType(String type, boolean show) {
    if (show) {
      types.add(type);
    }
    else {
      types.remove(type);
    }
    
    findRange();
  }

  public void setTerms(Map<Term, ? extends Number> termNumbers) {
    this.termNumbers = termNumbers;
    findRange();
  }

  private void findRange() {

    min = Double.MAX_VALUE;
    max = Double.MIN_VALUE;

    for(Map.Entry<Term, ? extends Number> entry : termNumbers.entrySet()) {
      if((languages == null || languages.contains(entry.getKey()
        .getLanguageCode()) &&
        (types == null || types.contains(entry.getKey().getType())))) {
        max = Math.max(max, entry.getValue().doubleValue());
        min = Math.min(min, entry.getValue().doubleValue());
      }
    }
  }

  public CloudGenerator(Map<Term, ? extends Number> termNumbers) {
    this.termNumbers = termNumbers;
    findRange();
    blend();
  }

  public void setForeground(Color color) {
    if(!foreground.equals(color)) {
      foreground = color;
      blend();
    }
  }

  public Color getForeground() {
    return foreground;
  }

  public void setBackground(Color color) {
    if(!background.equals(color)) {
      background = color;
      blend();
    }
  }

  private void blend() {
    blended[0] = getHTMLColor(blend(foreground, background, 0.4f));
    blended[1] = getHTMLColor(blend(foreground, background, 0.45f));
    blended[2] = getHTMLColor(blend(foreground, background, 0.5f));
    blended[3] = getHTMLColor(blend(foreground, background, 0.6f));
    blended[4] = getHTMLColor(blend(foreground, background, 0.7f));
    blended[5] = getHTMLColor(blend(foreground, background, 0.8f));
    blended[6] = getHTMLColor(blend(foreground, background, 0.9f));
  }

  public Color getBackground() {
    return background;
  }

  private static Color blend(Color clOne, Color clTwo, float fAmount) {
    float fInverse = 1.0f - fAmount;

    float afOne[] = new float[3];
    clOne.getColorComponents(afOne);
    float afTwo[] = new float[3];
    clTwo.getColorComponents(afTwo);

    float afResult[] = new float[3];
    afResult[0] = afOne[0] * fAmount + afTwo[0] * fInverse;
    afResult[1] = afOne[1] * fAmount + afTwo[1] * fInverse;
    afResult[2] = afOne[2] * fAmount + afTwo[2] * fInverse;

    return new Color(afResult[0], afResult[1], afResult[2]);
  }

  private static String getHTMLColor(Color color) {
    return "#" + Integer.toHexString(color.getRGB()).substring(2).toUpperCase();
  }

  public String getHTML(int percentage) {

    double min =
      this.min + ((max - this.min) * ((100d - (double)percentage) / 100));

    // stop lots of array copying by starting with a reasonably sized buffer
    StringBuilder html = new StringBuilder(1024 * 10);

    html.append("<html>\n<head>\n\t<style type='text/css'>\n");

    html.append("\t\tbody{background:").append(getHTMLColor(background))
      .append("}\n");

    html
      .append(
        "\t\t#cloud{padding:5px;text-align:justify;font-family:sans-serif;background:")
      .append(getHTMLColor(background)).append("}\n");

    // would be nice to add 'white-space:nowrap' but the renderer seems
    // to mess up and overflow. maybe an option to do this on export?
    html.append("\t\t.term{padding:0;}\n");

    html.append("\t\t.cloud0{font-size:1.0em;color:").append(blended[0])
      .append(";}\n");
    html.append("\t\t.cloud1{font-size:1.4em;color:").append(blended[0])
      .append(";}\n");
    html.append("\t\t.cloud2{font-size:1.8em;color:").append(blended[1])
      .append(";}\n");
    html.append("\t\t.cloud3{font-size:2.2em;color:").append(blended[1])
      .append(";}\n");
    html.append("\t\t.cloud4{font-size:2.6em;color:").append(blended[2])
      .append(";}\n");
    html.append("\t\t.cloud5{font-size:3.0em;color:").append(blended[2])
      .append(";}\n");
    html.append("\t\t.cloud6{font-size:3.3em;color:").append(blended[3])
      .append(";}\n");
    html.append("\t\t.cloud7{font-size:3.6em;color:").append(blended[4])
      .append(";}\n");
    html.append("\t\t.cloud8{font-size:3.9em;color:").append(blended[5])
      .append(";}\n");
    html.append("\t\t.cloud9{font-size:4.2em;color:").append(blended[6])
      .append(";}\n");
    html.append("\t\t.cloud10{font-size:4.5em;color:")
      .append(getHTMLColor(foreground)).append(";}\n");

    html.append("\t</style>\n</head>\n<body>\n\t<div id='cloud'>\n");

    Set<Term> terms = new TreeSet<Term>(termNumbers.keySet());

    double scaler = 10d / (max - min);

    for(Term term : terms) {
      double score = termNumbers.get(term).doubleValue();
      if(score >= min &&
        (languages == null || languages.contains(term.getLanguageCode()) &&
          (types == null || types.contains(term.getType())))) {
        int size = (int)((score - min) * scaler);

        html.append("\t\t<span class=\"term cloud").append(size).append("\">")
          .append(StringEscapeUtils.escapeXml(term.getTermString()).replaceAll("\\s+", "&#160;"))
          .append("</span>\n");
      }
    }

    html.append("\t</div>\n</body>\n</html>");

    return html.toString();
  }
}
