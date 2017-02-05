package ac.at.acdh.transform;

import java.io.FileOutputStream;

import ac.at.acdh.transform.commons.CMDICreatorType;
import ac.at.acdh.x3ml.X3ML;

public class Main {

	public static void main(String[] args) throws Exception {
		CMDHeaderMapper cmd = new CMDHeaderMapper();
		XMLMarshaller<X3ML> xmlUtils = new XMLMarshaller<>(X3ML.class);
		
		CMDICreatorType creator = args[0].equals("software")? CMDICreatorType.SOFTWARE : CMDICreatorType.ACTOR;
		
		if(args.length > 1){
			xmlUtils.marshal(cmd.mapToX3ML(creator), new FileOutputStream(args[1]));
		}else{
			xmlUtils.marshal(cmd.mapToX3ML(creator), System.out);
		}		
		
	}

}
