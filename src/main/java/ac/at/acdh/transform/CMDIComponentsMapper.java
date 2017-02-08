package ac.at.acdh.transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import at.ac.acdh.concept_mapping.Profile2CIDOCMap;
import at.ac.acdh.concept_mapping.Profile2CIDOCMap.PropertyMap;
import at.ac.acdh.transform.utils.CMDIResourceType;
import at.ac.acdh.transform.utils.TemplateGenerator;
import at.ac.acdh.transform.utils.X3MLUtils;
import gr.forth.x3ml.DomainTargetNodeType;
import gr.forth.x3ml.Entity;
import gr.forth.x3ml.X3ML.Mappings.Mapping;
import gr.forth.x3ml.X3ML.Mappings.Mapping.Domain;
import gr.forth.x3ml.X3ML.Mappings.Mapping.Link.Path;

public class CMDIComponentsMapper {
	

	public static Map<CMDIResourceType, String> resourceTypeMap = new HashMap<>();
	static {
		resourceTypeMap.put(CMDIResourceType.DATASET, "crmpe:PE24_Volatile_Dataset");
		resourceTypeMap.put(CMDIResourceType.SERVICE, "crmpe:PE8_E-Service");
		resourceTypeMap.put(CMDIResourceType.SOFTWARE, "crmdig:D14_Software");
	}

	private Profile2CIDOCMap profileMapping;
	private CMDIResourceType resourceType;

	private X3MLUtils utils = new X3MLUtils();
	private TemplateGenerator templGen = new TemplateGenerator();

	public CMDIComponentsMapper(Profile2CIDOCMap profileMapping) {
		this.profileMapping = profileMapping;
	}

	public List<Mapping> getMappings(CMDIResourceType resourceType) {
		List<Mapping> mappings = new ArrayList<>();
		this.resourceType = resourceType;
		
		mappings.add(createMapping(resourceType));

		return mappings;
	}

	private Mapping createMapping(CMDIResourceType resourceType){
		Mapping cmd = new Mapping();		
		cmd.setDomain(createDomain());
				
		
		List<PropertyMap> properties = profileMapping.getMappings().get(resourceType.toString().toLowerCase());
		for(PropertyMap propertyMap: properties){			
			for(String xpath: propertyMap.getPatterns()){
				xpath = xpath.replace("text()", "");
				xpath = xpath.substring(profileMapping.getSourceNode().length() + 1, xpath.lastIndexOf('/'));
				
				Path path;
				if(propertyMap.getIntermadiate() == null){					
					path = utils.createPath(xpath, propertyMap.getRelationship(), null);
				}else{
					path = utils.createPathWithIntermediate(
						xpath, 
						propertyMap.getIntermadiate().getRelationship(), 
						propertyMap.getRelationship(), 
						utils.createEntity(propertyMap.getIntermadiate().getType(), utils.crateInstanceGenerator("UUID", null), null, null));
				}
				
				cmd.getLink().add(
					utils.createLink(
						path, utils.createRange(xpath, utils.createEntity(propertyMap.getName(), utils.crateInstanceGenerator("UUID", null), templGen.simpleLabelLG()))
					)
				);				
			}
		}
		
		return cmd;
	}

	private Domain createDomain() {
		Entity resourceEntity = utils.createEntity(
			resourceTypeMap.get(resourceType),
			utils.crateInstanceGenerator("UUID", null),
			null, null);

		DomainTargetNodeType resourceDomain = new DomainTargetNodeType();
		resourceDomain.setEntity(resourceEntity);
		return new Domain(profileMapping.getSourceNode(), resourceDomain);
	}

}
