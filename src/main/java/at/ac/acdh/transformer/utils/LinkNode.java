package at.ac.acdh.transformer.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gr.forth.x3ml.Entity;
import gr.forth.x3ml.RangeTargetNodeType;
import gr.forth.x3ml.SourceRelationType;
import gr.forth.x3ml.TargetRelationType;
import gr.forth.x3ml.X3ML.Mappings.Mapping.Link;
import gr.forth.x3ml.X3ML.Mappings.Mapping.Link.Path;
import gr.forth.x3ml.X3ML.Mappings.Mapping.Link.Range;

public class LinkNode {
	
	private String subject;
	List<ObjectNode> objects = new ArrayList<>();
	
	public LinkNode(String subject, ObjectNode object) {
		this.subject = subject;
		objects.add(object);
	}
	
	public LinkNode(String subject, ObjectNode... objects) {
		this.subject = subject;
		this.objects.addAll(Arrays.asList(objects));
	}
	
	public Link toLink(){
		if(objects == null || objects.size() == 0)
			return null;
		
		SourceRelationType srcRelation = new SourceRelationType();
		srcRelation.getRelation().add(subject);
		TargetRelationType tgtRelation = new TargetRelationType();
		RangeTargetNodeType rtnt = new RangeTargetNodeType();		
		
		for(int i = 0; i < objects.size(); i++){
			ObjectNode target = objects.get(i);
			tgtRelation.getEntityAndRelationship().add(target.relation);
			
			if(i + 1 < objects.size()){ //add it to the path/target_relation
				if(target.object != null)
					tgtRelation.getEntityAndRelationship().add(target.object);
			}else{
				rtnt.setEntity(target.object);
				break;
			}			
		}

		Path path = new Path();
		path.setSourceRelation(srcRelation);
		path.setTargetRelation(tgtRelation);
		
		Range range = new Range();
		range.setSourceNode(subject);
		range.setTargetNode(rtnt);
		
		Link link = new Link();
		link.setPath(path);
		link.setRange(range);
		return link;		
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public List<ObjectNode> getObjects() {
		return objects;
	}

	public void addObjects(ObjectNode object) {
		this.objects.add(object);
	}
	
	public static class ObjectNode{
		private String relation;
		private Entity object;
		
		public ObjectNode(String relation, Entity object) {
			this.relation = relation;
			this.object = object;
		}
		
		public String getRelation() {
			return relation;
		}
		
		public void setRelation(String relation) {
			this.relation = relation;
		}
		
		public Entity getObject() {
			return object;
		}
		
		public void setObject(Entity object) {
			this.object = object;
		}		
	}
}
