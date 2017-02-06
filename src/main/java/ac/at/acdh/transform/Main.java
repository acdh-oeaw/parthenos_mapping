package ac.at.acdh.transform;

import ac.at.acdh.transform.utils.CMDICreatorType;
import ac.at.acdh.transform.utils.CMDIResourceType;
import ac.at.acdh.transform.utils.XMLMarshaller;
import ac.at.acdh.x3ml.X3ML;

public class Main {

	public static void main(String[] args) throws Exception {
		ProfileTransformer transformer = new ProfileTransformer();
		XMLMarshaller<X3ML> xmlUtils = new XMLMarshaller<>(X3ML.class);
		
		
		X3ML service = transformer.transform("https://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/1.x/profiles/clarin.eu:cr1:p_1288172614026/xsd",
				CMDICreatorType.SOFTWARE, CMDIResourceType.SERVICE);
		
		X3ML dataset = transformer.transform("https://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/1.x/profiles/clarin.eu:cr1:p_1271859438164/xsd",
				CMDICreatorType.ACTOR, CMDIResourceType.DATASET);
		
		xmlUtils.marshal(dataset, System.out);
	
		
	}

}
