package at.ac.acdh.concept_mapping;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;




/*
* @author Wolfgang Walter SAUER (wowasa) &lt;wolfgang.sauer@oeaw.ac.at&gt;
* 
* This extension of the LabelGenerator is necessary to create specific Arg objects which allow the use of concepts, blacklist patterns and default patterns to set schema 
* specific xpaths. The same approach is used in the Link object already. 
*/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "args"
})
@XmlRootElement(name = "label_generator")
public class ParthenosLabelGenerator{

    @XmlElement(name="arg")
    protected List<ParthenosArg> args;
    
    @XmlAttribute(name = "name", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String name;

    /**
     * Gets the value of the arg property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the arg property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArg().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Arg }
     * 
     * 
     */
    public List<ParthenosArg> getArgs() {
        if (args == null) {
            args = new ArrayList<ParthenosArg>();
        }
        return this.args;
    }

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
    public void setName(String value) {
        this.name = value;
    }
    
}
