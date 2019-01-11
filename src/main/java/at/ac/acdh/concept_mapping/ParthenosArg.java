package at.ac.acdh.concept_mapping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlSchemaType;

/*
* @author Wolfgang Walter SAUER (wowasa) &lt;wolfgang.sauer@oeaw.ac.at&gt;
*/
@XmlAccessorType(XmlAccessType.FIELD)
public class ParthenosArg{   

    @XmlAttribute(name = "name")
    @XmlSchemaType(name = "anySimpleType")
    protected String name;
    @XmlAttribute(name = "type")
    @XmlSchemaType(name = "anySimpleType")
    protected String type;
    
    @XmlMixed
    List<String> content  = new ArrayList<String>();

    
    @XmlElement(name = "concept")
    protected Collection<String> concepts = new ArrayList<>();

    @XmlElement(name = "pattern")
    protected Collection<String> patterns = new ArrayList<>();

    @XmlElement(name = "blacklistPattern")
    protected Collection<String> blacklistPatterns = new ArrayList<>();
    


    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String name) {
        this.name = name;
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


    public Collection<String> getConcepts() {
        return concepts;
    }

    public void setConcepts(Collection<String> concepts) {
        this.concepts = concepts;
    }

    public Collection<String> getPatterns() {
        return patterns;
    }

    public void setPatterns(Collection<String> patterns) {
        this.patterns = patterns;
    }

    public Collection<String> getBlacklistPatterns() {
        return blacklistPatterns;
    }

    public void setBlacklistPatterns(Collection<String> blacklistPatterns) {
        this.blacklistPatterns = blacklistPatterns;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }        

}
