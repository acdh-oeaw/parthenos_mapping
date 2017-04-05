package at.ac.acdh.profile_parser;

/**
 * 
 * This class is similar to {@link CRElement} and it contains information about concepts and components. 
 * It is "value" part of {@link ParsedProfile#xpaths} map
 * 
 * @see ParsedProfile CRElement Component
 * 
 * 
 * @author dostojic
 *
 */
public class CMDINode{
	
	boolean isRequired;
	String concept;
	Component component;
	
	@Override
    public String toString() {
    	return (component != null? component.toString() : concept != null? concept.toString() : "");
    }
	
	/**
	 * This class represents component from CLARIN Component Registry with name and unique id. In case that  element is a component,
	 *  {@link CMDINode} will reference an instantiation of this class
	 *  
	 *  @see CMDINode {@link ParsedProfile#getComponents()}	 *
	 */
	public static class Component{
		public String name;
		public String id;
		
		public Component(String name, String id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String toString() {
			return "component: " + name + " / " + id;
		}
	}
	
}
