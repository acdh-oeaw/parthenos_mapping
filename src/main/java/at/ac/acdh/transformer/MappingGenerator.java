package at.ac.acdh.transformer;

import java.util.List;

import at.ac.acdh.concept_mapping.Profile2CIDOCMap;
import at.ac.acdh.transformer.utils.LinkNode;
import at.ac.acdh.transformer.utils.TemplateGenerator;
import at.ac.acdh.transformer.utils.X3MLAdapter;
import gr.forth.x3ml.Entity;
import gr.forth.x3ml.X3ML.Mappings.Mapping;

abstract class MappingGenerator {
	
	protected Profile2CIDOCMap profileMapping;
	protected String resourceType;
		
	protected X3MLAdapter adapter;
	protected TemplateGenerator templGen;
	
	protected Entity entity = null;
	protected LinkNode link = null;

	public MappingGenerator(Profile2CIDOCMap profileMapping, String resourceType, X3MLAdapter adapter, TemplateGenerator templGen) {
		this.profileMapping = profileMapping;
		this.resourceType = resourceType;
		this.adapter = adapter;
		this.templGen = templGen;
	}

	public abstract List<Mapping> transformToMappings();	

}
