package at.ac.acdh.transformer;

import java.util.ArrayList;
import java.util.List;

import at.ac.acdh.concept_mapping.Profile2CIDOCMap;
import at.ac.acdh.concept_mapping.Profile2CIDOCMap.PropertyMap;
import at.ac.acdh.transformer.utils.LinkNode;
import at.ac.acdh.transformer.utils.TemplateGenerator;
import at.ac.acdh.transformer.utils.X3MLAdapter;
import at.ac.acdh.transformer.utils.LinkNode.ObjectNode;
import gr.forth.x3ml.DomainTargetNodeType;
import gr.forth.x3ml.Entity;
import gr.forth.x3ml.X3ML.Mappings.Mapping;
import gr.forth.x3ml.X3ML.Mappings.Mapping.Domain;

class CMDComponentsMapper extends MappingGenerator{

	public CMDComponentsMapper(Profile2CIDOCMap profileMapping, String resourceType, X3MLAdapter adapter,
			TemplateGenerator templGen) {
		super(profileMapping, resourceType, adapter, templGen);
	}	

	@Override
	public List<Mapping> transformToMappings() {
		List<Mapping> mappings = new ArrayList<>();		
		mappings.add(mapComponents(resourceType));
		return mappings;
	}

	private Mapping mapComponents(String resourceType){
		Mapping cmd = new Mapping();		
		cmd.setDomain(createDomain());				
		
		List<PropertyMap> properties = profileMapping.getMappings().get(resourceType);
		for(PropertyMap propertyMap: properties){			
			for(String xpath: propertyMap.getPatterns()){
				xpath = xpath.replace("text()", "");
				xpath = xpath.substring(profileMapping.getSourceNode().length() + 1, xpath.lastIndexOf('/'));
				
				Entity entity = adapter.createEntity(
					propertyMap.getName(),
					adapter.crateInstanceGenerator("UUID", null),
					templGen.simpleLabelLG(),
					propertyMap.getType() != null? templGen.hasTypeTemplate(propertyMap.getType()) : null
				);			
				LinkNode link = new LinkNode(xpath, new ObjectNode(propertyMap.getRelationship(), entity));
				
				if(propertyMap.getIntermadiates() != null){
					propertyMap.getIntermadiates().forEach(i -> {
						Entity intermNode = adapter.createEntity(i.getType(), adapter.crateInstanceGenerator("UUID", null), null);
						link.addObjects(new ObjectNode(i.getRelationship(), intermNode));
					});
				}
				
				cmd.getLink().add(link.toLink());
			}
		}
		
		return cmd;
	}

	private Domain createDomain() {
		Entity resourceEntity = adapter.createEntity(resourceType, adapter.crateInstanceGenerator("UUID", null), null);
		resourceEntity.setGlobalVariable("main_entity");
		DomainTargetNodeType resourceDomain = new DomainTargetNodeType();
		resourceDomain.setEntity(resourceEntity);
		return new Domain(profileMapping.getSourceNode(), resourceDomain);
	}

}
