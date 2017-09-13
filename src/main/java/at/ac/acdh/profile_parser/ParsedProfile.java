package at.ac.acdh.profile_parser;


import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 
 * <p>This class keeps information from processed CLARIN profile and provides API for analyzing xpaths,
 *  concepts and components found in one profile</p>
 * 
 * <p>Information are kept in map(xpath, {@link CMDINode})</p>
 * @author dostojic
 *
 */
public class ParsedProfile {
	
	final Map<String, CMDINode> xpaths;
	
	ParsedProfile(Map<String, CMDINode> xpaths){
		this.xpaths = xpaths;
	}
	
	/**
	 * 
	 * @return list of components used in profile
	 * 
	 * @see CMDINode Component
	 */
	public Collection<CMDINode> getComponents(){
		return xpaths.entrySet()
		.stream()
		.filter(e -> e.getValue().component != null)
		.map(e -> e.getValue())
		.collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @param componentId
	 * @return xpaths for the specified component in profile
	 */
	public Collection<String> getXPathsForComponent(String componentId){
		return xpaths.entrySet()
			.stream()
			.filter(e -> e.getValue().component != null && e.getValue().component.id.equals(componentId))
			.map(e -> e.getKey())
			.collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @return "submap" created by filtering entries that are components
	 */
	public Map<String, CMDINode> getElements(){
		return xpaths.entrySet()
		.stream()
		.filter(e -> e.getValue().component == null)
		.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (x,n)->x, LinkedHashMap::new));
	}
	
	/**
	 * 
	 * @return xpaths for the profile
	 */
	public Collection<String> getXPaths(){
		return xpaths.keySet();
	}
	
	/**
	 * 
	 * @param concept
	 * @return xpaths for the given concept
	 */
	public Collection<String> getXPathsForConcept(String concept){		
		return xpaths.entrySet()
		.stream()
		.filter(e -> e.getValue().concept != null && e.getValue().concept.equals(concept))
		.map(e -> e.getKey())
		.collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @param concept
	 * @param root required root path of the identified xpath
	 * @return xpaths for the given concept which have the required root path
	 */
	public Collection<String> getXPathsForConcept(String root, String concept){		
		return xpaths.entrySet()
		.stream()
		.filter(e -> e.getValue().concept != null && e.getValue().concept.equals(concept) && e.getKey().startsWith(root))
		.map(e -> e.getKey())
		.collect(Collectors.toList());
	}	
	
	/**
	 * 
	 * @param xpath
	 * @return concept for the given xpath
	 */
	public String getConcept(String xpath){
		return xpaths.get(xpath) != null? (xpaths.get(xpath).concept != null? xpaths.get(xpath).concept : null) : null;
	}
	
	/**
	 * Checks if profile has specified profiles. Works also with generic xpaths
	 * 
	 * @param xpath
	 * @return true if profile has specified xpath
	 */
	//xpath will be pre-processed -> remove conditions, text()
	public boolean hasXPath(String xpath){
		final String normalisedXPath = preprocessXPath(xpath);
		if(normalisedXPath.contains("//")){//handle generic xpaths
			return xpaths.keySet().stream()
			.filter(x -> {
				String[] chunks = normalisedXPath.split("//");
				int curInd = 0;
				for(int i= 0; i < chunks.length; i++){
					if(curInd > x.length())
						return false;			
					if(i == 0 && !chunks[0].isEmpty()){
						if(x.startsWith(chunks[i])){
							curInd += chunks[0].length() + 1; // first after '/'					
							continue;
						}else
							return false;
					}else{
						curInd = x.indexOf(chunks[i], curInd);
						if(curInd == -1)
							return false;
						curInd += chunks[i].length() + 1; // first after '/'				
					}
				}						
				return true;						
			})
			.findFirst() //match first xpath in the map
			.orElse(null) //if no match return null
			!= null? true : false;			
		}else
			return xpaths.containsKey(xpath);
	}
	
	/**
	 * 
	 * @param xpath
	 * @return xpath without conditions
	 */
	private String preprocessXPath(String xpath){		
		//remove conditions
		xpath = xpath.replaceAll("\\[.*?\\]", "");
		//remove text()
//		xpath = xpath.replace("/text()", "");
		//remove attributes
		xpath = xpath.replaceAll("/@.*", "/");		
		return xpath;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		xpaths.forEach((x, n) -> sb.append("\t").append(x + ": " + n).append("\n"));
		
		return sb.toString();
	}
}
