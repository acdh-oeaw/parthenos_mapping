package at.ac.acdh.transformer.utils;

import java.util.Arrays;

import gr.forth.x3ml.Additional;
import gr.forth.x3ml.InstanceGenerator;
import gr.forth.x3ml.LabelGenerator;

public class TemplateGenerator {
	
	private X3MLAdapter utils = new X3MLAdapter();
	
	public Additional hasTypeTemplate(String type){
		Additional add = utils.createAditional( //hasType = metadata
				"crm:P2_has_type",
				utils.createEntity(
					"crm:E55_Type",					
					utils.crateInstanceGenerator("LocalTermURI", Arrays.asList(
							utils.crateArg("hierarchy", "constant", "type"),
							utils.crateArg("term", "constant", type)
					)),
					utils.crateLabelGenerator("Constant", Arrays.asList(
							utils.crateArg("text", "constant", type)					
					)),
					null,
					utils.createInstanceInfo(type, null, null)
				));
		
		return add;
	}
	
	public InstanceGenerator localTermURI(String name, String xpath){
		return utils.crateInstanceGenerator("LocalTermURI", Arrays.asList(
				utils.crateArg("hierarchy", "constant", name),
				utils.crateArg("term", "xpath", xpath)
		));		
	}
	
	public InstanceGenerator localTermURI(String name){
		return localTermURI(name, "text()");
	}
	
	public InstanceGenerator simpleLabelIG(String xpath){
		return utils.crateInstanceGenerator("SimpleLabel", Arrays.asList(
				utils.crateArg("label", "xpath", xpath)
			));
	}
	
	public InstanceGenerator simpleLabelIG(){
		return 	simpleLabelIG("text()");
	}
	
	public LabelGenerator simpleLabelLG(String xpath){
		return utils.crateLabelGenerator("SimpleLabel", Arrays.asList(
				utils.crateArg("label", "xpath", xpath)
			));		
	}
	
	public LabelGenerator simpleLabelLG(){
		return 	simpleLabelLG("text()");
	}

}
