package ac.at.acdh.transform;

import java.io.FileOutputStream;

import ac.at.acdh.x3ml.X3ML;

public class Main {

	public static void main(String[] args) throws Exception {
		CMDHeaderMapper cmd = new CMDHeaderMapper();
		XMLMarshaller<X3ML> xmlUtils = new XMLMarshaller<>(X3ML.class);
		
		if(args.length > 0){
			xmlUtils.marshal(cmd.mapToX3ML(), new FileOutputStream(args[0]));
		}else{
			xmlUtils.marshal(cmd.mapToX3ML(), System.out);
		}		
		
	}

}
