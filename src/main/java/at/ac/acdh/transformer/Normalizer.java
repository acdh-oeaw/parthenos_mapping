package at.ac.acdh.transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import at.ac.acdh.concept_mapping.CMDI2CIDOCMap;
import at.ac.acdh.concept_mapping.CMDI2CIDOCMap.Link;
import at.ac.acdh.concept_mapping.CMDI2CIDOCMap.ParthenosEntity;
import at.ac.acdh.profile_parser.ParsedProfile;

/**
 * @author dostojic
 * 
 * The Normalizer class does following
 * # filters conditional entities
 * # replaces xpaths containing ?val
 * # converts concepts into xpaths
 * # filters xpaths with blackListPatterns
 * # removes all patterns that don't exist in profile
 *
 */

public class Normalizer {
	
	public void normalise(CMDI2CIDOCMap map, ParsedProfile parsedProfile, List<String> conditions){
		
		filterConditionalEntities(map.getEntities(), conditions);
		//replaceComponentsWithXPaths(map.getEntities(), parsedProfile);
		
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
	}
	

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
	
	public void filterConditionalEntities(Collection<ParthenosEntity> entities, List<String> conditions) {
		if(entities == null || conditions == null || conditions.isEmpty())
			return;
		
		Iterator<ParthenosEntity> entitiesIterator = entities.iterator();
		
		while (entitiesIterator.hasNext()) {
			ParthenosEntity entity = entitiesIterator.next();
			if(entity.getCondition() != null && !conditions.contains(entity.getCondition())){
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
	
	public void convertConceptsToXPaths(Link link, ParsedProfile parsedProfile){
		if(link.getConcepts() == null)
			return;
		
		for(String concept: link.getConcepts()){
			Collection<String> xpaths = parsedProfile.getXPathsForConcept(concept);
			if(xpaths != null){
				//stripe xpath
				
				if(link.getPatterns() == null){
					link.setPatterns(new ArrayList<>());
				}
				for(String xpath: xpaths){
					if(!link.getPatterns().contains(xpath)){
						link.getPatterns().add(xpath);
					}						
				}				
			}			
		}
	}
	
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
	
	/*
	 * remove all patterns that don't exist in profile's xsd
	 */
	public void filterNonExistingPatterns(Link link, ParsedProfile parsedProfile) {
		if(link.getPatterns() == null || link.getPatterns().isEmpty())
			return;		
		
		Iterator<String> xpathIterator = link.getPatterns().iterator();
		while (xpathIterator.hasNext()) {
			String xpath = xpathIterator.next();
			boolean hit = false;
			for(String profileXpath: parsedProfile.getXPaths()){				
				if (profileXpath.contains(xpath)){
					hit = true;
					break;
				}
			}
			if(!hit){
				xpathIterator.remove();
			}
		}
	}

}
