package ac.at.acdh.transform;

import java.util.Arrays;

import ac.at.acdh.x3ml.Additional;

public class Templates {
	
	private static X3MLUtils utils = new X3MLUtils();
	
	public static Additional hasTypeTemplate(String type){
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

}
