package cebuano;

import java.net.URL;

import gate.creole.tokeniser.DefaultTokeniser;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.CreoleParameter;

@CreoleResource(name="Cebuano Tokeniser")
public class CebuanoTokeniser extends DefaultTokeniser {

	@CreoleParameter(defaultValue="resources/tokeniser/DefaultTokeniser.rules")
	public void setTokeniserRulesURL(URL url) {
		super.setTokeniserRulesURL(url);
	}

	@CreoleParameter(defaultValue="resources/tokeniser/postprocess.jape")
	public void setTransducerGrammarURL(URL url) {
		super.setTransducerGrammarURL(url);
	}
}
