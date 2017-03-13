package at.ac.acdh.concept_mapping;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
	

	public Collection<ParthenosEntity> getEntities() {
		return entities;
	}

	public void setEntities(Collection<ParthenosEntity> entities) {
		this.entities = entities;
	}

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

		public String getXpath() {
			return xpath;
		}

		public void setXpath(String xpath) {
			this.xpath = xpath;
		}

		public String getRelationship() {
			return relationship;
		}

		public void setRelationship(String relationship) {
			this.relationship = relationship;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getHasType() {
			return hasType;
		}

		public void setHasType(String hasType) {
			this.hasType = hasType;
		}

		public String getVar() {
			return var;
		}

		public void setVar(String var) {
			this.var = var;
		}

		public String getGlobVar() {
			return globVar;
		}

		public void setGlobVar(String globVar) {
			this.globVar = globVar;
		}

		public String getCondition() {
			return condition;
		}

		public void setCondition(String condition) {
			this.condition = condition;
		}

		public Collection<Link> getLinks() {
			return links;
		}

		public void setLinks(Collection<Link> links) {
			this.links = links;
		}		
	}	

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

		public Collection<ParthenosEntity> getEntities() {
			return entities;
		}

		public void setEntities(Collection<ParthenosEntity> entities) {
			this.entities = entities;
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
	}

}
