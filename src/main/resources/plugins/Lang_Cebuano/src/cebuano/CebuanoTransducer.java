package cebuano;

import java.net.URL;

import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.CreoleParameter;
import gate.creole.Transducer;

@CreoleResource(name="Cebuano Transducer")
public class CebuanoTransducer extends Transducer {

	@CreoleParameter(defaultValue="resources/grammar/main.jape")
	public void setGrammarURL(URL url) {
		super.setGrammarURL(url);
	}
}
