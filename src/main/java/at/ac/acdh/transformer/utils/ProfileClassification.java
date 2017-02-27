package at.ac.acdh.transformer.utils;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "profiles")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProfileClassification {

	@XmlElementWrapper(name = "type")
	private List<Type> types;	

	public List<Type> getTypes() {
		return types;
	}

	public void setTypes(List<Type> types) {
		this.types = types;
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Type {
		@XmlAttribute
		private String entity;

		@XmlElementWrapper(name = "profile")
		List<Profile> profiles;

		public String getEntity() {
			return entity;
		}

		public void setEntity(String entity) {
			this.entity = entity;
		}

		public List<Profile> getProfiles() {
			return profiles;
		}

		public void setProfiles(List<Profile> profiles) {
			this.profiles = profiles;
		}
		
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Profile {
		@XmlAttribute
		private String id;

		@XmlAttribute
		private String name;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
