
package genes.models.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the genes.models.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetGenesTaggerResponse_QNAME = new QName("http://ws.models.genes/", "getGenesTaggerResponse");
    private final static QName _MyMap_QNAME = new QName("http://ws.models.genes/", "MyMap");
    private final static QName _GetBioCreativeResults_QNAME = new QName("http://ws.models.genes/", "getBioCreativeResults");
    private final static QName _GetGenesTagger_QNAME = new QName("http://ws.models.genes/", "getGenesTagger");
    private final static QName _IOException_QNAME = new QName("http://ws.models.genes/", "IOException");
    private final static QName _GetBioCreativeResultsResponse_QNAME = new QName("http://ws.models.genes/", "getBioCreativeResultsResponse");
    private final static QName _A_QNAME = new QName("http://ws.models.genes/", "A");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: genes.models.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetBioCreativeResultsResponse }
     * 
     */
    public GetBioCreativeResultsResponse createGetBioCreativeResultsResponse() {
        return new GetBioCreativeResultsResponse();
    }

    /**
     * Create an instance of {@link IOException }
     * 
     */
    public IOException createIOException() {
        return new IOException();
    }

    /**
     * Create an instance of {@link A }
     * 
     */
    public A createA() {
        return new A();
    }

    /**
     * Create an instance of {@link GetGenesTaggerResponse }
     * 
     */
    public GetGenesTaggerResponse createGetGenesTaggerResponse() {
        return new GetGenesTaggerResponse();
    }

    /**
     * Create an instance of {@link MyMap }
     * 
     */
    public MyMap createMyMap() {
        return new MyMap();
    }

    /**
     * Create an instance of {@link GetBioCreativeResults }
     * 
     */
    public GetBioCreativeResults createGetBioCreativeResults() {
        return new GetBioCreativeResults();
    }

    /**
     * Create an instance of {@link GetGenesTagger }
     * 
     */
    public GetGenesTagger createGetGenesTagger() {
        return new GetGenesTagger();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetGenesTaggerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.models.genes/", name = "getGenesTaggerResponse")
    public JAXBElement<GetGenesTaggerResponse> createGetGenesTaggerResponse(GetGenesTaggerResponse value) {
        return new JAXBElement<GetGenesTaggerResponse>(_GetGenesTaggerResponse_QNAME, GetGenesTaggerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MyMap }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.models.genes/", name = "MyMap")
    public JAXBElement<MyMap> createMyMap(MyMap value) {
        return new JAXBElement<MyMap>(_MyMap_QNAME, MyMap.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBioCreativeResults }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.models.genes/", name = "getBioCreativeResults")
    public JAXBElement<GetBioCreativeResults> createGetBioCreativeResults(GetBioCreativeResults value) {
        return new JAXBElement<GetBioCreativeResults>(_GetBioCreativeResults_QNAME, GetBioCreativeResults.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetGenesTagger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.models.genes/", name = "getGenesTagger")
    public JAXBElement<GetGenesTagger> createGetGenesTagger(GetGenesTagger value) {
        return new JAXBElement<GetGenesTagger>(_GetGenesTagger_QNAME, GetGenesTagger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IOException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.models.genes/", name = "IOException")
    public JAXBElement<IOException> createIOException(IOException value) {
        return new JAXBElement<IOException>(_IOException_QNAME, IOException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBioCreativeResultsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.models.genes/", name = "getBioCreativeResultsResponse")
    public JAXBElement<GetBioCreativeResultsResponse> createGetBioCreativeResultsResponse(GetBioCreativeResultsResponse value) {
        return new JAXBElement<GetBioCreativeResultsResponse>(_GetBioCreativeResultsResponse_QNAME, GetBioCreativeResultsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link A }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.models.genes/", name = "A")
    public JAXBElement<A> createA(A value) {
        return new JAXBElement<A>(_A_QNAME, A.class, null, value);
    }

}
