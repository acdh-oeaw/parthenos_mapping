package at.ac.acdh.transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import at.ac.acdh.concept_mapping.CMDI2CIDOCMap;
import at.ac.acdh.concept_mapping.Link;
import at.ac.acdh.concept_mapping.ParthenosArg;
import at.ac.acdh.concept_mapping.ParthenosEntity;
import at.ac.acdh.concept_mapping.ParthenosLabelGenerator;
import at.ac.acdh.profile_parser.ParsedProfile;

import org.slf4j.*;

/**
 * @author dostojic
 * 
 * The Normalizer class processes {@link CMDI2CIDOCMap} object by performing following actions
 * <ul>
 * <li>filters conditional entities with conditions provided by caller</li>
 * <li>replaces ?val variable in profile specific xpaths</li>
 * <li>converts concepts into patterns</li>
 * <li>filters patterns with blackListPatterns</li>
 * <li>removes all patterns that don't exist in profile</li> 
 * </ul>
 *
 * @see ProfileTransformer
 */

public class Normalizer {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * see class description for details
	 * 
	 * 
	 * @param map
	 * @param parsedProfile
	 * @param conditions
	 */
	public void normalise(CMDI2CIDOCMap map, ParsedProfile parsedProfile, List<String> conditions){
		
		filterConditionalEntities(map.getEntities(), conditions);		
		String replacement = getReplacement(parsedProfile);
		resolveXpaths(map.getEntities(), replacement);
		List<Link> allLinks = getLinks(map.getEntities());		
		if(allLinks != null){
			for(Link link: allLinks){
				convertConceptsToXPaths(link, parsedProfile);
				filterWithBlackListPatterns(link);
				filterNonExistingPatterns(link, parsedProfile);
			}
		}
		
		processArgsConcept(map.getEntities(), parsedProfile);
	}
	
	/**
	 * Creates replacement for ?val variable 
	 * 
	 * @param parsedProfile
	 * @return part of profile specific xpath that comes after /cmd:CMD/cmd:Components/ part
	 */	
	public String getReplacement(ParsedProfile parsedProfile){
		final String prefix = "/cmd:CMD/cmd:Components/";
		String replacement = parsedProfile.getXPaths().stream().filter(xpath -> xpath.startsWith(prefix)).findFirst().orElse(null);
		if(replacement != null){
			replacement = replacement.substring(prefix.length());
			replacement = replacement.substring(0, replacement.indexOf('/'));
			return replacement;
		}else{
			throw new RuntimeException("/cmd:CMD/cmd:Components/ xpaths are missing");
		}
	}
	
	/**
	 * removes conditional entities with their sub-tree from {@link CMDI2CIDOCMap} 
	 * object if their conditions are not provided in parameter.
	 * 
	 * @param entities
	 * @param conditions
	 */
	public void filterConditionalEntities(Collection<ParthenosEntity> entities, List<String> conditions) {
		if(entities == null)
			return;
		
		
		Iterator<ParthenosEntity> entitiesIterator = entities.iterator();
		
		while (entitiesIterator.hasNext()) {
			ParthenosEntity entity = entitiesIterator.next();
			if(entity.getCondition() != null && (conditions == null || conditions.isEmpty() || !conditions.contains(entity.getCondition()))){
				entitiesIterator.remove();
				continue;
			}
			
			if(entity.getLinks() != null){
				for(Link link: entity.getLinks()){
					filterConditionalEntities(link.getEntities(), conditions); //!recursive call
				}
			}
		}
	}
	
	/**
	 * for each entity replaces "?profileRoot" with the given string
	 * 
	 * @param entities
	 * @param replacement - string to replace "?profileRoot"
	 */
	public void resolveXpaths(Collection<ParthenosEntity> entities, String replacement) {
		if(entities == null)
			return;
		
		Iterator<ParthenosEntity> entitiesIterator = entities.iterator();
		
		while (entitiesIterator.hasNext()) {
			ParthenosEntity entity = entitiesIterator.next();
			if(entity.getXpath() != null && entity.getXpath().contains("?profileRoot")){
				entity.setXpath(entity.getXpath().replace("?profileRoot", replacement));
			}			
			
			if(entity.getLinks() != null){
				for(Link link: entity.getLinks()){
					resolveProfileXpath(link, replacement);
					resolveXpaths(link.getEntities(), replacement); //!recursive call
				}
			}			
		}
	}
	
	/**
	 * for the given link and its patterns replaces "?profileRoot" with the given string
	 * 
	 * @param link
	 * @param replacement - string to replace "?profileRoot"
	 */
	public void resolveProfileXpath(Link link, String replacement){
		if(link == null || link.getPatterns() == null)
			return;
		List<String> resolvedXpaths = new ArrayList<>(link.getPatterns().size());		
		
		for(String xpath: link.getPatterns()){
			if(xpath.contains("?profileRoot"))
				resolvedXpaths.add(xpath.replace("?profileRoot", replacement));
			else
				resolvedXpaths.add(xpath);
		}
		
		link.setPatterns(resolvedXpaths);
	}
			

	/**
	 * This method is called recursively to create a list of all links from {@link CMDI2CIDOCMap} object
	 * 
	 * @param entities
	 * @return list of all links for the given list of entities
	 */
	public List<Link> getLinks(Collection<ParthenosEntity> entities) {
		List<Link> links = new ArrayList<>();
		for(ParthenosEntity entity: entities){
			if(entity.getLinks() != null){
				links.addAll(entity.getLinks());
				for(Link link: entity.getLinks()){
					if(link.getEntities() != null){
						links.addAll(getLinks(link.getEntities()));//!recursive call
					}						
				}
			}
		}		
		return links;
	}
	
	/**
	 * This method is called recursively to create a list of all entities from {@link CMDI2CIDOCMap} object
	 * 
	 * @param entities
	 * @return list of sub-entities for the given list of entities
	 */
	public List<ParthenosEntity> getEntities(Collection<ParthenosEntity> entities){
		List<ParthenosEntity> ents = new ArrayList<>();
		for(ParthenosEntity entity: entities){
			if(entity.getLinks() != null){
				for(Link link: entity.getLinks()){
					if(link.getEntities() != null){
						ents.addAll(getEntities(link.getEntities()));//!recursive call
					}
				}
			}
		}
		return ents;
	}
	
	/**
	 * transforms all concepts to patterns for the given link and profile
	 * 
	 * @param link
	 * @param parsedProfile
	 * 
	 * @see ParsedProfile#getXPathsForConcept(String)
	 */
	public void convertConceptsToXPaths(Link link, ParsedProfile parsedProfile){
		if(link.getConcepts() == null)
			return;
		
		for(String concept: link.getConcepts()){
			Collection<String> xpaths = parsedProfile.getXPathsForConcept(((ParthenosEntity)link.getParent()).getXpath(), concept);
			
			
			if(xpaths != null && !xpaths.isEmpty()){ //now identified xpaths replace the patterns
				link.setPatterns(xpaths);
				//stripe xpath
				
/*				if(link.getPatterns() == null){
					link.setPatterns(new ArrayList<>());
				}
				for(String xpath: xpaths){
					if(!link.getPatterns().contains(xpath)){
						link.getPatterns().add(xpath);
					}						
				}	*/			
			}			
		}
	}
	
	/**
	 * Removes all patterns from the given link that are specified in blacklist patterns
	 * 
	 * @param link
	 */
	public void filterWithBlackListPatterns(Link link) {
		if(link.getBlacklistPatterns() == null || link.getPatterns() == null || link.getPatterns().isEmpty())
			return;
		
		for (String blacklistPattern : link.getBlacklistPatterns()) {
			Iterator<String> xpathIterator = link.getPatterns().iterator();
			
			while (xpathIterator.hasNext()) {
				String xpath = xpathIterator.next();
				if (xpath.contains(blacklistPattern)) {
					xpathIterator.remove();
				}
			}
		}		
	}
	
	/**
	 * removes all non existing patterns for the given link and profile
	 * 
	 * @param link 
	 * @param parsedProfile
	 *  
	 * @see ParsedProfile#hasXPath(String)
	 */
	public void filterNonExistingPatterns(Link link, ParsedProfile parsedProfile) {
		if(link.getPatterns() == null || link.getPatterns().isEmpty())
			return;		
		
		Iterator<String> xpathIterator = link.getPatterns().iterator();
		while (xpathIterator.hasNext()) {
			String xpath = xpathIterator.next();
			if(xpath.startsWith("..")) //allow xpath starting with dots like ../xx/yy
			    xpath = xpath.substring(2);
			if(xpath.startsWith("//")) //allow xpath starting with double slash like //xx/yy
			    xpath = xpath.substring(1);
			boolean hit = false;
			for(String profileXpath: parsedProfile.getXPaths()){				
				if (profileXpath.contains(xpath)){
				//if (profileXpath.endsWith(xpath)){
					hit = true;
					break;
				}
			}
			if(!hit){
				logger.info("removing xpath \"" + xpath +"\" since it has no match in the profile");
				xpathIterator.remove();
			}
		}
	}
	
	public void processArgsConcept(Collection<ParthenosEntity> entities, ParsedProfile parsedProfile) {
	    for(ParthenosEntity pe : entities) {
	        if(pe.getLinks() != null) {
	            for(Link link : pe.getLinks()) {
	                processArgsConcept(link.getEntities(), parsedProfile);
	            }
	        }
	        for(ParthenosLabelGenerator plg:pe.getLabelGenerator()) {
	            for(ParthenosArg parg:plg.getArgs()) {
	                if(!parg.getConcepts().isEmpty() || !parg.getPatterns().isEmpty())
	                    parg.getContent().clear(); //since it's a mixed element there might be some empty Strings as content
	                
	                for(String concept : parg.getConcepts()) {
	                    Collection<String> xpaths = parsedProfile.getXPathsForConcept(concept);
	                    if(xpaths != null && !xpaths.isEmpty()) {
	                        parg.getContent().addAll(xpaths);
	                    }
	                }
	                
	                for(String blacklistPattern: parg.getBlacklistPatterns()) {
	                    parg.getContent().remove(blacklistPattern);
	                }
	                
	                for(String pattern : parg.getPatterns()) {
	                    parg.getContent().add(pattern);
	                }
	            }
	        }
	    }
	}
}
