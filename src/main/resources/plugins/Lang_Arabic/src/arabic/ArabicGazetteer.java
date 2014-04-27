package arabic;

import java.net.URL;

import gate.creole.gazetteer.DefaultGazetteer;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.CreoleParameter;

@CreoleResource(name="Arabic Gazetteer")
public class ArabicGazetteer extends DefaultGazetteer {

	@CreoleParameter(defaultValue="resources/gazetteer/lists.def")
	public void setListsURL(URL url) {
		super.setListsURL(url);
	}
}
