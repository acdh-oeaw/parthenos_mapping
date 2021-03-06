//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.12 at 05:10:52 PM CEST 
//


package gr.forth.x3ml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="rationale" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="alternatives" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="typical_mistakes" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="local_habits" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="link_to_cook_book" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="example" type="{}exampleComplexType"/&gt;
 *         &lt;element ref="{}comments_last_update"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "rationale",
    "alternatives",
    "typicalMistakes",
    "localHabits",
    "linkToCookBook",
    "example",
    "commentsLastUpdate"
})
@XmlRootElement(name = "comment")
public class Comment {

    @XmlElement(required = true)
    protected String rationale;
    @XmlElement(required = true)
    protected String alternatives;
    @XmlElement(name = "typical_mistakes", required = true)
    protected String typicalMistakes;
    @XmlElement(name = "local_habits", required = true)
    protected String localHabits;
    @XmlElement(name = "link_to_cook_book", required = true)
    protected String linkToCookBook;
    @XmlElement(required = true)
    protected ExampleComplexType example;
    @XmlElement(name = "comments_last_update", required = true)
    protected CommentsLastUpdate commentsLastUpdate;
    @XmlAttribute(name = "type", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String type;

    /**
     * Gets the value of the rationale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRationale() {
        return rationale;
    }

    /**
     * Sets the value of the rationale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRationale(String value) {
        this.rationale = value;
    }

    /**
     * Gets the value of the alternatives property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlternatives() {
        return alternatives;
    }

    /**
     * Sets the value of the alternatives property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlternatives(String value) {
        this.alternatives = value;
    }

    /**
     * Gets the value of the typicalMistakes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypicalMistakes() {
        return typicalMistakes;
    }

    /**
     * Sets the value of the typicalMistakes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypicalMistakes(String value) {
        this.typicalMistakes = value;
    }

    /**
     * Gets the value of the localHabits property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalHabits() {
        return localHabits;
    }

    /**
     * Sets the value of the localHabits property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalHabits(String value) {
        this.localHabits = value;
    }

    /**
     * Gets the value of the linkToCookBook property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkToCookBook() {
        return linkToCookBook;
    }

    /**
     * Sets the value of the linkToCookBook property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkToCookBook(String value) {
        this.linkToCookBook = value;
    }

    /**
     * Gets the value of the example property.
     * 
     * @return
     *     possible object is
     *     {@link ExampleComplexType }
     *     
     */
    public ExampleComplexType getExample() {
        return example;
    }

    /**
     * Sets the value of the example property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExampleComplexType }
     *     
     */
    public void setExample(ExampleComplexType value) {
        this.example = value;
    }

    /**
     * Gets the value of the commentsLastUpdate property.
     * 
     * @return
     *     possible object is
     *     {@link CommentsLastUpdate }
     *     
     */
    public CommentsLastUpdate getCommentsLastUpdate() {
        return commentsLastUpdate;
    }

    /**
     * Sets the value of the commentsLastUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommentsLastUpdate }
     *     
     */
    public void setCommentsLastUpdate(CommentsLastUpdate value) {
        this.commentsLastUpdate = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

}
