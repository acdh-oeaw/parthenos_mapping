package at.ac.acdh.concept_mapping;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import gr.forth.x3ml.InstanceGenerator;

/**
 * 
 * <p>CMDI2CIDOCMap class is java representative for xml file CMDI2CIDOC.xml.</p>
 * <p>The xml file itself defines mappings between CMDI concepts/xpaths and CIDOC-CRM entities</p>
 * 
 * 
 * @author dostojic
 * 
 */
@XmlRootElement(name = "mappings")
@XmlAccessorType(XmlAccessType.FIELD)
public class CMDI2CIDOCMap{

	@XmlElement(name = "entity")
	Collection<ParthenosEntity> entities = new ArrayList<>();
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(ParthenosEntity pe: entities)
			sb.append(pe.toString() + "\n");
		
		return sb.toString();
	}
	

	/**
	 * @return list of specified {@link ParthenosEntity}
	 * 
	 * <p>Each {@link ParthenosEntity entity }will be transformed to one {@link gr.forth.x3ml.X3ML.Mappings.Mapping}</p>
	 */
	public Collection<ParthenosEntity> getEntities() {
		return entities;
	}

	public void setEntities(Collection<ParthenosEntity> entities) {
		this.entities = entities;
	}

	/**
	 * 
	 * <p>ParthenosEntity consists of number of attributes used by business logic to create X3ML Entity object </p> 
	 * 
	 * @see gr.forth.x3ml.Entity
	 * 
	 * @author dostojic
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class ParthenosEntity {
		@XmlAttribute String xpath;
		@XmlAttribute String relationship;		
		@XmlAttribute String type;
		@XmlAttribute String hasType;
		@XmlAttribute String var;
		@XmlAttribute String globVar;
		@XmlAttribute String condition;
		
		@XmlElement(name = "link")
		Collection<Link> links = new ArrayList<>();
		
		@XmlElement(name = "instance-generator")
		InstanceGenerator ig;
		
		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer("entity: ");
			if(xpath != null)
				sb.append(xpath + "->");
			if(relationship != null)
				sb.append(relationship + "->");
			sb.append(type);
			
			if(hasType != null)
				sb.append("hasType " + hasType);
			
			if(var != null)
				sb.append("var=" + var);
			
			if(globVar != null)
				sb.append("globVar=" + globVar);
			
			if(condition != null)
				sb.append("condition=" + condition);
			
			sb.append("\n");
			for(Link l: links)
				sb.append(l.toString());
			
			return sb.toString();			
		}

		/**
		 * @return value from xpath attribute
		 * <p>If provided object will be transformed to 
		 * corresponding x3ml class {@link gr.forth.x3ml.X3ML.Mappings.Mapping.Domain}</p>
		 */
		public String getXpath() {
			return xpath;
		}

		public void setXpath(String xpath) {
			this.xpath = xpath;
		}

		/**
		 * @return value from relationship attribute
		 * <p>Value represents relationship with parent entity 
		 * corresponding x3ml class {@link gr.forth.x3ml.TargetRelationType}</p>
		 */
		public String getRelationship() {
			return relationship;
		}

		public void setRelationship(String relationship) {
			this.relationship = relationship;
		}

		/**
		 * @return value from type attribute
		 * <p>Value represents CIDOC-CRM class
		 * corresponding x3ml class {@link gr.forth.x3ml.TargetRelationType}</p>
		 * 
		 * use namespace qualified values
		 */
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		/**
		 * @return value from hasType attribute
		 * <p>If provided entity gets crm:P2_has_type crm:E55_Type = value relationship</p> 
		 * 
		 */
		public String getHasType() {
			return hasType;
		}

		public void setHasType(String hasType) {
			this.hasType = hasType;
		}

		/**
		 * @return value from var attribute
		 * <p>If provided entity is labeled with variable</p> 
		 * 
		 * <p>Variables are used by X3ML to link entities from different mappings</p>
		 */
		public String getVar() {
			return var;
		}

		public void setVar(String var) {
			this.var = var;
		}


		/**
		 * @return value from globVar attribute
		 * <p>If provided entity is labeled with global variable</p>
		 * 
		 * <p>Variables are used by X3ML to link entities from different mappings</p>
		 */
		public String getGlobVar() {
			return globVar;
		}

		public void setGlobVar(String globVar) {
			this.globVar = globVar;
		}

		/**
		 * @return value from condition attribute
		 * <p>Used for filtering conditional entities. 
		 * Filters are passed from command line</p>
		 * 
		 */
		public String getCondition() {
			return condition;
		}

		public void setCondition(String condition) {
			this.condition = condition;
		}

		/**
		 * <p>Links are translated into (corresponding x3ml class) {@link gr.forth.x3ml.X3ML.Mappings.Mapping.Link} instances</p>
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
		
		public InstanceGenerator getInstanceGenerator(){
			return this.ig;
		}
		
		public void setInstanceGenerator(InstanceGenerator ig){
			this.ig = ig;
		}
	}	

	/**
	 * 
	 * <p>Link class connects <a href="https://www.clarin.eu/content/component-metadata">CLARIN CMDI</a> elements with CIDOC-CRM ontology. 
	 * It uses same elements as <a href="https://www.clarin.eu/">CLARIN</a> mapping between concepts and facets
	 * such as patterns, concepts and blacklistPatterns</p>
	 * 
	 * 
	 * @author dostojic
	 * 
	 */
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Link {
		@XmlElement(name = "entity")
		Collection<ParthenosEntity> entities = new ArrayList<>();
		
		@XmlElement(name = "concept")
		Collection<String> concepts = new ArrayList<>();

		@XmlElement(name = "pattern")
		Collection<String> patterns = new ArrayList<>();

		@XmlElement(name = "blacklistPattern")
		Collection<String> blacklistPatterns = new ArrayList<>();
		
		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer("link:\n");		
			
			for(ParthenosEntity pe: entities)
				sb.append(pe.toString());
			
			for(String concept: concepts)
				sb.append(concept + "\n");
			
			for(String pattern: patterns)
				sb.append(pattern + "\n");
			
			return sb.toString();			
		}

		/** 
		 * <p>In case of multiple entities, intermediate nodes are created. In that case original order is preserved.</p>
		 * 
		 * @return list of {@link ParthenosEntity}
		 * 
		 */
		public Collection<ParthenosEntity> getEntities() {
			return entities;
		}

		public void setEntities(Collection<ParthenosEntity> entities) {
			this.entities = entities;
		}

		/** 
		 * @return list of specified CLARIN concepts
		 * 
		 * <p>Concepts will be "translated" to xpaths for each profile.
		 * One concept can be "translated" to many xpaths.</p>
		 * 
		 */
		public Collection<String> getConcepts() {
			return concepts;
		}

		public void setConcepts(Collection<String> concepts) {
			this.concepts = concepts;
		}

		/** 
		 * <p>For each pattern one link will be created.</p>
		 * 
		 * @return list of specified xpaths in CLARIN profiles
		 *  
		 * @see gr.forth.x3ml.SourceRelationType gr.forth.x3ml.X3ML.Mappings.Mapping.Link.Range
		 * 
		 */
		public Collection<String> getPatterns() {
			return patterns;
		}

		public void setPatterns(Collection<String> patterns) {
			this.patterns = patterns;
		}

		/** 
		 * <p>Patterns derived from concepts, that are specified in blacklist will be removed from further translation into x3ml classes</p>
		 * 
		 * @return blacklist of xpaths
		 * 
		 */
		public Collection<String> getBlacklistPatterns() {
			return blacklistPatterns;
		}

		public void setBlacklistPatterns(Collection<String> blacklistPatterns) {
			this.blacklistPatterns = blacklistPatterns;
		}		
	}
/*	
	
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)	
	public static class InstanceGenerator{
		@XmlAttribute 
		String name;
		
		@XmlElement(name = "argument")
		Collection<Argument> arguments;
		
		public String getName(){
			return this.name;
		}
		
		public void setName(String name){
			this.name = name;
		}
		
		public Collection<Argument> getArguments(){
			return this.arguments;
		}
		
		public void setArguments(Collection<Argument> arguments){
			this.arguments = arguments;
		}
	
		
		
	}
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Argument{
		@XmlAttribute
		String name; 
		
		@XmlAttribute
		String type;
		
		@XmlValue
		String value;
		
		public String getName(){
			return this.name;
		}
		
		public void setName(String name){
			this.name = name;
		}
		
		public String getType(){
			return this.type;
		}
		
		public void setType(String type){
			this.type = type;
		}
		
		public String getValue(){
			return this.value;
		}
		
		public void setValue(String value){
			this.value = value;
		}
	}
*/
}
