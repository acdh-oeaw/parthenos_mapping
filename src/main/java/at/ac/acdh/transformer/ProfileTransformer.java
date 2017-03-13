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

public class ProfileTransformer {
	
	static Pattern PROFILE_ID = Pattern.compile(".*(clarin.eu:cr1:p_\\d{13}).*");	
	
	public X3ML transform(String profileUrl, List<String> conditions) throws VTDException{
		X3ML x3ml = new X3ML();	
		x3ml.setSourceType("xpath");
		x3ml.setVersion("1.0");
		x3ml.setNamespaces(createNamespaces(profileUrl));
		
		ParsedProfile parsedProfile = ParsedProfileFactory.parse(profileUrl, true);
		
		//System.out.println(parsedProfile); System.exit(0);
		
		CMDI2CIDOCMap xmlMappings = Concepts2CIDOCFactory.unmarshall();
		new Normalizer().normalise(xmlMappings, parsedProfile, conditions);
		
		x3ml.setMappings(new CmdiToX3mlTransformer().transform(xmlMappings));
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
	
	private String getProfileURI(String url){
		String uri = "http://www.clarin.eu/cmd/1/profiles/";
		uri += extractProfileId(url);		
		return uri;
	}
	
	private String extractProfileId(String url){
		Matcher matcher = PROFILE_ID.matcher(url);
		String id = "";
		if(matcher.matches())
			id += matcher.group(1);		
		return id;
	}
}
