package at.ac.acdh.transformer.utils;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import at.ac.acdh.transformer.utils.ProfileClassification.Profile;
import at.ac.acdh.transformer.utils.ProfileClassification.Type;

public class ProfileClassificationService{
	
	private Map<String, String> profile2PEMap; 
	
	public ProfileClassificationService(){
		try{
			ProfileClassification profiles = (ProfileClassification) new XMLIOService().unmarshal(ProfileClassification.class, 
				ProfileClassificationService.class.getResourceAsStream("/profileClassification.xml"));
			
			profile2PEMap = new HashMap<>();
			for(Type type: profiles.getTypes())
				for(Profile profile: type.getProfiles()){
					profile2PEMap.put(profile.getId(), type.getEntity());
				}
		
		}catch(JAXBException e){
			throw new RuntimeException("Unable to read profileClassification.xml. Check if it is on classpath and it is valid", e);
		}
		
	}
	
	public String getType(String profileId){
		String type = profile2PEMap.get(profileId); 
		return type != null? type : "crmpe:PE24_Volatile_Dataset";
	}
	
}
