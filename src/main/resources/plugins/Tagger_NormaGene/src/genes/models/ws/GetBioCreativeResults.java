
package genes.models.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getBioCreativeResults complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getBioCreativeResults">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="text" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="goCat" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="thresh" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getBioCreativeResults", propOrder = {
    "text",
    "goCat",
    "thresh"
})
public class GetBioCreativeResults {

    protected String text;
    protected boolean goCat;
    protected double thresh;

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * Gets the value of the goCat property.
     * 
     */
    public boolean isGoCat() {
        return goCat;
    }

    /**
     * Sets the value of the goCat property.
     * 
     */
    public void setGoCat(boolean value) {
        this.goCat = value;
    }

    /**
     * Gets the value of the thresh property.
     * 
     */
    public double getThresh() {
        return thresh;
    }

    /**
     * Sets the value of the thresh property.
     * 
     */
    public void setThresh(double value) {
        this.thresh = value;
    }

}
