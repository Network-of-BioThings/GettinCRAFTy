package arabic;

import java.net.URL;

import gate.creole.orthomatcher.OrthoMatcher;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.CreoleParameter;

@CreoleResource(name="Arabic OrthoMatcher")
public class ArabicOrthoMatcher extends OrthoMatcher{

	@CreoleParameter(defaultValue="resources/orthomatcher/listsNM.def")
	public void setDefinitionFileURL(URL url) {
		super.setDefinitionFileURL(url);
	}
}
