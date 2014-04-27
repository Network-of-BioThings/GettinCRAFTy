package gate.termraider;

import gate.creole.PackagedController;
import gate.creole.metadata.AutoInstance;
import gate.creole.metadata.AutoInstanceParam;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;

@CreoleResource(name = "TermRaider English Term Extraction",
    icon = "TermRaiderApp",
    autoinstances = @AutoInstance(parameters = {
        @AutoInstanceParam(name="pipelineURL", value="applications/termraider-eng.gapp"),
        @AutoInstanceParam(name="menu", value="TermRaider")}))
public class TermRaiderEnglish extends PackagedController {

}
