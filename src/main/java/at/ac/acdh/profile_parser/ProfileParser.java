package at.ac.acdh.profile_parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

import at.ac.acdh.profile_parser.CRElement.NodeType;

abstract class ProfileParser{
	
	private static final Logger _logger = LoggerFactory.getLogger(ProfileParser.class);
	
	protected VTDNav vn;
	protected boolean namespace;
	
	private CRElement _cur;
	
	public ParsedProfile parse(String schema, boolean namespace) throws VTDException{
		this.namespace = namespace;
		VTDGen gen = new VTDGen();
		gen.parseHttpUrl(schema, true);
		vn = gen.getNav();
		Map<String, CMDINode> xpaths = createMap(processElements());
		
		if(namespace){
			Map<String, CMDINode> fixedNamespaceXpaths = new HashMap<>();
			for(Entry<String, CMDINode> node: xpaths.entrySet()){
				String xpath;
				if(!node.getKey().startsWith("/cmdp:CMD/cmdp:Components/")){
					xpath = node.getKey().replaceAll("cmdp:", "cmd:");
				}else{
					xpath = node.getKey().replaceFirst("/cmdp:CMD/cmdp:Components/", "/cmd:CMD/cmd:Components/");
				}				
				fixedNamespaceXpaths.put(xpath, node.getValue());
			}
			
			xpaths = fixedNamespaceXpaths;
		}
		 
		
		return new ParsedProfile(xpaths);
	}
	
	protected abstract String getCMDVersion();	
	protected abstract String conceptAttributeName();
	protected abstract CRElement processNameAttributeNode() throws VTDException;
	protected abstract Map<String, CMDINode> createMap(Collection<CRElement> nodes) throws VTDException;
	
//	protected ProfileHeader fillInHeader(VTDNav vn, ProfileHeader header) throws VTDException{
//		AutoPilot ap = new AutoPilot(vn);
//		header.id = 		evaluateXPath("//Header/ID/text()", ap);		
//		header.name = 		evaluateXPath("//Header/Name/text()", ap);
//		header.description = evaluateXPath("//Header/Description/text()", ap);
//		header.status = 	evaluateXPath("//Header/Status/text()", ap);
//		header.cmdiVersion = getCMDVersion();
//		return header;
//		
//	}
	
	private Collection<CRElement> processElements() throws VTDException{
		Collection<CRElement> nodes = new ArrayList<>();
		vn.toElement(VTDNav.ROOT);// reset
		AutoPilot ap = new AutoPilot(vn);
		
		ap.selectElement("element");
		while (ap.iterate()){// process single element
			CRElement _new = new CRElement();
			_new.isLeaf = !isComplex();

			_new.name = extractAttributeValue("name");
			if (_new.name == null) {
				_new.name = "";
				_logger.error("Element at {} doenst have specified name, xpaths will be invalid", vn.getCurrentIndex());
			}else if(namespace){
				_new.name = "cmdp:" + _new.name;
			}
			
			
			String minOccurs = extractAttributeValue("minOccurs");
			_new.isRequired = minOccurs != null ? !minOccurs.equals("0") : true;

			_new.lvl = vn.getCurrentDepth();

			// getAttrs
			vn.push();
			Collection<CRElement> attributes = getAttributes();
			vn.pop();
			CRElement component = attributes.stream().filter(attr -> attr.type == NodeType.COMPONENT).findAny()
					.orElse(null);

			if (component != null) { // component
				_new.type = NodeType.COMPONENT;
				_new.ref = component.ref;
			} else {
				_new.type = NodeType.ELEMENT;			
				_new.ref = extractAttributeValue(conceptAttributeName());				
			}

			// resolve parent
			if (_cur != null) {// if not root
				if (_new.lvl > _cur.lvl)
					_new.parent = _cur;
				else if (_new.lvl == _cur.lvl)
					_new.parent = _cur.parent;
				else {// child.lvl < lvl -> sibling of parent('s parent)
					CRElement parent = _cur.parent;
					while (_new.lvl <= parent.lvl)
						parent = parent.parent;
					_new.parent = parent;
				}
			}
			_cur = _new;
			nodes.add(_new);

			// add attribute nodes
			attributes.stream().filter(attr -> attr.type == NodeType.ATTRIBUTE || attr.type == NodeType.CMD_VERSION_ATTR)
					.forEach(attr -> {
						attr.parent = _new;
						nodes.add(attr);
					});			
		}		
		return nodes;		
	}
	
	private boolean isComplex() throws VTDException {
		vn.push();
		AutoPilot ap = new AutoPilot(vn);
		ap.selectXPath("./complexType/sequence");
		boolean isComplex = ap.evalXPathToBoolean();
		vn.pop();		
		return isComplex;		
	}

	private Collection<CRElement> getAttributes() throws VTDException {
		Collection<CRElement> attributes = new ArrayList<>();

		vn.push();
		AutoPilot ap = new AutoPilot(vn);
		ap.selectXPath("./complexType/simpleContent/extension/attribute");
		while (ap.evalXPath() != -1) {
			CRElement nameAttr = processNameAttributeNode();
			if (nameAttr != null)
				attributes.add(nameAttr);
		}
		vn.pop();

		// teiHeader.xsd has attributes on different xpath
		if (attributes.isEmpty()) {// paths are exclusive
			ap.bind(vn);
			ap.selectXPath("./complexType/attribute");
			while (ap.evalXPath() != -1) {
				CRElement nameAttr = processNameAttributeNode();				
				if (nameAttr != null)
					attributes.add(nameAttr);
			}
		}
		return attributes;
	}	

	//utils
	protected String evaluateXPath(String xpath, AutoPilot ap) throws VTDException{
		ap.selectXPath(xpath);
		int index = ap.evalXPath();
		return index != -1? vn.toString(index).trim() : null;		
	}

	protected String extractAttributeValue(String attrName) throws NavException {
		int ind = vn.getAttrVal(attrName);
		return ind != -1 ? vn.toNormalizedString(ind) : null;
	}
}
