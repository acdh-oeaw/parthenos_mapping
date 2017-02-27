package at.ac.acdh.concept_mapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.ac.acdh.concept_mapping.CMDI2CIDOCMap.Intermediate;

public class Profile2CIDOCMap {
	
	private String sourceNode;
	
	private Map<String, List<PropertyMap>> mappings = new HashMap<>();
	
	public String getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(String sourceNode) {
		this.sourceNode = sourceNode;
	}
	
	public Map<String, List<PropertyMap>> getMappings() {
		return mappings;
	}

	public void setMappings(Map<String, List<PropertyMap>> mappings) {
		this.mappings = mappings;
	}

	public static class PropertyMap {
		String name;
		String relationship;
		String type;
		
		List<Intermediate> intermadiates;
		
		private Collection<String> patterns;
		
		public PropertyMap(String name, String relationship, Collection<String> patterns, List<Intermediate> intermadiates, String type) {
			this.name = name;
			this.relationship = relationship;
			this.type = type;
			
			this.patterns = patterns;
			this.intermadiates = intermadiates;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getRelationship() {
			return relationship;
		}

		public void setRelationship(String relationship) {
			this.relationship = relationship;
		}		
		

		public List<Intermediate> getIntermadiates() {
			return intermadiates;
		}

		public void setIntermadiates(List<Intermediate> intermadiates) {
			this.intermadiates = intermadiates;
		}

		public Collection<String> getPatterns() {
			return patterns;
		}

		public void setPatterns(Collection<String> patterns) {
			this.patterns = patterns;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
		
		
	}

}
