package at.ac.acdh.transformer;

import java.util.ArrayList;
import java.util.List;

import at.ac.acdh.concept_mapping.Profile2CIDOCMap;
import at.ac.acdh.transformer.utils.LinkNode;
import at.ac.acdh.transformer.utils.TemplateGenerator;
import at.ac.acdh.transformer.utils.X3MLAdapter;
import at.ac.acdh.transformer.utils.LinkNode.ObjectNode;
import gr.forth.x3ml.DomainTargetNodeType;
import gr.forth.x3ml.Entity;
import gr.forth.x3ml.X3ML.Mappings.Mapping;
import gr.forth.x3ml.X3ML.Mappings.Mapping.Domain;

class CMDResProxyMapper extends MappingGenerator{	
	
	public CMDResProxyMapper(Profile2CIDOCMap profileMapping, String resourceType, X3MLAdapter adapter,
			TemplateGenerator templGen) {
		super(profileMapping, resourceType, adapter, templGen);
	}

	@Override
	public List<Mapping> transformToMappings() {
		List<Mapping> mappings = new ArrayList<>();
		
		Mapping resProxyMapping = new Mapping();		
		
		Entity entity = adapter.createEntity("crmpe:PE29_Access_Point", adapter.crateInstanceGenerator("UUID", null), null);
		
		DomainTargetNodeType target = new DomainTargetNodeType();
		target.setEntity(entity);
		Domain domain = new Domain("//cmd:CMD/cmd:Resources/cmd:ResourceProxyList/cmd:ResourceProxy", target);		
		resProxyMapping.setDomain(domain);
		
		
		//ResourceType -> P2_has_type -> E55_Type
		entity = adapter.createEntity("crm:E55_Type", adapter.crateInstanceGenerator("UUID", null), null);
		LinkNode link = new LinkNode("cmd:ResourceType", new ObjectNode("crm:P2_has_type", entity));
		resProxyMapping.getLink().add(link.toLink());
		
		
		//Entity is identified with ResourceRef
		entity = adapter.createEntity(resourceType, adapter.crateInstanceGenerator("UUID", null), null);
		entity.setGlobalVariable("main_entity");
		link = new LinkNode("cmd:ResourceRef", new ObjectNode("crm:P1i_identifies", entity));
		resProxyMapping.getLink().add(link.toLink());
		
		//CMDI is metadata for components part
		String relativeXpath = "../../../cmd:Components/" + profileMapping.getSourceNode().substring("/cmd:CMD/".length());
		
		entity = adapter.createEntity("crmpe:PE15_Data_E-Service", adapter.crateInstanceGenerator("UUID", null), null);
		entity.setGlobalVariable("host");
		link = new LinkNode(relativeXpath, new ObjectNode("crmpe:PP28i_is_designated_access_point_of", entity));
		
		entity = adapter.createEntity(resourceType, adapter.crateInstanceGenerator("UUID", null), null);
		entity.setGlobalVariable("main_entity");
		link.addObjects(new ObjectNode("crmpe:PP8_hosts_dataset", entity));
		resProxyMapping.getLink().add(link.toLink());
				
		mappings.add(resProxyMapping);
		return mappings;
	}	

}
