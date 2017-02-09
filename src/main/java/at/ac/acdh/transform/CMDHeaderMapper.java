package at.ac.acdh.transform;


import java.util.ArrayList;
import java.util.List;

import at.ac.acdh.concept_mapping.Profile2CIDOCMap;
import at.ac.acdh.transform.utils.CMDICreatorType;
import at.ac.acdh.transform.utils.CMDIResourceType;
import at.ac.acdh.transform.utils.TemplateGenerator;
import at.ac.acdh.transform.utils.X3MLUtils;
import gr.forth.x3ml.DomainTargetNodeType;
import gr.forth.x3ml.Entity;
import gr.forth.x3ml.X3ML.Mappings.Mapping;
import gr.forth.x3ml.X3ML.Mappings.Mapping.Domain;
import gr.forth.x3ml.X3ML.Mappings.Mapping.Link.Path;

public class CMDHeaderMapper {
	
	
	
	private X3MLUtils utils = new X3MLUtils();
	private TemplateGenerator templGen = new TemplateGenerator();
	
	private Profile2CIDOCMap profileMapping;
	private CMDICreatorType creator;
	private CMDIResourceType resourceType;
	
	
	public CMDHeaderMapper(Profile2CIDOCMap profileMapping) {
		this.profileMapping = profileMapping;
	}
	
	public List<Mapping> getMappings(CMDICreatorType creator, CMDIResourceType resourceType){
		this.creator = creator;
		this.resourceType = resourceType;
		
		List<Mapping> mappings = new ArrayList<>();
		
		mappings.add(mapInstanceToPersistantDS());
		mappings.add(creatorMapping());	
		
		return mappings;
	}	
	
	private Mapping mapInstanceToPersistantDS(){
		
		Mapping cmd = new Mapping();
		
		//create crmpe:PE22_Persistent_Dataset from CMD record
		//with hasType metadata
		Entity metadataEntity = utils.createEntity(
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
		cmd.getLink().add(
			utils.createLink(
				utils.createPath("cmd:Header/cmd:MdSelfLink", "crm:P1_is_identified_by", null),		
				utils.createRange("cmd:Header/cmd:MdSelfLink", 
					utils.createEntity("crm:E42_Identifier", templGen.localTermURI("id"), templGen.simpleLabelLG())
				)
			)
		);		
		
		//CMD partof COllection
		cmd.getLink().add(
			utils.createLink(
				utils.createPath("cmd:Header/cmd:MdCollectionDisplayName", "crmpe:PP23i_is_dataset_part_of", null),
				utils.createRange("cmd:Header/cmd:MdCollectionDisplayName", 
					utils.createEntity(
						"crmpe:PE24_Volatile_Dataset", templGen.localTermURI("cmdi-collection"), 
						templGen.simpleLabelLG(), templGen.hasTypeTemplate("collection")
					)
				)
			)
		);
		
		//crmdig:D7_Digital_Machine_Event for modeling 
		cmd.getLink().add(
			utils.createLink(
				utils.createPath("cmd:Header", "crmdig:L11i_was_output_of", null),
				utils.createRange("cmd:Header", 
					utils.createEntity("crmdig:D7_Digital_Machine_Event",utils.crateInstanceGenerator("UUID", null),
						templGen.simpleLabelLG("cmd:MdSelfLink/text()")
		//.getArg().add(utils.crateArg("label", "constant", "Creation of CDMI record") nice to have but it is just for description
					)
				)
			)
		);
		

		/*
		//crmdig: cmdirecord is metadata for resource
		cmd.getLink().add(
			utils.createLink(
				utils.createPath("cmd:Resources", "crm:P129_is_about", null),
				utils.createRange("cmd:Resources", 
					utils.createEntity(
						"crmpe:PE8_E-Service", //or whatever
						templGen.localTermURI("service", "/cmd:CMD/cmd:Resources/cmd:ResourceProxyList/cmd:ResourceProxy/cmd:ResourceRef/text()"),
						null,
						null
					)
				)
			)
		);		
		*/
		
		//CMDI is metadata for 
		String relativeXpath = profileMapping.getSourceNode().substring("/cmd:CMD/".length());
		String relation = resourceType.equals(CMDIResourceType.SERVICE)? "crm:P129_is_about" : "crmpe:PP39_is_metadata_for";
		cmd.getLink().add(
				utils.createLink(
					utils.createPath(relativeXpath, relation, null),
					utils.createRange(relativeXpath, 
						utils.createEntity(CMDIComponentsMapper.resourceTypeMap.get(resourceType), utils.crateInstanceGenerator("UUID", null), null))
				)
			);
		
		return cmd;
				
	}
	
	private Mapping creatorMapping(){
		
		Mapping profile = new Mapping();		
		
				
		//creation of record on date using schema 
			
		DomainTargetNodeType metadataDomain = new DomainTargetNodeType();
		metadataDomain.setEntity(
				utils.createEntity("crmdig:D7_Digital_Machine_Event", utils.crateInstanceGenerator("UUID", null), null, null)
		);		
		Domain cmdDomain = new Domain("/cmd:CMD/cmd:Header", metadataDomain);
		profile.setDomain(cmdDomain);
		
		//creation date
		
		profile.getLink().add(
			utils.createLink(
				utils.createPathWithIntermediate("cmd:MdCreationDate", "crm:P4_has_time-span", "crm:P82_at_some_time_within",
							utils.createEntity("crm:E52_Time-Span", utils.crateInstanceGenerator("UUID", null), null, null)),		
				utils.createRange("cmd:MdCreationDate", utils.createEntity("http://www.w3.org/2000/01/rdf-schema#Literal", templGen.simpleLabelIG(),null))
			)
		);
		
		if(creator.equals(CMDICreatorType.SOFTWARE)){
			//cmdi was created by software
			profile.getLink().add(
				utils.createLink(
					utils.createPath("cmd:MdCreator", "crmdig:L23_used_software_or_firmware", null),
					utils.createRange(
						"cmd:MdCreator", 
						utils.createEntity("crmdig:D14_Software", templGen.localTermURI("software"), templGen.simpleLabelLG())
					)
				)
			);		
		}else{// -||- by actor
		profile.getLink().add(
				utils.createLink(
					utils.createPath("cmd:MdCreator", "crm:P11_had_participant", null),
					utils.createRange(
						"cmd:MdCreator", 
						utils.createEntity("crm:E39_Actor", templGen.localTermURI("actor"), templGen.simpleLabelLG())
					)
				)
			);
		}				
		
		//profile (Software)
		profile.getLink().add(
			utils.createLink(
				utils.createPath("cmd:MdProfile", "crmdig:L23_used_software_or_firmware", null),
				utils.createRange(
					"cmd:MdProfile", 
					utils.createEntity("crmdig:D14_Software", templGen.localTermURI("software"), templGen.simpleLabelLG(), 
							templGen.hasTypeTemplate("cmdi-profile"))
				)
			)
		);		
		
		return profile;
	}	
}
