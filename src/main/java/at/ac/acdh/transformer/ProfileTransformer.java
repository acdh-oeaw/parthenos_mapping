package at.ac.acdh.transformer;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ximpleware.VTDException;

import at.ac.acdh.concept_mapping.CMDI2CIDOCMap;
import at.ac.acdh.concept_mapping.Concepts2CIDOCFactory;
import at.ac.acdh.profile_parser.ParsedProfile;
import at.ac.acdh.profile_parser.ParsedProfileFactory;
import gr.forth.x3ml.X3ML;
import gr.forth.x3ml.X3ML.Namespaces;
import gr.forth.x3ml.X3ML.Namespaces.Namespace;

/**
 * 
 * This class is responsible for converting CLARIN profiles into X3ML files. 
 * It returns {@link X3ML} object for the given profile to the caller. 
 * 
 * @author dostojic
 *
 */
public class ProfileTransformer {
	
	static Pattern PROFILE_ID = Pattern.compile(".*(clarin.eu:cr1:p_\\d{13}).*");	
	
	/**
	 * 
	 * @param mappingXml - path to the xml file containing mappings
	 * @param profileUrl - profile's XSD
	 * @param conditions - list of strings used for filtering conditional entities in xml mapping file
	 * @return {@link X3ML} object that can be serialized to X3ML xml file
	 * @throws VTDException
	 */
	public X3ML transform(String mappingXml, String profileUrl, List<String> conditions) throws VTDException{
		return transform(Concepts2CIDOCFactory.unmarshall(mappingXml), profileUrl, conditions);
	}
	
	/**
	 * 
	 * This method reads packed xml mapping file
	 * 
	 * @param profileUrl - profile's XSD
	 * @param conditions - list of strings used for filtering conditional entities in xml mapping file
	 * @return {@link X3ML} object that can be serialized to X3ML xml file
	 * @throws VTDException
	 */
	public X3ML transform(String profileUrl, List<String> conditions) throws VTDException{
		return transform(Concepts2CIDOCFactory.unmarshall(), profileUrl, conditions);		
	}
	
	/**
	 * This method parses profile, and based on mappings creates X3ML object.
	 * 
	 * 
	 * @param xmlMappings - {@link CMDI2CIDOCMap} object 
	 * @param profileUrl - url of the profile's XSD
	 * @param conditions - list of strings used for filtering conditional entities in xml mapping file
	 * @return {@link X3ML} object that can be serialized to X3ML xml file
	 * @throws VTDException
	 * 
	 * @see ParsedProfileFactory ParsedProfile X3ML
	 */
	private X3ML transform(CMDI2CIDOCMap xmlMappings, String profileUrl, List<String> conditions)throws VTDException{
		X3ML x3ml = new X3ML();	
		x3ml.setSourceType("xpath");
		x3ml.setVersion("1.0");
		x3ml.setNamespaces(createNamespaces(profileUrl));
		
		ParsedProfile parsedProfile = ParsedProfileFactory.parse(profileUrl, true);
		
		new Normalizer().normalise(xmlMappings, parsedProfile, conditions);
		
		x3ml.setMappings(new CmdiToX3mlTransformer().transform(xmlMappings));
		return x3ml;
	}
	
	/**
	 * 
	 * @param profileUrl
	 * @return {@link gr.forth.x3ml.X3ML.Namespaces} with list of namespaces required for rdf generation
	 */
	private Namespaces createNamespaces(String profileUrl){
		Namespaces namespaces = new Namespaces();
		
		namespaces.getNamespace().add(new Namespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#"));
		namespaces.getNamespace().add(new Namespace("xsd", "http://www.w3.org/2001/XMLSchema#"));
		namespaces.getNamespace().add(new Namespace("crm", "http://www.cidoc-crm.org/cidoc-crm/"));
		namespaces.getNamespace().add(new Namespace("crmdig", "http://www.ics.forth.gr/isl/CRMext/CRMdig.rdfs/"));
		namespaces.getNamespace().add(new Namespace("crmpe", "http://parthenos.d4science.org/CRMext/CRMpe.rdfs/"));
		namespaces.getNamespace().add(new Namespace("parthenos", "http://parthenos.d4science.org/handle/Clarin/VLO/")); 
		namespaces.getNamespace().add(new Namespace("cmd", "http://www.clarin.eu/cmd/1"));
		namespaces.getNamespace().add(new Namespace("reg", "http://parthenos.d4science.org/handle/Parthenos/REG/"));
		namespaces.getNamespace().add(new Namespace("frbr", "http://iflastandards.info/ns/fr/frbr/frbroo/"));
		namespaces.getNamespace().add(new Namespace("cmdp", getProfileURI(profileUrl)));//derive from xsd url
		
		return namespaces;
	}
	
	/** 
	 * @param url
	 * @return URI for cmdp namespace
	 * 
	 * @see ProfileTransformer#createNamespaces(String)
	 */
	private String getProfileURI(String url){
		String uri = "http://www.clarin.eu/cmd/1/profiles/";
		uri += extractProfileId(url);		
		return uri;
	}
	
	/**
	 * 
	 * @param url
	 * @return extracted id from profiles URL 
	 */
	private String extractProfileId(String url){
		Matcher matcher = PROFILE_ID.matcher(url);
		String id = "";
		if(matcher.matches())
			id += matcher.group(1);		
		return id;
	}
}
