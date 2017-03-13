package at.ac.acdh.transformer.utils;

import java.util.List;

import gr.forth.x3ml.Additional;
import gr.forth.x3ml.Arg;
import gr.forth.x3ml.Entity;
import gr.forth.x3ml.InstanceGenerator;
import gr.forth.x3ml.InstanceInfo;
import gr.forth.x3ml.LabelGenerator;
import gr.forth.x3ml.RangeTargetNodeType;
import gr.forth.x3ml.SourceRelationType;
import gr.forth.x3ml.TargetRelationType;
import gr.forth.x3ml.X3ML.Mappings.Mapping.Link;
import gr.forth.x3ml.X3ML.Mappings.Mapping.Link.Path;
import gr.forth.x3ml.X3ML.Mappings.Mapping.Link.Range;

public class X3MLAdapter {
	
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
	
	
	public InstanceGenerator crateInstanceGenerator(String name, List<Arg> args){
		InstanceGenerator ig = new InstanceGenerator();
		ig.setName(name);
		if(args != null)
			ig.getArg().addAll(args);
		return ig;
	}
	
	public LabelGenerator crateLabelGenerator(String name, List<Arg> args){
		LabelGenerator lg = new LabelGenerator();
		lg.setName(name);
		if(args != null)
			lg.getArg().addAll(args);
		return lg;
	}
	
	public Entity createEntity(String type, InstanceGenerator ig, LabelGenerator lg){
		return createEntity(type, ig, lg, null, null);
	}
	
	public Entity createEntity(String type, InstanceGenerator ig, LabelGenerator lg, Additional additional){
		return createEntity(type, ig, lg, additional, null);
	}
	
	public Entity createEntity(String type, InstanceGenerator ig, LabelGenerator lg, Additional additional, InstanceInfo instanceInfo){
		Entity entity = new Entity();
		entity.getType().add(type);
		
		if(ig != null){
			entity.setInstanceGenerator(ig);
		}
		
		if(lg != null){
			entity.getLabelGenerator().add(lg);
		}		
		
		
		if(additional != null){
			entity.getAdditional().add(additional);
		}
		
		if(instanceInfo != null){
			entity.setInstanceInfo(instanceInfo);
		}
		
		return entity;
	}
	
	public InstanceInfo createInstanceInfo(String constant, String language, String description){
		InstanceInfo instanceInfo = new InstanceInfo();
		
		instanceInfo.setConstant(constant);
		instanceInfo.setLanguage(language);
		instanceInfo.setDescription(description);
		
		return instanceInfo;
	}
	
	public Additional createAditional(String relationship, Entity entity){
		Additional additional = new Additional();
		additional.setRelationship(relationship);
		additional.setEntity(entity);
		return additional;
	}
	
	
	public Path createPath(String sourceRelation, String targetRelation, Entity targetEntity){
		SourceRelationType srcRelation = new SourceRelationType();
		srcRelation.getRelation().add(sourceRelation);
		
		TargetRelationType tgtRelation = new TargetRelationType();
		tgtRelation.getEntityAndRelationship().add(targetRelation);
		
		if(targetEntity != null)
			tgtRelation.getEntityAndRelationship().add(targetEntity);
		
		Path path = new Path();
		path.setSourceRelation(srcRelation);
		path.setTargetRelation(tgtRelation);		
		
		return path;
	}
	
	public Path createPathWithIntermediate(String sourceXpath, String intermediateRelation, Entity intermediateEntity, String targetRelation){
		SourceRelationType srcRelation = new SourceRelationType();
		srcRelation.getRelation().add(sourceXpath);
		
		TargetRelationType tgtRelation = new TargetRelationType();
		tgtRelation.getEntityAndRelationship().add(intermediateRelation);
		
		if(intermediateEntity != null)
			tgtRelation.getEntityAndRelationship().add(intermediateEntity);
		
		Path path = new Path();
		path.setSourceRelation(srcRelation);	
		path.setTargetRelation(tgtRelation);
		path.getTargetRelation().getEntityAndRelationship().add(targetRelation);		
		
		return path;
	}
	
	public Range createRange(String sourceNode, Entity targetEntity){
		RangeTargetNodeType rtnt = new RangeTargetNodeType();
		rtnt.setEntity(targetEntity);
		
		Range range = new Range();
		range.setSourceNode(sourceNode);
		range.setTargetNode(rtnt);
		return range;
	}
	
	public Link createLink(Path path, Range range){
		Link link = new Link();
		link.setPath(path);
		link.setRange(range);
		return link;
	}
}
