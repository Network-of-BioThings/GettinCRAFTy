package arabic;
import java.net.URL;

import gate.creole.gazetteer.DefaultGazetteer;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.CreoleParameter;

@CreoleResource(name="Arabic Infered Gazetteer")
public class ArabicInferedGazetteer extends DefaultGazetteer {

	@CreoleParameter(defaultValue="resources/inferred-gazetteer/lists.def")
	public void setListsURL(URL url) {
		super.setListsURL(url);
	}
}
