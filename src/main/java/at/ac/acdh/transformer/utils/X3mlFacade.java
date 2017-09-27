package at.ac.acdh.transformer.utils;


import java.util.ArrayList;
import java.util.Collection;

import at.ac.acdh.concept_mapping.ParthenosEntity;
import gr.forth.x3ml.Additional;
import gr.forth.x3ml.Arg;
import gr.forth.x3ml.DomainTargetNodeType;
import gr.forth.x3ml.Entity;
import gr.forth.x3ml.InstanceGenerator;
import gr.forth.x3ml.InstanceInfo;
import gr.forth.x3ml.X3ML.Mappings.Mapping;
import gr.forth.x3ml.X3ML.Mappings.Mapping.Domain;
import gr.forth.x3ml.LabelGenerator;
/**
 * 
 * @author dostojic
 * 
 * X3mlFacade class is facade for X3ML model API
 * It provides API for creating X3ML objects and building x3ml mapping from values configured in mapping xml
 * 
 */
public class X3mlFacade {
	
	/**
	 * 
	 * @param srcNode
	 * @param type
	 * @return X3ML {@link Mapping}
	 * 
	 * @see  Mapping
	 */	
//	public Mapping createMapping(String srcNode, String type){
//		return createMapping(srcNode, type, null, null, null);
//	}	
	
	/**
	 * 
	 * @param srcNode
	 * @param type
	 * @param hasType
	 * @param var
	 * @param globVar
	 * @return X3ML {@link Mapping}
	 * 
	 * @see  Mapping
	 */
/*	public Mapping createMapping(String srcNode, String type, String hasType, String var, String globVar){		
		DomainTargetNodeType target = new DomainTargetNodeType();
		target.setEntity(createEntity(type, hasType, var, globVar));
		Domain domain = new Domain(srcNode, target);
		
		Mapping mapping = new Mapping();
		mapping.setDomain(domain);
		
		return mapping;
		
	}	*/
	
	public Mapping createMapping(ParthenosEntity pe){		
		DomainTargetNodeType target = new DomainTargetNodeType();
		//target.setEntity(createEntity(type, hasType,  var, globVar));
		target.setEntity(createEntity(pe));
		Domain domain = new Domain(pe.getXpath(), target);
		
		Mapping mapping = new Mapping();
		mapping.setDomain(domain);
		
		return mapping;
		
	}		
	
	/**
	 * 
	 * @param type
	 * @return X3ML {@link Entity}
	 * 
	 * @see Entity
	 */
/*	public Entity createEntity(String type){
		return createEntity(type, null, null, null);
	}*/
	
	/** 
	 * 
	 * @param type
	 * @param hasType
	 * @param var
	 * @param globVar
	 * @return X3ML {@link Entity}
	 * 
	 * Entity can have a list of Additional nodes but in our case 
	 * there is only situation when additional represents hasType E55_Type relationship
	 * 
	 * @see Additional
	 * 
	 * In other cases create Entity with createEntity(type) and Additional with create Additional 
	 * and call entity.getAdditional().add(additional) to add it
	 * 
	 *  @see Entity
	 */
/*	public Entity createEntity(String type, String hasType, String var, String globVar){
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
	}*/
	
	public Entity createEntity(ParthenosEntity pe){
		Entity entity = new Entity();
		entity.getType().add(pe.getType());
		
		//set variables 
		entity.setVariable(pe.getVar());
		entity.setGlobalVariable(pe.getGlobVar());
		
		if(pe.getHasType() != null){
			entity.getAdditional().add(createAditionalHasType(pe));
		}
		
		entity.setInstanceGenerator(createInstanceGenerator(pe));
		
		entity.getLabelGenerator().addAll(pe.getLabelGenerator());
		
		
		return entity;
	}	
	


	/**
	 * 
	 * @param type
	 * @return X3ML {@link Additional}
	 * 
	 * @see Additional
	 */
	public Additional createAditionalHasType(ParthenosEntity pe){		
		Entity e55 = new Entity();
		e55.getType().add("crm:E55_Type");
		e55.setInstanceInfo(createInstanceInfo(pe.getHasType(), null, null));
		
		e55.setInstanceGenerator(createClarinTypeIG(pe.getHasType()));
		//e55.getLabelGenerator().
		if(pe.getHasLabel() != null)
			e55.getLabelGenerator().addAll(createLabelGenerator(pe));
		
		return createAditional(pe.getSubrelation() == null?"crm:P2_has_type":pe.getSubrelation(), e55);
	}	
	

	/**
	 * 
	 * @param relationship
	 * @param entity
	 * @return X3ML {@link Additional}
	 * 
	 * @see Additional
	 */
	public Additional createAditional(String relationship, Entity entity){
		Additional additional = new Additional();
		additional.setRelationship(relationship);
		additional.setEntity(entity);
		return additional;
	}
	
	/**
	 * 
	 * @param constant
	 * @param language
	 * @param description
	 * @return X3ML {@link InstanceInfo}
	 * 
	 * @see InstanceInfo
	 */
	public InstanceInfo createInstanceInfo(String constant, String language, String description){
		InstanceInfo instanceInfo = new InstanceInfo();
		
		instanceInfo.setConstant(constant.split("\\|")[0]);
		instanceInfo.setLanguage(language);
		instanceInfo.setDescription(description);
		
		return instanceInfo;
	}
	
	/**
	 * LocalTermURI_CLARIN InstanceGenerator is based on LocalTermURI and it generates entity urls in form 
	 *  http://www.clarin.eu/{hierarchy}/{term}
	 * 
	 * @param hierarchy - constant 
	 * @param xpath - xpath from which value will be obtained
	 * @return LocalTermURI_CLARIN type of X3ML {@link InstanceGenerator} 
	 * 
	 * @see InstanceGenerator
	 */
	public InstanceGenerator createLocalTermURI_CLARIN(String hierarchy, String xpath){
		InstanceGenerator ig = new InstanceGenerator();
		ig.setName("LocalTermURI_CLARIN");
		ig.getArg().add(crateArg("hierarchy", "constant", hierarchy));
		ig.getArg().add(crateArg("term", "xpath", xpath));
		
		return ig;
	}
	
	/**
	 * Template for hasType type entities:  http://www.clarin.eu/type/{param}
	 * 
	 * @param type
	 * @return LocalTermURI_CLARIN type of X3ML {@link InstanceGenerator} 
	 * 
	 * @see InstanceGenerator
	 */
	public InstanceGenerator createClarinTypeIG(String types){
		InstanceGenerator ig = new InstanceGenerator();
		ig.setName("ConceptURI_2step");

		if(types != null) {
			String[] typeArr = types.split("\\|");
			for(int i=1; i<typeArr.length; i++){
				ig.getArg().add(crateArg("term" + i, "constant", typeArr[i]));
			}
		}
		
		
		return ig;
	}
	
	/**
	 * LocalTermURI InstanceGenerator generates entity urls in form 
	 *  {hierarchy}/{term}
	 * 
	 * @param hierarchy - constant 
	 * @param xpath - xpath from which value will be obtained
	 * @return LocalTermURI type of X3ML {@link InstanceGenerator} 
	 * 
	 * @see InstanceGenerator
	 */
	public InstanceGenerator createLocalTermURI(String hierarchy, String xpath){
		InstanceGenerator ig = new InstanceGenerator();
		ig.setName("LocalTermURI");
		ig.getArg().add(crateArg("hierarchy", "constant", hierarchy));
		ig.getArg().add(crateArg("term", "xpath", xpath));
		
		return ig;
	}	
	
	/**
	 * SimpleLabel InstanceGenerator generates entity urls in form {label}
	 * 
	 * @param xpath - xpath from which value will be obtained
	 * @return SimpleLabel type of X3ML {@link InstanceGenerator} 
	 * 
	 * @see InstanceGenerator
	 */
	public InstanceGenerator createSimpleLabel(String xpath){
		InstanceGenerator ig = new InstanceGenerator();
		ig.setName("SimpleLabel");
		ig.getArg().add(crateArg("label", "xpath", xpath));
		
		return ig;
	}
	
	/**
	 * 
	 * @return UUID type of X3ML {@link InstanceGenerator} 
	 * 
	 * @see InstanceGenerator
	 */
/*	public InstanceGenerator createUUIDIG(){
		InstanceGenerator ig = new InstanceGenerator();
		ig.setName("UUID");
		return ig;		
	}*/
	
	public InstanceGenerator createInstanceGenerator(ParthenosEntity pe){
		
		if(pe.getInstanceGenerator() != null)
			return pe.getInstanceGenerator();
			
		InstanceGenerator ig = new InstanceGenerator();
		ig.setName("UUID");

		return ig;
	}
	
	public Collection<LabelGenerator> createLabelGenerator(ParthenosEntity pe){
		ArrayList<LabelGenerator> lgList = new ArrayList<LabelGenerator>();
		if(pe.getHasLabel() != null) {
			
			for(String lgDefintion : pe.getHasLabel().split("\\|\\|")) {
				LabelGenerator lg = new LabelGenerator();
				String[] tokens = lgDefintion.split("\\|");
				lg.setName(tokens[0]);
				
				for(int i=1; i<tokens.length; i++) {
					lg.getArg().add(crateArg("label" + i, "constant", tokens[i]));
				}
				lgList.add(lg);
			};
		}
		
		return lgList;
	}
	
	//there are 2 Arg classes, one in InstanceGen and the other in LabelGen but they are identical 
	/**
	 * 
	 * @param name
	 * @param type
	 * @param value
	 * @return X3ML {@link Arg}
	 * 
	 * @see Arg
	 */
	public Arg crateArg(String name, String type, String value){
		Arg arg = new Arg();
		arg.setName(name);
		arg.setType(type);
		arg.setValue(value);
		return arg;
	}
	
}
