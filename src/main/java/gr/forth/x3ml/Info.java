//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.12 at 05:10:52 PM CEST 
//


package gr.forth.x3ml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="general_description" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="source_info" type="{}source_infoComplexType"/&gt;
 *         &lt;element name="target_info" type="{}target_infoComplexType" maxOccurs="unbounded"/&gt;
 *         &lt;element name="mapping_info" type="{}mapping_infoComplexType"/&gt;
 *         &lt;element name="example_data_info" type="{}example_data_infoComplexType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "title",
    "generalDescription",
    "sourceInfo",
    "targetInfo",
    "mappingInfo",
    "exampleDataInfo"
})
@XmlRootElement(name = "info")
public class Info {

    @XmlElement(required = true)
    protected String title;
    @XmlElement(name = "general_description", required = true)
    protected String generalDescription;
    @XmlElement(name = "source_info", required = true)
    protected SourceInfoComplexType sourceInfo;
    @XmlElement(name = "target_info", required = true)
    protected List<TargetInfoComplexType> targetInfo;
    @XmlElement(name = "mapping_info", required = true)
    protected MappingInfoComplexType mappingInfo;
    @XmlElement(name = "example_data_info", required = true)
    protected ExampleDataInfoComplexType exampleDataInfo;

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the generalDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGeneralDescription() {
        return generalDescription;
    }

    /**
     * Sets the value of the generalDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGeneralDescription(String value) {
        this.generalDescription = value;
    }

    /**
     * Gets the value of the sourceInfo property.
     * 
     * @return
     *     possible object is
     *     {@link SourceInfoComplexType }
     *     
     */
    public SourceInfoComplexType getSourceInfo() {
        return sourceInfo;
    }

    /**
     * Sets the value of the sourceInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceInfoComplexType }
     *     
     */
    public void setSourceInfo(SourceInfoComplexType value) {
        this.sourceInfo = value;
    }

    /**
     * Gets the value of the targetInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the targetInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTargetInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TargetInfoComplexType }
     * 
     * 
     */
    public List<TargetInfoComplexType> getTargetInfo() {
        if (targetInfo == null) {
            targetInfo = new ArrayList<TargetInfoComplexType>();
        }
        return this.targetInfo;
    }

    /**
     * Gets the value of the mappingInfo property.
     * 
     * @return
     *     possible object is
     *     {@link MappingInfoComplexType }
     *     
     */
    public MappingInfoComplexType getMappingInfo() {
        return mappingInfo;
    }

    /**
     * Sets the value of the mappingInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link MappingInfoComplexType }
     *     
     */
    public void setMappingInfo(MappingInfoComplexType value) {
        this.mappingInfo = value;
    }

    /**
     * Gets the value of the exampleDataInfo property.
     * 
     * @return
     *     possible object is
     *     {@link ExampleDataInfoComplexType }
     *     
     */
    public ExampleDataInfoComplexType getExampleDataInfo() {
        return exampleDataInfo;
    }

    /**
     * Sets the value of the exampleDataInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExampleDataInfoComplexType }
     *     
     */
    public void setExampleDataInfo(ExampleDataInfoComplexType value) {
        this.exampleDataInfo = value;
    }

}
