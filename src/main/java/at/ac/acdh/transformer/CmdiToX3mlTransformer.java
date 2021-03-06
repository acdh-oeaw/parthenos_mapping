package at.ac.acdh.transformer;

import java.util.ArrayList;
import java.util.List;

import at.ac.acdh.concept_mapping.CMDI2CIDOCMap;
import at.ac.acdh.concept_mapping.Link;
import at.ac.acdh.concept_mapping.ParthenosEntity;
import at.ac.acdh.transformer.utils.LinkNode;
import at.ac.acdh.transformer.utils.LinkNode.ObjectNode;
import at.ac.acdh.transformer.utils.X3mlFacade;
import gr.forth.x3ml.Entity;
import gr.forth.x3ml.X3ML.Mappings;
import gr.forth.x3ml.X3ML.Mappings.Mapping;

/**
 * This class is responsible for processing {@link ParthenosEntity} and {@link Link} objects 
 * and converting them to objects from X3ML realm
 * 
 * 
 * @see at.ac.acdh.concept_mapping  gr.forth.x3ml
 * 
 * 
 * @author dostojic
 *
 */
public class CmdiToX3mlTransformer {
	
	protected X3mlFacade x3mlFacade = new X3mlFacade();
	
	List<Mapping> x3mlMappings = new ArrayList<>();
	
	/**
	 * 
	 * @param xmlMappings - {@link CMDI2CIDOCMap} object
	 * @return {@link Mappings}
	 */
	public Mappings transform(CMDI2CIDOCMap xmlMappings){
		Mappings mappings = new Mappings();
		
		for(ParthenosEntity pe: xmlMappings.getEntities())
			createMapping(pe);
		
		mappings.getMapping().addAll(x3mlMappings);		
		return mappings;
	}
	
	/**
	 * This method processes entity elements from xml and for each creates {@link Mapping} object
	 * 
	 * @param pe - {@link ParthenosEntity}, entity element in mapping xml
	 */
	private void createMapping(ParthenosEntity pe){	
		Mapping mapping =  x3mlFacade.createMapping(pe);
		x3mlMappings.add(mapping);
		
		//process links
		if(pe.getLinks() != null){
			for(Link link: pe.getLinks()){
				mapping.getLink().addAll(createLink(link));
			}
		}		
	}
	
	/**
	 * This method processes link elements from xml and for each link and for each pattern
	 *  creates {@link gr.forth.x3ml.X3ML.Mappings.Mapping.Link}
	 * 
	 * @param link - link element in mapping xml
	 * @return list of x3ml links, one for each pattern in link element
	 */
	public List<gr.forth.x3ml.X3ML.Mappings.Mapping.Link> createLink(Link link){
		
		//preserve order of entities in map
		List<ObjectNode> relationships = new ArrayList<>();
		if(link.getEntities() != null){
			for(ParthenosEntity pe: link.getEntities()){
				Entity entity = x3mlFacade.createEntity(pe);
				relationships.add(new ObjectNode(pe.getRelationship(), entity));

				
				if(pe.getXpath() != null)
					createMapping(pe);//create new mapping 
			}
		}
		
		
		List<gr.forth.x3ml.X3ML.Mappings.Mapping.Link> links = new ArrayList<>();
		
		if(link.getPatterns() != null){
			//for each pattern we need to create one x3ml link
			for(String pattern: link.getPatterns()){
				LinkNode linkNode = new LinkNode(pattern, relationships);
				links.add(linkNode.toLink());
			}
		}		
		
		return links;		
	}

}
