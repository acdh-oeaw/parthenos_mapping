package at.ac.acdh.profile_parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

import at.ac.acdh.profile_parser.CRElement.NodeType;


 /**
  * <p>Abstract class responsible for parsing CLARIN profile from URL. 
  * <a href="http://vtd-xml.sourceforge.net/">VTD-XML</a> library is used as xml processor</p>
  * <p>Concrete implementations are able to parse profiles with specific CMDI version</p>
  * 
  * @see CMDI1_1_ProfileParser CMDI1_2_ProfileParser
  * 
 * @author dostojic
 *
 */
abstract class ProfileParser{
	
	private static final Logger _logger = LoggerFactory.getLogger(ProfileParser.class);
	
	protected VTDNav vn;
	protected boolean namespace;
	
	private CRElement _cur;
	
	/** 
	 * 
	 * @param schema - url of profile's xsd
	 * @param namespace - should construct xpaths with namespace
	 * @return an instance of {@link ParsedProfile}
	 * @throws VTDException if parsing of XSD fails
	 */
	public ParsedProfile parse(String schema, boolean namespace) throws VTDException{
		this.namespace = namespace;
		VTDGen gen = new VTDGen();
		gen.parseHttpUrl(schema, true);
		vn = gen.getNav();
		Map<String, CMDINode> xpaths = createMap(processElements());
		
		return new ParsedProfile(xpaths);
	}	

	/**
	 * 
	 * @return version of CMDI that parser can process
	 */
	protected abstract String getCMDVersion();
	
	/** 
	 * @return namespace qualifier
	 */
	protected abstract String getNamespace();
	/** 
	 * @return name of the xml attribute holding link to a concept
	 */
	protected abstract String conceptAttributeName();
	/** 
	 * this method creates an {@link CRElement} object based on the value of "name" attribute of current element
	 * @return {@link CRElement}
	 * @throws VTDException
	 */
	protected abstract CRElement processNameAttributeNode() throws VTDException;
	/** 
	 * this method is called at the end of parsing and for each found node creates xpath. Other information like concept or component are kept in {@link CMDINode} object
	 * @param nodes - {@link CRElement} nodes to be procesed
	 * @return xpath - {@link CMDINode} map
	 * @throws VTDException
	 */
	protected abstract Map<String, CMDINode> createMap(Collection<CRElement> nodes) throws VTDException;
	
	/**
	 * This method parses XSD and for each occurrence of an element creates one {@link CRElement},
	 *  an internal representation of xsd elements 
	 * 
	 * @return list of {@link CRElement}
	 * @throws VTDException
	 */
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
				_new.name = getNamespace() + ":" + _new.name;
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
	
	/**
	 * 
	 * @return if element contains other elements 
	 * @throws VTDException
	 */
	private boolean isComplex() throws VTDException {
		vn.push();
		AutoPilot ap = new AutoPilot(vn);
		ap.selectXPath("./complexType/sequence");
		boolean isComplex = ap.evalXPathToBoolean();
		vn.pop();		
		return isComplex;		
	}

	/**
	 * 
	 * @return list of attributes for the current element
	 * @throws VTDException
	 */
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

	/**
	 * 
	 * @param attrName - name of the attribute of current element
	 * @return value of the attribute
	 * @throws NavException 
	 */
	protected String extractAttributeValue(String attrName) throws NavException {
		int ind = vn.getAttrVal(attrName);
		return ind != -1 ? vn.toNormalizedString(ind) : null;
	}
}
