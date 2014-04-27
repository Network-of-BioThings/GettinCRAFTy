package arabic;

import gate.creole.PackagedController;
import gate.creole.metadata.AutoInstance;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;

import java.net.URL;
import java.util.List;

@CreoleResource(name = "Arabic Gazetteer Collector", autoinstances = @AutoInstance)
public class ArabicGazCollector extends PackagedController {

  @Override
  @CreoleParameter(defaultValue = "resources/arabic_lists_collector.gapp")
  public void setPipelineURL(URL url) {
    this.url = url;
  }

  @Override
  @CreoleParameter(defaultValue = "Arabic")
  public void setMenu(List<String> menu) {
    super.setMenu(menu);
  }
}
