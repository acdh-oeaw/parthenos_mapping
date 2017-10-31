package at.ac.acdh.concept_mapping;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;




/**
 * 
 * <p>CMDI2CIDOCMap class is java representative for xml file CMDI2CIDOC.xml.</p>
 * <p>The xml file itself defines mappings between CMDI concepts/xpaths and CIDOC-CRM entities</p>
 * 
 * 
 * @author dostojic
 * 
 */
@XmlRootElement(name = "mappings")
@XmlAccessorType(XmlAccessType.FIELD)
public class CMDI2CIDOCMap extends Node{

	@XmlElement(name = "entity")
	Collection<ParthenosEntity> entities = new ArrayList<>();
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(ParthenosEntity pe: entities)
			sb.append(pe.toString() + "\n");
		
		return sb.toString();
	}
	

	/**
	 * @return list of specified {@link ParthenosEntity}
	 * 
	 * <p>Each {@link ParthenosEntity entity }will be transformed to one {@link gr.forth.x3ml.X3ML.Mappings.Mapping}</p>
	 */
	public Collection<ParthenosEntity> getEntities() {
		return entities;
	}

	public void setEntities(Collection<ParthenosEntity> entities) {
		this.entities = entities;
	}



/*	
	
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)	
	public static class InstanceGenerator{
		@XmlAttribute 
		String name;
		
		@XmlElement(name = "argument")
		Collection<Argument> arguments;
		
		public String getName(){
			return this.name;
		}
		
		public void setName(String name){
			this.name = name;
		}
		
		public Collection<Argument> getArguments(){
			return this.arguments;
		}
		
		public void setArguments(Collection<Argument> arguments){
			this.arguments = arguments;
		}
	
		
		
	}
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Argument{
		@XmlAttribute
		String name; 
		
		@XmlAttribute
		String type;
		
		@XmlValue
		String value;
		
		public String getName(){
			return this.name;
		}
		
		public void setName(String name){
			this.name = name;
		}
		
		public String getType(){
			return this.type;
		}
		
		public void setType(String type){
			this.type = type;
		}
		
		public String getValue(){
			return this.value;
		}
		
		public void setValue(String value){
			this.value = value;
		}
	}
*/
}
