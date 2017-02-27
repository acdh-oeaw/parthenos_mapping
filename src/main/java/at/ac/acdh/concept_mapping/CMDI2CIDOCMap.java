package at.ac.acdh.concept_mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "mappings")
@XmlAccessorType(XmlAccessType.FIELD)
public class CMDI2CIDOCMap {

	@XmlElement(name = "entity")
	Collection<ParthenosEntity> entities = new ArrayList<>();

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (entities != null)
			entities.forEach(e -> sb.append(e.toString()));
		return sb.toString();
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class ParthenosEntity {
		@XmlAttribute
		String name;		
		
		@XmlElementWrapper(name = "properties")
		@XmlElement(name = "property")
		Collection<Property> properties = new ArrayList<>();
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("entity: " + name + "\n");
			properties.forEach(sb::append);
			return sb.toString();
		}
		
	}
	

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Property {
		@XmlAttribute
		String name;
		
		@XmlAttribute
		String relationship;
		
		@XmlAttribute
		String type;
		
		@XmlElement(name = "intermediate")
		List<Intermediate> intermediates;

		@XmlElement(name = "concept")
		Collection<String> concepts = new ArrayList<>();

		@XmlElement(name = "pattern")
		Collection<String> patterns = new ArrayList<>();

		@XmlElement(name = "blacklistPattern")
		Collection<String> blacklistPatterns = new ArrayList<>();

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("\tproperty: -> " + relationship + " -> " + name + "\n");
			if (concepts != null)
				concepts.forEach(c -> sb.append("\t\t").append("concept: " + c).append("\n"));
			if (patterns != null)
				patterns.forEach(p -> sb.append("\t\t").append("pattern: " + p).append("\n"));
			if (blacklistPatterns != null)
				blacklistPatterns.forEach(b -> sb.append("\t\t").append("black pattern: " + b).append("\n"));

			return sb.toString();
		}
	}
	
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Intermediate {
		
		@XmlAttribute
		String type;
		
		@XmlAttribute
		String relationship;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getRelationship() {
			return relationship;
		}

		public void setRelationship(String relationship) {
			this.relationship = relationship;
		}
		
	}

}
