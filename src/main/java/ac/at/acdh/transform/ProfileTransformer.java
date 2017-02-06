package ac.at.acdh.transform;

import com.ximpleware.VTDException;

import ac.at.acdh.concept_mapping.ConceptMappingFactory;
import ac.at.acdh.concept_mapping.Profile2CIDOCMap;
import ac.at.acdh.transform.utils.CMDICreatorType;
import ac.at.acdh.transform.utils.CMDIResourceType;
import ac.at.acdh.x3ml.X3ML;
import ac.at.acdh.x3ml.X3ML.Mappings;

public class ProfileTransformer {
	
	public X3ML transform(String profileUrl, CMDICreatorType creatorType, CMDIResourceType resourceType) throws VTDException{
		Profile2CIDOCMap conceptMappings = ConceptMappingFactory.getMapping(profileUrl);
		
		X3ML x3ml = new X3ML();		
		Mappings mappings = new Mappings();
		mappings = new Mappings();
		
		//add mappings for header
		mappings.getMapping().addAll(new CMDHeaderMapper(conceptMappings).getMappings(creatorType, resourceType));
		
		//add mappings for resource part
		
		//add mappings for components part		
		mappings.getMapping().addAll(new CMDIComponentsMapper(conceptMappings).getMappings(resourceType));
		
		x3ml.setMappings(mappings);
		return x3ml;
		
		//XMLMarshaller<X3ML> xmlUtils = new XMLMarshaller<>(X3ML.class);
	}
	

}
