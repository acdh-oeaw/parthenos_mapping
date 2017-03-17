package at.ac.acdh.concept_mapping;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Paths;

import javax.xml.bind.JAXB;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Concepts2CIDOCFactory transforms given xml mapping file into into CMDI2CIDOCMap object
 * 
 * @see CMDI2CIDOCMap
 * 
 * @author dostojic
 *
 */
public class Concepts2CIDOCFactory {

	private static String CONCEPT_MAP = "/mapping/CMDI2CIDOC.xml";

	/**
	 * reads xml mapping file from the given path and transforms it into
	 * CMDI2CIDOCMap java object
	 * 
	 * @param xmlFile - path to xml file with mappings
	 * 
	 * @return CMDI2CIDOCMap instance
	 */
	public static CMDI2CIDOCMap unmarshall(String xmlFile) {
		final SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setXIncludeAware(true);
		spf.setNamespaceAware(true);
		try {
			final XMLReader xr = spf.newSAXParser().getXMLReader();
			xr.setEntityResolver(new MappingsEntityResolver(Paths.get(xmlFile).getParent().toString()));
			InputStream xml = new FileInputStream(xmlFile);
			final InputSource inputsource = SAXSource.sourceToInputSource(new StreamSource(xml));
			final SAXSource saxSource = new SAXSource(xr, inputsource);

			return JAXB.unmarshal(saxSource, CMDI2CIDOCMap.class);

		} catch (ParserConfigurationException | SAXException | IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * reads packed CMDI2CIDOC.xml and transforms it into CMDI2CIDOCMap java object
	 * 
	 * @return CMDI2CIDOCMap instance
	 */
	public static CMDI2CIDOCMap unmarshall() {
		final SAXParserFactory spf = SAXParserFactory.newInstance();		
		spf.setXIncludeAware(true);
		spf.setNamespaceAware(true);
		try {
			final XMLReader xr = spf.newSAXParser().getXMLReader();			
			InputStream xml = Concepts2CIDOCFactory.class.getResourceAsStream(CONCEPT_MAP);
			final InputSource inputsource = SAXSource.sourceToInputSource(new StreamSource(xml));
			final SAXSource saxSource = new SAXSource(xr, inputsource);

			return JAXB.unmarshal(saxSource, CMDI2CIDOCMap.class);

		} catch (ParserConfigurationException | SAXException ex) {
			throw new RuntimeException(ex);
		}
	}


	/**
	 * Custom resolver for xml entities 
	 * 
	 * @author dostojic
	 *
	 * @see EntityResolver
	 */
	static class MappingsEntityResolver implements EntityResolver {
		private final String dir;
		
		 public MappingsEntityResolver(String dir) {
			this.dir = dir;
		}

		@Override
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			int ind = systemId.lastIndexOf('/');
			if(ind != -1){
				String file = dir + systemId.substring(ind);
				StringReader strReader = new StringReader(file);
				return new InputSource(strReader);
			}else{
				return null;
			}
		}
	}

}
