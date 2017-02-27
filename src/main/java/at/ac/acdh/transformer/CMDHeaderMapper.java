package at.ac.acdh.transformer;


import java.util.ArrayList;
import java.util.List;

import at.ac.acdh.concept_mapping.Profile2CIDOCMap;
import at.ac.acdh.transformer.utils.CMDICreatorType;
import at.ac.acdh.transformer.utils.CMDIResourceType;
import at.ac.acdh.transformer.utils.LinkNode;
import at.ac.acdh.transformer.utils.TemplateGenerator;
import at.ac.acdh.transformer.utils.X3MLAdapter;
import at.ac.acdh.transformer.utils.LinkNode.ObjectNode;
import gr.forth.x3ml.DomainTargetNodeType;
import gr.forth.x3ml.Entity;
import gr.forth.x3ml.X3ML.Mappings.Mapping;
import gr.forth.x3ml.X3ML.Mappings.Mapping.Domain;

class CMDHeaderMapper extends MappingGenerator{
	
	private CMDICreatorType creator;
	
	
	public CMDHeaderMapper(Profile2CIDOCMap profileMapping, String resourceType,
			X3MLAdapter adapter, TemplateGenerator templGen, CMDICreatorType creator) {
		
		super(profileMapping, resourceType, adapter, templGen);
		this.creator = creator;
	}	


	@Override
	public List<Mapping> transformToMappings(){
		
		List<Mapping> mappings = new ArrayList<>();
		
		mappings.add(mapInstanceToPersistantDS());
		mappings.add(creatorMapping());	
		
		return mappings;
	}
	
	private Mapping mapInstanceToPersistantDS(){
		
		Mapping cmd = new Mapping();
		
		//CMD record is crmpe:PE22_Persistent_Dataset 
		//with hasType metadata
		Entity metadataEntity = adapter.createEntity(
			"crmpe:PE22_Persistent_Dataset", 
			templGen.localTermURI("cmdi-record", "cmd:Header/cmd:MdSelfLink/text()"),
			null,
			templGen.hasTypeTemplate("metadata")
		);		
		
		DomainTargetNodeType metadataDomain = new DomainTargetNodeType();
		metadataDomain.setEntity(metadataEntity);		
		Domain cmdDomain = new Domain("/cmd:CMD", metadataDomain);
		cmd.setDomain(cmdDomain);		
		
		//CMD identifier
		entity = adapter.createEntity("crm:E42_Identifier", templGen.localTermURI("id"), templGen.simpleLabelLG());
		link = new LinkNode("cmd:Header/cmd:MdSelfLink", new ObjectNode("crm:P1_is_identified_by", entity));
		cmd.getLink().add(link.toLink());
		
		
		//CMD partof Collection
		entity = adapter.createEntity(
			"crmpe:PE24_Volatile_Dataset", templGen.localTermURI("cmdi-collection"), 
			templGen.simpleLabelLG(), templGen.hasTypeTemplate("collection")
		);
		link = new LinkNode("cmd:Header/cmd:MdCollectionDisplayName", new ObjectNode("crmpe:PP23i_is_dataset_part_of", entity));
		cmd.getLink().add(link.toLink());
		
		
		//created in crmdig:D7_Digital_Machine_Event
		entity = adapter.createEntity("crmdig:D7_Digital_Machine_Event",
			adapter.crateInstanceGenerator("UUID", null),
			templGen.simpleLabelLG("cmd:MdSelfLink/text()")
		);
		link = new LinkNode("cmd:Header", new ObjectNode("crmdig:L11i_was_output_of", entity));
		cmd.getLink().add(link.toLink());
			
		
		String relation = resourceType.equals(CMDIResourceType.SERVICE)? "crm:P129_is_about" : "crmpe:PP39_is_metadata_for";		
		Entity mainEntity = adapter.createEntity(resourceType, adapter.crateInstanceGenerator("UUID", null), null);
		mainEntity.setGlobalVariable("main_entity");
		
		
		//CMDI is metadata for resProxy part
		link = new LinkNode("cmd:Resources/cmd:ResourceProxyList/cmd:ResourceProxy", new ObjectNode(relation, mainEntity));		
		
		entity = adapter.createEntity("crmpe:PE15_Data_E-Service", adapter.crateInstanceGenerator("UUID", null), null);
		entity.setGlobalVariable("host");
		link.addObjects(new ObjectNode("crmpe:PP8i_is_dataset_hosted_by", entity));
		
		entity = adapter.createEntity("crmpe:PE29_Access_Point", adapter.crateInstanceGenerator("UUID", null), null);
		link.addObjects(new ObjectNode("crmpe:PP28_has_designated_access_point", entity));

		cmd.getLink().add(link.toLink());		
		
		
		//CMDI is metadata for components part
		String relativeXpath = profileMapping.getSourceNode().substring("/cmd:CMD/".length());
		
		link = new LinkNode(relativeXpath, new ObjectNode(relation, mainEntity));
		cmd.getLink().add(link.toLink());
		
		
		return cmd;				
	}
	
	private Mapping creatorMapping(){
		Mapping profile = new Mapping();
		
		//creation of record on date using schema
			
		DomainTargetNodeType metadataDomain = new DomainTargetNodeType();
		metadataDomain.setEntity(
			adapter.createEntity("crmdig:D7_Digital_Machine_Event", adapter.crateInstanceGenerator("UUID", null), null)
		);		
		Domain cmdDomain = new Domain("/cmd:CMD/cmd:Header", metadataDomain);
		profile.setDomain(cmdDomain);
		
		//creation date		
		entity = adapter.createEntity("crm:E52_Time-Span", adapter.crateInstanceGenerator("UUID", null), null);
		link = new LinkNode("cmd:MdCreationDate", new ObjectNode("crm:P4_has_time-span", entity));
		entity = adapter.createEntity("http://www.w3.org/2000/01/rdf-schema#Literal", templGen.simpleLabelIG(), null);
		link.addObjects(new ObjectNode("crm:P82_at_some_time_within", entity));		
		profile.getLink().add(link.toLink());
		
		
		if(creator.equals(CMDICreatorType.SOFTWARE)){
			//cmdi was created by software
			entity = adapter.createEntity("crmdig:D14_Software", templGen.localTermURI("software"), templGen.simpleLabelLG());
			link = new LinkNode("cmd:MdCreator", new ObjectNode("crmdig:L23_used_software_or_firmware", entity));			
		}else{// -||- by actor
			entity = adapter.createEntity("crm:E39_Actor", templGen.localTermURI("actor"), templGen.simpleLabelLG());
			link = new LinkNode("cmd:MdCreator", new ObjectNode("crm:P11_had_participant", entity));	
		}
		profile.getLink().add(link.toLink());	
		
		//profile (Software)
		entity = adapter.createEntity("crmdig:D14_Software", templGen.localTermURI("software"), templGen.simpleLabelLG(), templGen.hasTypeTemplate("cmdi-profile"));
		link = new LinkNode("cmd:MdProfile", new ObjectNode("crmdig:L23_used_software_or_firmware", entity));
		profile.getLink().add(link.toLink());		
		
		return profile;
	}
	
}
