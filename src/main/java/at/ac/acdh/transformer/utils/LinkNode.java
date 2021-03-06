package at.ac.acdh.transformer.utils;

import java.util.ArrayList;
import java.util.List;

import gr.forth.x3ml.Entity;
import gr.forth.x3ml.RangeTargetNodeType;
import gr.forth.x3ml.SourceRelationType;
import gr.forth.x3ml.TargetRelationType;
import gr.forth.x3ml.X3ML.Mappings.Mapping.Link;
import gr.forth.x3ml.X3ML.Mappings.Mapping.Link.Path;
import gr.forth.x3ml.X3ML.Mappings.Mapping.Link.Range;


/**
 * @author dostojic
 * 
 * This class represents an object of "visual" source to target node from X3ML web application.
 * Class has two attributes, subject or source - xpath from CMDI profile and 
 * list of objects or target where each object has relationship and CIDOC-CRM entity
 * 
 *
 */
public class LinkNode {
	
	private String subject;
	List<ObjectNode> objects = new ArrayList<>();
	
	/**
	 * 
	 * @param subject - xpath from CMDI profile/record
	 * @param objects - CIDOC-CRM entities
	 * 
	 * @see ObjectNode
	 */
	public LinkNode(String subject, List<ObjectNode> objects) {
		this.subject = subject;
		this.objects = objects;
	}
	
	/**
	 * 
	 * @return corresponding X3ML {@link Link} automatically taking care of intermediate nodes 
	 */
	public Link toLink(){
		if(objects == null || objects.size() == 0)
			return null;
		
		String xpath = normalizeXPath(subject);
		
		SourceRelationType srcRelation = new SourceRelationType();
		srcRelation.getRelation().add(xpath);
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
		range.setSourceNode(xpath);
		range.setTargetNode(rtnt);
		
		Link link = new Link();
		link.setPath(path);
		link.setRange(range);
		return link;		
	}
	
	private String normalizeXPath(final String xpath){
		String normalized = xpath;
		
		if(normalized.startsWith("/cmd:CMD/cmd:Components/"))
			normalized = normalized.replace("/cmd:CMD/cmd:Components/", "");
		
		if(normalized.endsWith("/text()"))
			normalized = normalized.replace("/text()", "");
		
		return normalized;
		
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
	
	/**
	 * This class represents "target" part from X3ML web application. 
	 * It has two attributes:
	 * <ul><li>relation - CIDOC-CRM relationship and </li><li>object - CIDOC-CRM class</li></ul>  
	 * 
	 */
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
