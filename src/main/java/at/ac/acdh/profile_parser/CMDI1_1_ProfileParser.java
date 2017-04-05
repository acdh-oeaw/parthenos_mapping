package at.ac.acdh.profile_parser;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.ximpleware.VTDException;

import at.ac.acdh.profile_parser.CMDINode.Component;
import at.ac.acdh.profile_parser.CRElement.NodeType;

/**
 * 
 * This class is responsible for parsing CMDI 1.1 profiles 
 * 
 * @see ProfileParser
 * @author dostojic
 *
 */
class CMDI1_1_ProfileParser extends ProfileParser{
	

	/**
	 * @return 1.1
	 * @see ProfileParser#getCMDVersion()
	 */
	@Override
	protected String getCMDVersion() {
		return "1.1";
	}
	
	/**
	 * @return cmd
	 * @see ProfileParser#getNamespace()
	 */
	@Override
	protected String getNamespace() {
		return "cmd";
	}
	
	/**
	 * @return dcr:datcat
	 * @see ProfileParser#conceptAttributeName()
	 */
	@Override
	protected String conceptAttributeName(){
		return "dcr:datcat";
	}		

	/**
	 * @see ProfileParser#processNameAttributeNode()
	 */
	@Override
	protected CRElement processNameAttributeNode() throws VTDException {
		String name = extractAttributeValue("name");
		if(name == null)
			return null;
		
		CRElement elem = new CRElement();
		elem.isLeaf = true;
		elem.lvl = vn.getCurrentDepth();
		
		switch (name) {
			case "ref": 		// ignore
			case "CMDVersion":  // ignore
				elem = null;
				break;
			case "ComponentId":
				elem.type = NodeType.COMPONENT;
				elem.ref = extractAttributeValue("fixed");
				break;
			default: // attribute
				elem.type = NodeType.ATTRIBUTE;
				elem.ref = extractAttributeValue(conceptAttributeName());				
				elem.name = name;				
		}
		
		return elem;
	}
	
	/**
	 * @see ProfileParser#createMap(Collection)
	 */
	@Override
	protected Map<String, CMDINode> createMap(Collection<CRElement> nodes) throws VTDException {
		Map<String, CMDINode> xpaths = new LinkedHashMap<>();
		
		nodes.stream()
		.filter(n -> n.isLeaf || n.type == NodeType.COMPONENT)
		.forEach(node -> {
			String xpath = "";
			CRElement parent = node.parent;
			while (parent != null) {
				xpath = parent.name + "/" + xpath;
				parent = parent.parent;
			}
			
			
			xpath = "/" + xpath + (node.type == NodeType.ATTRIBUTE || node.type == NodeType.CMD_VERSION_ATTR
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
