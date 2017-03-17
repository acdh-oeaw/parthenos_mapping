package at.ac.acdh.profile_parser;

/**
 * This class is internal representation of elements found in CLARIN CMDI profiles.
 * Except for attribute values it keeps reference to the parent node. This information is used to construct xpaths. 
 * 
 * @author dostojic
 *
 */
public class CRElement {
	
	/**
	 * 
	 * This enum represents internal classification of elements. An element can be
	 * <ul style="list-style-type:square">
  	 * <li>Element</li>
  	 * <li>Attribute</li>
  	 * <li>Component</li>
  	 * <li>or a special element holding CMDI version</li>
	 * </ul>
	 *
	 */
	public enum NodeType {COMPONENT, ELEMENT, ATTRIBUTE, CMD_VERSION_ATTR};
	
	String name = null;
	String ref = null; //datcat or id in case of component
	
	boolean isLeaf;
	boolean isRequired;	
	NodeType type;
	int lvl;
	CRElement parent = null;
	
	/** 
	 * @return value of the attribute "name"
	 */
	public String getName() {
		return name;
	}
	
	/** 
	 * @return true if attribute "minOccurs" exists and its value is greater then 0
	 */
	public boolean isRequired() {
		return isRequired;
	}
	
	/** 
	 * @return internal type of an element
	 * @see NodeType
	 */
	public NodeType getType(){
		return type;
	}
	
	/** 
	 * @return true if attribute "minOccurs" exists and its value is greater then 0
	 */
	public String getConcept(){//
		return type == NodeType.ELEMENT || type == NodeType.ATTRIBUTE? ref : null;
	}
	
	/** 
	 * @return componentId if element has type component
	 * @see NodeType
	 */
	public String getComponentId(){//if node is not component returns null
		return type == NodeType.COMPONENT? ref : null;
	}
	
	@Override
	public String toString() {
		return type.name() + "\t" + name + "\t" + "leaf:" + isLeaf + "\t" + ref;
	}
	

}
