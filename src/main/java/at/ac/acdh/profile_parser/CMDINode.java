package at.ac.acdh.profile_parser;

public class CMDINode{
	
	public boolean isRequired;
	public String concept;
	public Component component;
	
	@Override
    public String toString() {
    	return (component != null? component.toString() : concept != null? concept.toString() : "");
    }
	
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
