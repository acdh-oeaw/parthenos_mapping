package at.ac.acdh.transformer.utils;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * 
 * @author dostojic
 * 
 * Util class for marshaling and demarshaling XMLs using JAXB
 *
 */
public class XMLIOService<T> {

	public T unmarshal(InputStream is) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance();
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		return (T) unmarshaller.unmarshal(is);
	}

	
	public void marshal(T t, OutputStream os) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(t.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8");
		jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.w3.org/2001/XMLSchema-instance");
		jaxbMarshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "x3ml_v1.0.xsd");

		jaxbMarshaller.marshal(t, os);
	}

}
