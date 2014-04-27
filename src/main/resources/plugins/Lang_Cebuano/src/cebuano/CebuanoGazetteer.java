package cebuano;

import java.net.URL;

import gate.creole.gazetteer.DefaultGazetteer;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;

@CreoleResource(name="Cebuano Gazetteer")
public class CebuanoGazetteer extends DefaultGazetteer {

  @CreoleParameter(defaultValue="resources/gazetteer/cebuano/lists.def")
  public void setListsURL(URL url) {
    super.setListsURL(url);
  }
}
