package ac.at.acdh.concept_mapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ximpleware.VTDException;

import ac.at.acdh.concept_mapping.CMDI2CIDOCMap.ParthenosEntity;
import ac.at.acdh.concept_mapping.CMDI2CIDOCMap.Property;
import ac.at.acdh.concept_mapping.Profile2CIDOCMap.PropertyMap;
import ac.at.acdh.profile_parser.ParsedProfile;
import ac.at.acdh.profile_parser.ParsedProfileFactory;

public class ConceptMappingFactory {
	
	public static Profile2CIDOCMap getMapping(String profileSchemaUrl) throws VTDException{
		CMDI2CIDOCMap xml = Concepts2CIDOCFactory.unmarshall();
		ParsedProfile parsedProfile = ParsedProfileFactory.parse(profileSchemaUrl, true);
		
		//leave only xpath in profile specific section
		
		Profile2CIDOCMap profileMapping = new Profile2CIDOCMap();
		
		String domainNode = parsedProfile.getXPaths().stream().filter(xpath -> xpath.startsWith("/cmd:CMD/cmd:Components/")).findFirst().orElse(null);
		if(domainNode != null){
			domainNode = domainNode.substring(0, domainNode.indexOf('/', "/cmd:CMD/cmd:Components/".length() + 1));
		}else{
			throw new RuntimeException("/cmd:CMD/cmd:Components/ xpaths are missing");
		}
		
		profileMapping.setSourceNode(domainNode);
		
		
		for(ParthenosEntity entity: xml.entities){
			List<PropertyMap> propertyMapList = new ArrayList<>();
			for(Property property: entity.properties){
				Set<String> patterns = new HashSet<>();
				
				//handle concepts
				for(String concept: property.concepts){
					parsedProfile.getXPathsForConcept(concept).forEach(xpath -> patterns.add(xpath));					
				}
				
				// pattern-based blacklisting: remove all XPath expressions
				// that contain a blacklisted substring;
				// this is basically a hack to enhance the quality of the
				// visualised information in the VLO;
				// should be replaced by a more intelligent approach in the future
				for (String blacklistPattern : property.blacklistPatterns) {
					//Iterator<String> xpathIterator = xpaths.iterator();
					Iterator<String> xpathIterator = patterns.iterator();
					while (xpathIterator.hasNext()) {
						String xpath = xpathIterator.next();
						if (xpath.contains(blacklistPattern)) {
							//_logger.debug("Rejecting {} because of blacklisted substring {}", xpath, blacklistPattern);
							xpathIterator.remove();
							
							//remove it from map as well
							patterns.remove(xpath);
						}
					}
				}
				//add fallback patterns to the xpath list
				for (String fallbackPattern : property.patterns) {
					if(parsedProfile.hasXPath(fallbackPattern)){
						patterns.add(fallbackPattern);
					}
				}
				
				propertyMapList.add(new PropertyMap(property.name, property.relationship, patterns, property.intermediate));		
			}
			profileMapping.getMappings().put(entity.name, propertyMapList);
		}
		
		return profileMapping;
		
	}

}
