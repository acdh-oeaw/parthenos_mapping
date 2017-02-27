package at.ac.acdh.transformer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ximpleware.VTDException;

import at.ac.acdh.concept_mapping.ConceptMappingFactory;
import at.ac.acdh.concept_mapping.Profile2CIDOCMap;
import at.ac.acdh.transformer.utils.CMDICreatorType;
import at.ac.acdh.transformer.utils.TemplateGenerator;
import at.ac.acdh.transformer.utils.X3MLAdapter;
import gr.forth.x3ml.X3ML;
import gr.forth.x3ml.X3ML.Mappings;
import gr.forth.x3ml.X3ML.Namespaces;
import gr.forth.x3ml.X3ML.Namespaces.Namespace;

public class ProfileTransformer {
	
	static Pattern PROFILE_ID = Pattern.compile(".*(clarin.eu:cr1:p_\\d{13}).*");
	
	public X3ML transform(String profileUrl, CMDICreatorType creatorType, String resourceType) throws VTDException{
		Profile2CIDOCMap conceptMappings = ConceptMappingFactory.getMapping(profileUrl);
		
		X3ML x3ml = new X3ML();	
		x3ml.setSourceType("xpath");
		x3ml.setVersion("1.0");
		x3ml.setNamespaces(createNamespaces(profileUrl));
		
		
		Mappings mappings = new Mappings();
		
		X3MLAdapter adapter = new X3MLAdapter();
		TemplateGenerator templGen = new TemplateGenerator();
		
		MappingGenerator headerMapper = new CMDHeaderMapper(conceptMappings, resourceType, adapter, templGen, creatorType);
		MappingGenerator resourceProxyMapper = new CMDResProxyMapper(conceptMappings, resourceType, adapter, templGen);
		MappingGenerator componentsMapper = new CMDComponentsMapper(conceptMappings, resourceType, adapter, templGen);
		
		//add mappings for header
		mappings.getMapping().addAll(headerMapper.transformToMappings());		
		//add mappings for resource part
		mappings.getMapping().addAll(headerMapper.transformToMappings());		
		//add mappings for components part
		mappings.getMapping().addAll(headerMapper.transformToMappings());
		
		
		x3ml.setMappings(mappings);
		return x3ml;
	}	
	
	private Namespaces createNamespaces(String profileUrl){
		Namespaces namespaces = new Namespaces();
		
		namespaces.getNamespace().add(new Namespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#"));
		namespaces.getNamespace().add(new Namespace("xsd", "http://www.w3.org/2001/XMLSchema#"));
		namespaces.getNamespace().add(new Namespace("crm", "http://www.cidoc-crm.org/cidoc-crm/"));
		namespaces.getNamespace().add(new Namespace("crmdig", "http://www.ics.forth.gr/isl/CRMext/CRMdig.rdfs/"));
		namespaces.getNamespace().add(new Namespace("crmpe", "http://www.ics.forth.gr/isl/CRMext/CRMpe.rdfs/"));
		namespaces.getNamespace().add(new Namespace("parthenos", "http://parthenos-project.eu/"));
		namespaces.getNamespace().add(new Namespace("cmd", "http://www.clarin.eu/cmd/1"));
		namespaces.getNamespace().add(new Namespace("cmdp", getProfileURI(profileUrl)));//derive from xsd url
		
		return namespaces;
	}
	
	private static String getProfileURI(String url){
		String uri = "http://www.clarin.eu/cmd/1/profiles/";
		Matcher matcher = PROFILE_ID.matcher(url);
		if(matcher.matches())
			uri += matcher.group(1);
		
		return uri;
	}
}
