package cebuano;

import java.net.URL;

import gate.creole.gazetteer.DefaultGazetteer;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;

@CreoleResource(name="Cebuano Gazetteer Tokeniser")
public class CebuanoGazetteerTokeniser extends DefaultGazetteer {

 @CreoleParameter(defaultValue="resources/tokeniser/lists.def")
  public void setListsURL(URL url) {
    super.setListsURL(url);
  }
}
