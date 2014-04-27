package cebuano;

import java.net.URL;

import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.CreoleParameter;
import gate.creole.Transducer;

@CreoleResource(name="Cebuano Transducer Postprocessor")
public class CebuanoTransducerPost extends Transducer {

	@CreoleParameter(defaultValue="resources/tokeniser/join.jape")
	public void setGrammarURL(URL url) {
		super.setGrammarURL(url);
	}
}
