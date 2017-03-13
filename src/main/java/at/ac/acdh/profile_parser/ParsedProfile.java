package at.ac.acdh.profile_parser;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ParsedProfile {
	
	final Map<String, CMDINode> xpaths;
	
	ParsedProfile(Map<String, CMDINode> xpaths){
		this.xpaths = xpaths;
	}
	
	public Collection<CMDINode> getComponents(){
		return xpaths.entrySet()
		.stream()
		.filter(e -> e.getValue().component != null)
		.map(e -> e.getValue())
		.collect(Collectors.toList());
	}
	
	public Collection<String> getXPathsForComponent(String componentId){
		return xpaths.entrySet()
			.stream()
			.filter(e -> e.getValue().component != null && e.getValue().component.id.equals(componentId))
			.map(e -> e.getKey())
			.collect(Collectors.toList());
	}
	
	public Map<String, CMDINode> getElements(){
		return xpaths.entrySet()
		.stream()
		.filter(e -> e.getValue().component == null)
		.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (x,n)->x, LinkedHashMap::new));
	}
	
	public Collection<String> getXPaths(){
		return xpaths.keySet();
	}
	
	public Collection<String> getXPathsForConcept(String concept){		
		return xpaths.entrySet()
		.stream()
		.filter(e -> e.getValue().concept != null && e.getValue().concept.equals(concept))
		.map(e -> e.getKey())
		.collect(Collectors.toList());
	}
	
	public String getConcept(String xpath){
		return xpaths.get(xpath) != null? (xpaths.get(xpath).concept != null? xpaths.get(xpath).concept : null) : null;
	}
	
	
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
	
	//for generic xpaths 
	private Collection<CMDINode> getNodesForXPath(String xpath){
		if(xpaths.containsKey(xpath))
			return Arrays.asList(xpaths.get(xpath));		
		
		return null;
	}
	
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
