package french;

import gate.creole.PackagedController;
import gate.creole.metadata.AutoInstance;
import gate.creole.metadata.AutoInstanceParam;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;

import java.net.URL;
import java.util.List;

@CreoleResource(name = "French IE System", autoinstances = @AutoInstance(parameters = {
	@AutoInstanceParam(name="pipelineURL", value="french.gapp"),
	@AutoInstanceParam(name="menu", value="French")}))
public class FrenchIE extends PackagedController {

}
