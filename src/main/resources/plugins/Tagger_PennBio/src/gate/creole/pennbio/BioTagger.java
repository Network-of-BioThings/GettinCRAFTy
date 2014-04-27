package gate.creole.pennbio;

import gate.creole.PackagedController;
import gate.creole.metadata.AutoInstance;
import gate.creole.metadata.AutoInstanceParam;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;

import java.net.URL;
import java.util.List;

@CreoleResource(name = "Penn BioTagger", autoinstances = @AutoInstance(parameters = {
	@AutoInstanceParam(name="pipelineURL", value="resources/biotagger.xgapp"),
	@AutoInstanceParam(name="menu", value="Biomedical")}), icon="bio")
public class BioTagger extends PackagedController {

}
