package at.ac.acdh.transformer.utils;

import gr.forth.x3ml.Additional;
import gr.forth.x3ml.Arg;
import gr.forth.x3ml.DomainTargetNodeType;
import gr.forth.x3ml.Entity;
import gr.forth.x3ml.InstanceGenerator;
import gr.forth.x3ml.InstanceInfo;
import gr.forth.x3ml.X3ML.Mappings.Mapping;
import gr.forth.x3ml.X3ML.Mappings.Mapping.Domain;

/*
 * @Adapter class is facade for X3ML model API
 * It provides API for creating X3ML objects and building x3ml mapping 
 * 
 */
public class X3mlFacade {
	
	
	public Mapping createMapping(String srcNode, String type){
		return createMapping(srcNode, type, null, null, null);
	}	
	
	public Mapping createMapping(String srcNode, String type, String hasType, String var, String globVar){		
		DomainTargetNodeType target = new DomainTargetNodeType();
		target.setEntity(createEntity(type, hasType, var, globVar));
		Domain domain = new Domain(srcNode, target);
		
		Mapping mapping = new Mapping();
		mapping.setDomain(domain);
		
		return mapping;
		
	}	
	
	public Entity createEntity(String type){
		return createEntity(type, null, null, null);
	}
	
	/*
	 * Entity can have a list of Additional nodes but in our case 
	 * there is only situation when additional represents hasType E55_Type relationship
	 * 
	 * @see Additional
	 * 
	 * In other cases create Entity with createEntity(type) and Additional with create Additional 
	 * and call entity.getAdditional().add(additional) to add it
	 * 
	 * @see gr.forth.x3ml package for more info
	 */
	public Entity createEntity(String type, String hasType, String var, String globVar){
		Entity entity = new Entity();
		entity.getType().add(type);
		
		//set variables 
		entity.setVariable(var);
		entity.setGlobalVariable(globVar);
		
		if(hasType != null){
			entity.getAdditional().add(createAditionalHasType(hasType));
		}
		
		entity.setInstanceGenerator(createUUIDIG());
		
		return entity;
	}
	
	public Additional createAditionalHasType(String type){		
		Entity e55 = createEntity("crm:E55_Type");
		e55.setInstanceInfo(createInstanceInfo(type, null, null));
		
		e55.setInstanceGenerator(createClarinTypeIG(type));
		
		return createAditional("crm:P2_has_type", e55);
	}
	
	public Additional createAditional(String relationship, Entity entity){
		Additional additional = new Additional();
		additional.setRelationship(relationship);
		additional.setEntity(entity);
		return additional;
	}
	
	public InstanceInfo createInstanceInfo(String constant, String language, String description){
		InstanceInfo instanceInfo = new InstanceInfo();
		
		instanceInfo.setConstant(constant);
		instanceInfo.setLanguage(language);
		instanceInfo.setDescription(description);
		
		return instanceInfo;
	}
	
	public InstanceGenerator createClarinTypeIG(String type){
		InstanceGenerator ig = new InstanceGenerator();
		ig.setName("LocalTermURI_CLARIN");
		ig.getArg().add(crateArg("hierarchy", "constant", "type"));
		ig.getArg().add(crateArg("hierarchy", "constant", type));
		
		return ig;
	}
	
	public InstanceGenerator createUUIDIG(){
		InstanceGenerator ig = new InstanceGenerator();
		ig.setName("UUID");
		return ig;		
	}
	
	/*
	 * there are 2 Arg classes, one in InstanceGen and the other in LabelGen but they are identical 
	 */
	public Arg crateArg(String name, String type, String value){
		Arg arg = new Arg();
		arg.setName(name);
		arg.setType(type);
		arg.setValue(value);
		return arg;
	}
	
}
