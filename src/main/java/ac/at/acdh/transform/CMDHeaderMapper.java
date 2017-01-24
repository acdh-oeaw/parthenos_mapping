package ac.at.acdh.transform;


import java.util.Arrays;

import ac.at.acdh.x3ml.DomainTargetNodeType;
import ac.at.acdh.x3ml.Entity;
import ac.at.acdh.x3ml.X3ML;
import ac.at.acdh.x3ml.X3ML.Mappings;
import ac.at.acdh.x3ml.X3ML.Mappings.Mapping;
import ac.at.acdh.x3ml.X3ML.Mappings.Mapping.Domain;
import ac.at.acdh.x3ml.X3ML.Mappings.Mapping.Link.Path;

public class CMDHeaderMapper {
	
	private Mappings mappings;	
	private X3MLUtils utils = new X3MLUtils();
	
	public X3ML mapToX3ML(){
		mappings = new Mappings();
		mappings.getMapping().add(mapInstanceToPersistantDS());
		mappings.getMapping().add(mapProfileToSoftware());	
		
		X3ML x3ml = new X3ML();
		x3ml.setMappings(mappings);
		return x3ml;
	}
	
	
	private Mapping mapInstanceToPersistantDS(){
		
		Mapping cmd = new Mapping();
		
		//create crmpe:PE22_Persistent_Dataset from CMD record
		//with hasType metadata
		Entity metadataEntity = utils.createEntity(
			"crmpe:PE22_Persistent_Dataset",
			utils.crateInstanceGenerator("LocalTermURI", Arrays.asList(
					utils.crateArg("hierarchy", "constant", "cmdi-record"),
					utils.crateArg("term", "xpath", "cmd:Header/cmd:MdSelfLink/text()")
			)),
			utils.crateLabelGenerator("Constant", Arrays.asList(
					utils.crateArg("text", "constant", "metadata")
			)),
			Templates.hasTypeTemplate("metadata")
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
					utils.createEntity(
						"crm:E42_Identifier",
						utils.crateInstanceGenerator("LocalTermURI", Arrays.asList(
								utils.crateArg("hierarchy", "constant", "id"),
								utils.crateArg("term", "xpath", "text()")
						)),
						utils.crateLabelGenerator("SimpleLabel", Arrays.asList(
								utils.crateArg("label", "xpath", "text()")
						)),
						null
					)
				)
			)
		);
		
		
		//CMD partof COllection
		cmd.getLink().add(
			utils.createLink(
				utils.createPath("cmd:Header/cmd:MdCollectionDisplayName", "crmpe:PP23i_is_dataset_part_of", null),
				utils.createRange("cmd:Header/cmd:MdCollectionDisplayName", 
					utils.createEntity(
						"crmpe:PE24_Volatile_Dataset",
						utils.crateInstanceGenerator("LocalTermURI", Arrays.asList(
								utils.crateArg("hierarchy", "constant", "cmdi-collection"),
								utils.crateArg("term", "xpath", "text()")
						)),
						utils.crateLabelGenerator("SimpleLabel", Arrays.asList(
								utils.crateArg("label", "xpath", "text()")
						)),
						Templates.hasTypeTemplate("collection")
					)
				)
			)
		);
		
		//crmdig:D7_Digital_Machine_Event for modeling 
		cmd.getLink().add(
			utils.createLink(
				utils.createPath("cmd:Header", "crmdig:L11i_was_output_of", null),
				utils.createRange("cmd:Header", 
					utils.createEntity(
						"crmdig:D7_Digital_Machine_Event",
						utils.crateInstanceGenerator("UUID", null),
						utils.crateLabelGenerator("SimpleLabel", Arrays.asList(
								utils.crateArg("label", "constant", "Creation of CDMI record"),
								utils.crateArg("text", "xpath", "cmd:MdSelfLink/text()")
						)),
						null
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
						utils.crateInstanceGenerator("LocalTermURI", Arrays.asList(
								utils.crateArg("hierarchy", "constant", "service"),
								utils.crateArg("term", "xpath", "/cmd:CMD/cmd:Resources/cmd:ResourceProxyList/cmd:ResourceProxy/cmd:ResourceRef/text()")
						)),
						null,
						null
					)
				)
			)
		);	
		
		*/
		
		return cmd;
				
	}
	
	private Mapping mapProfileToSoftware(){
		
		Mapping profile = new Mapping();		
		
				
		//creation of record on date using schema 
			
		DomainTargetNodeType metadataDomain = new DomainTargetNodeType();
		metadataDomain.setEntity(
				utils.createEntity("crmdig:D7_Digital_Machine_Event", utils.crateInstanceGenerator("UUID", null),	null, null)
		);		
		Domain cmdDomain = new Domain("/cmd:CMD/cmd:Header", metadataDomain);
		profile.setDomain(cmdDomain);
		
		//creation data		
		Path path = utils.createPath("cmd:MdCreationDate", "crm:P4_has_time-span", 
				utils.createEntity("crm:E52_Time-Span", utils.crateInstanceGenerator("UUID", null), null, null)
		);
		path.getTargetRelation().getEntityAndRelationship().add("crm:P82_at_some_time_within");		
		
		profile.getLink().add(
			utils.createLink(
				path,		
				utils.createRange("cmd:MdCreationDate", 
					utils.createEntity(
						"http://www.w3.org/2000/01/rdf-schema#Literal",
						utils.crateInstanceGenerator("SimpleLabel", Arrays.asList(
							utils.crateArg("label", "xpath", "text()")
						)),
						null,
						null
					)
				)
			)
		);
		
		//creator example for software 
		/*
		profile.getLink().add(
			utils.createLink(
				utils.createPath("cmd:MdCreator", "crmdig:L23_used_software_or_firmware", null),
				utils.createRange("cmd:MdCreator", 
					utils.createEntity(
						"crmdig:D14_Software",
						utils.crateInstanceGenerator("LocalTermURI", Arrays.asList(
							utils.crateArg("hierarchy", "constant", "software"),
							utils.crateArg("term", "xpath", "text()")
						)),
						utils.crateLabelGenerator("SimpleLabel", Arrays.asList(
							utils.crateArg("label", "xpath", "text()")
						)),
						null
					)
				)
			)
		);
		*/
		
		//profile (Software)
		profile.getLink().add(
			utils.createLink(
				utils.createPath("cmd:MdProfile", "crmdig:L23_used_software_or_firmware", null),
				utils.createRange("cmd:MdProfile", 
					utils.createEntity(
						"crmdig:D14_Software",
						utils.crateInstanceGenerator("LocalTermURI", Arrays.asList(
							utils.crateArg("hierarchy", "constant", "software"),
							utils.crateArg("term", "xpath", "text()")
						)),
						utils.crateLabelGenerator("SimpleLabel", Arrays.asList(
							utils.crateArg("label", "xpath", "text()")
						)),						
						Templates.hasTypeTemplate("cmdi-profile")
					)
				)
			)
		);
		
		
		return profile;
	}	

	
	
}
