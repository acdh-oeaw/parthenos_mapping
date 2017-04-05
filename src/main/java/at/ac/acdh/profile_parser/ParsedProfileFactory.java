package at.ac.acdh.profile_parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ximpleware.VTDException;


/**
 * <p>This class is responsible for creating proper parser, based on CMDI version, and to return instance of {@link ParsedProfile}
 * to caller.</p>
 * 
 * @author dostojic
 *
 */
public class ParsedProfileFactory {
	
	private static Pattern CMDI_VER = Pattern.compile("registry/(\\d\\.[x0-9])/profiles/");
	

	/**
	 * This method creates instance of {@link ProfileParser} based on CMDI version in passed url 
	 * and calls its method {@link ProfileParser#parse(String, boolean)} to create parsed instance object.
	 * If parsing was successful an instance of {@link ParsedProfile} is returned to the user
	 * 
	 * @param url - URL of a profile's XSD 
	 * @param withNamespace - should construct xpaths with namespace
	 * @return an instance of {@link ParsedProfile}
	 * @throws VTDException if parsing of XSD fails
	 * @throws IllegalArgumentException if unable to determinate CMDI version from url
	 */
	public static ParsedProfile parse(String url, boolean withNamespace) throws VTDException{
		ProfileParser parser = createParser(url);
		
		return parser.parse(url, withNamespace);
	}
	
	private static ProfileParser createParser(String url){
		Matcher matcher = CMDI_VER.matcher(url);
		String cmdVersion = matcher.find()? matcher.group(1) : "unknown";		
		
		switch(cmdVersion){
			case "1.1": 
				return new CMDI1_1_ProfileParser();
			case "1.x":
			case "1.2":
				return new CMDI1_2_ProfileParser();				
			default:
				throw new IllegalArgumentException("cmdi version is missing in schema url. I don't how to process the profile :(.\n"
						+ "Well formated schema url looks like: https://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/CMDI_VERSION/profiles/your_profile_id/xsd\n"
						+ "where CMDI_VERSION is a version number like 1.2 or 1.x for the latest version.\n");
		}		
	}

}
