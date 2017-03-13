package at.ac.acdh.profile_parser;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.ximpleware.VTDException;
import com.ximpleware.VTDGen;

import at.ac.acdh.profile_parser.CMDINode.Component;
import at.ac.acdh.profile_parser.CRElement.NodeType;

class CMDI1_2_ProfileParser extends ProfileParser{
	
	static final String ENVELOPE_URL = "https://infra.clarin.eu/CMDI/1.2/xsd/cmd-envelop.xsd";
	
	private static Map<String, CMDINode> envelope = null;

	@Override
	protected String getCMDVersion() {
		return "1.2";
	}
	
	@Override
	protected String getNamespace() {
		return "cmdp";
	}
	
	@Override
	protected String conceptAttributeName(){
		return "cmd:ConceptLink";
	}	
	
	@Override
	protected CRElement processNameAttributeNode() throws VTDException {
		//ref attribute
		String ref = extractAttributeValue("ref");
		if(ref != null){			
			switch(ref){//we care only about cmd:ComponentId
				case "cmd:ComponentId":
					CRElement elem = new CRElement();
					elem.isLeaf = true;
					elem.lvl = vn.getCurrentDepth();
					elem.type = NodeType.COMPONENT;
					elem.ref = extractAttributeValue("fixed");
					return elem;
				default:
					return null;
			}
		}
		
		//name attribute
		String name = extractAttributeValue("name");
		if(name != null){
			String concept = extractAttributeValue("ConceptLink");
			CRElement elem = new CRElement();
			elem.isLeaf = true;
			elem.lvl = vn.getCurrentDepth();
			elem.type = NodeType.ATTRIBUTE;
			elem.ref = 	concept;
			elem.name = name;
			return elem;
		}
		
		return null;		
	}
	
	@Override
	protected Map<String, CMDINode> createMap(Collection<CRElement> nodes) throws VTDException {
		Map<String, CMDINode> xpaths = new LinkedHashMap<>();
		
		//add xpaths from envelope
		if(envelope == null){
			CMDI1_1_ProfileParser envelopeParser = new CMDI1_1_ProfileParser();
			VTDGen vg = new VTDGen();
			envelope = envelopeParser.parse(ENVELOPE_URL, namespace).xpaths;
		}
		xpaths.putAll(envelope);
				
		nodes.stream()
		.filter(n -> n.isLeaf || n.type == NodeType.COMPONENT)
		.forEach(node -> {
			String xpath = "";
			CRElement parent = node.parent;
			while (parent != null) {
				xpath = parent.name + "/" + xpath;
				parent = parent.parent;
			}
			

			String prefix = (namespace)? "/cmd:CMD/cmd:Components/" : "/CMD/Components/";			
			xpath = prefix + xpath + (node.type == NodeType.ATTRIBUTE || node.type == NodeType.CMD_VERSION_ATTR
					? "@" + node.name : node.name + "/text()");
			
			CMDINode cmdiNode = new CMDINode();
			cmdiNode.isRequired = node.isRequired;
			
			if(node.type == NodeType.COMPONENT)
				cmdiNode.component = new Component(node.name, node.ref);
			else
				cmdiNode.concept = node.ref;

			xpaths.put(xpath, cmdiNode);
		});		

		return xpaths;
	}

}
