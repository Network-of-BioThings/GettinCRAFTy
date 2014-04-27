package gate.creole.coref;

import gate.creole.coref.matchers.Alias;
import gate.creole.coref.matchers.Or;
import gate.creole.coref.matchers.TransitiveAnd;
import gate.creole.coref.taggers.DocumentText;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.util.GateException;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

@CreoleResource (name="OrthoRef", comment = "An orthographic coreferencer")
public class OrthoRef extends CorefBase {

  /**
   * An orthographic co-reference element.
   */
  private static final long serialVersionUID = 1426355084774777956L;

  /* (non-Javadoc)
   * @see gate.creole.coref.CorefBase#setConfigFileUrl(java.net.URL)
   */
  @Override
  @CreoleParameter(defaultValue = "resources/default-config.coref.xml")
  public void setConfigFileUrl(URL configFileUrl) {
    super.setConfigFileUrl(configFileUrl);
  }


  /**
   * Debug code, please do not use. 
   * @param args
   * @throws GateException
   * @throws IOException
   */
  public static void main(String[] args) throws GateException, IOException{
    Config config = new Config();
    List<Tagger> taggers = new ArrayList<Tagger>();
    config.setTaggers(taggers);
    List<Matcher> matchers = new ArrayList<Matcher>();
    config.setMatchers(matchers);
    
    taggers.add(new DocumentText("Organization"));
    taggers.add(new DocumentText("Person"));
    taggers.add(new DocumentText("Location"));
    gate.creole.coref.taggers.Alias personTagger = 
        new gate.creole.coref.taggers.Alias("Person", "nicknames.txt");
    taggers.add(personTagger);
    
    
    matchers.add(new TransitiveAnd(
      new Or(new Matcher[] {
        new gate.creole.coref.matchers.DocumentText("Organization", "Organization"),
        new gate.creole.coref.matchers.Initials("Organization", "Organization"),
        new gate.creole.coref.matchers.MwePart("Organization", "Organization") 
      })));
    
    matchers.add(new Alias("Person", "Person", personTagger));
    
    XStream xstream = getXstream();
    OutputStream out = new BufferedOutputStream(new FileOutputStream("plugins/Coref_Tools/matcher-config.xml"));
    xstream.toXML(config, out);
    out.close();
  }
}
