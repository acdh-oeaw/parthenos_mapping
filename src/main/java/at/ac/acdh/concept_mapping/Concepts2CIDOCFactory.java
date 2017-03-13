package at.ac.acdh.concept_mapping;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXB;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class Concepts2CIDOCFactory {
	
	private static String CONCEPT_MAP = "/mapping/CMDI2CIDOC.xml";
	
	public static CMDI2CIDOCMap unmarshall(){
		final SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setXIncludeAware(true);
		spf.setNamespaceAware(true);
		try {
			final XMLReader xr = spf.newSAXParser().getXMLReader();
			final InputSource inputsource = SAXSource.sourceToInputSource(getXMLAsSource());
			final SAXSource saxSource = new SAXSource(xr, inputsource);
			
			return JAXB.unmarshal(saxSource, CMDI2CIDOCMap.class);
			
		} catch (ParserConfigurationException | SAXException | IOException ex) {
			throw new RuntimeException(ex);
		}		
	}
	
	private static Source getXMLAsSource() throws IOException{
		try{
			URL mappingXML = Concepts2CIDOCFactory.class.getResource(CONCEPT_MAP);		
			return new StreamSource(mappingXML.openStream(), mappingXML.toURI().toString());
		}catch(URISyntaxException | IOException e){
			throw new IOException(e);
		}
		
	}
	
}
