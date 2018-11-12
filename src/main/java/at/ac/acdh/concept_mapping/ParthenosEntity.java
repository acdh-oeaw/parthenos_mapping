package at.ac.acdh.concept_mapping;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import gr.forth.x3ml.InstanceGenerator;
import gr.forth.x3ml.LabelGenerator;

/**
 * 
 * <p>
 * ParthenosEntity consists of number of attributes used by business logic to
 * create X3ML Entity object
 * </p>
 * 
 * @see gr.forth.x3ml.Entity
 * 
 * @author dostojic
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ParthenosEntity extends Node {

	@XmlAttribute
	String xpath;
	@XmlAttribute
	String relationship;
	@XmlAttribute
	String type;
	@XmlAttribute
	String hasType;
	@XmlAttribute
	String hasLabel;
    @XmlAttribute
    String isListed;
    @XmlAttribute
    String isListedLabel;
	@XmlAttribute
	String var;
	@XmlAttribute
	String globVar;
	@XmlAttribute
	String condition;
	@XmlAttribute
	String subrelation;
	@XmlElement(name = "link")
	Collection<Link> links = new ArrayList<Link>();

	@XmlElement(name = "instance-generator")
	InstanceGenerator ig;
	
	@XmlElement(name = "label-generator")
	Collection<LabelGenerator> lg = new ArrayList<LabelGenerator>();

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("entity: ");
		if (xpath != null)
			sb.append(xpath + "->");
		if (relationship != null)
			sb.append(relationship + "->");
		sb.append(type);

		if (hasType != null)
			sb.append("hasType " + hasType);

		if (isListed != null)
	            sb.append("hasType " + hasType);

		if (var != null)
			sb.append("var=" + var);

		if (globVar != null)
			sb.append("globVar=" + globVar);

		if (condition != null)
			sb.append("condition=" + condition);

		sb.append("\n");
		for (Link l : links)
			sb.append(l.toString());

		return sb.toString();
	}

	/**
	 * @return value from xpath attribute
	 *         <p>
	 * 		If provided object will be transformed to corresponding x3ml
	 *         class {@link gr.forth.x3ml.X3ML.Mappings.Mapping.Domain}
	 *         </p>
	 */
	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	/**
	 * @return value from relationship attribute
	 *         <p>
	 * 		Value represents relationship with parent entity corresponding
	 *         x3ml class {@link gr.forth.x3ml.TargetRelationType}
	 *         </p>
	 */
	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	/**
	 * @return value from type attribute
	 *         <p>
	 * 		Value represents CIDOC-CRM class corresponding x3ml class
	 *         {@link gr.forth.x3ml.TargetRelationType}
	 *         </p>
	 * 
	 *         use namespace qualified values
	 */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return value from hasType attribute
	 *         <p>
	 * 		If provided entity gets crm:P2_has_type crm:E55_Type = value
	 *         relationship
	 *         </p>
	 * 
	 */
	public String getHasType() {
		return hasType;
	}

	public void setHasType(String hasType) {
		this.hasType = hasType;
	}
	
	public void setIsListed(String isListed) {
        this.isListed = isListed;
    }

    public String getIsListed() {
	    return this.isListed;
	}
	
	public String getIsListedLabel() {
        return isListedLabel;
    }

    public void setIsListedLabel(String isListedLabel) {
        this.isListedLabel = isListedLabel;
    }

    public void SetIsListed(String isListed) {
	    this.isListed = isListed;
	}

	/**
	 * @return value from var attribute
	 *         <p>
	 * 		If provided entity is labeled with variable
	 *         </p>
	 * 
	 *         <p>
	 * 		Variables are used by X3ML to link entities from different
	 *         mappings
	 *         </p>
	 */
	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	/**
	 * @return value from globVar attribute
	 *         <p>
	 * 		If provided entity is labeled with global variable
	 *         </p>
	 * 
	 *         <p>
	 * 		Variables are used by X3ML to link entities from different
	 *         mappings
	 *         </p>
	 */
	public String getGlobVar() {
		return globVar;
	}

	public void setGlobVar(String globVar) {
		this.globVar = globVar;
	}

	/**
	 * @return value from condition attribute
	 *         <p>
	 * 		Used for filtering conditional entities. Filters are passed from
	 *         command line
	 *         </p>
	 * 
	 */
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	/**
	 * <p>
	 * Links are translated into (corresponding x3ml class)
	 * {@link gr.forth.x3ml.X3ML.Mappings.Mapping.Link} instances
	 * </p>
	 * 
	 * @return list of {@link Link}
	 * 
	 */
	public Collection<Link> getLinks() {
		return links;
	}

	public void setLinks(Collection<Link> links) {
		this.links = links;
	}

	public InstanceGenerator getInstanceGenerator() {
		return this.ig;
	}

	public void setInstanceGenerator(InstanceGenerator ig) {
		this.ig = ig;
	}
	
	public Collection<LabelGenerator> getLabelGenerator(){
		return this.lg;
	}
	
	public void setLabelGenerator(Collection<LabelGenerator> lg){
		this.lg = lg;
	}

	public String getSubrelation() {
		return subrelation;
	}

	public void setSubrelation(String subrelation) {
		this.subrelation = subrelation;
	}

	public String getHasLabel() {
		return hasLabel;
	}

	public void setHasLabel(String hasLabel) {
		this.hasLabel = hasLabel;
	}

}
