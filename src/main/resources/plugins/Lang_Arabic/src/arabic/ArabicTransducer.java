package arabic;

import java.net.URL;

import gate.creole.Transducer;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.CreoleParameter;

@CreoleResource(name="Arabic Main Grammar")
public class ArabicTransducer extends Transducer {
	
	@CreoleParameter(defaultValue="resources/grammar/main.jape")
	public void setGrammarURL(URL url) {
		super.setGrammarURL(url);
	}
}
