package config;

import java.io.File;
import java.util.prefs.Preferences;

public class UserSettings {
	private static Preferences pref = 
			Preferences.userRoot();
	
	public static String DEFAULT_DESTINATION = 
			System.getProperty("user.home") + File.separator + "Downloads";
		
	private static String DESTINATION = "destination";
		
	public String getDestinationFolder() {
		return pref.get(DESTINATION, DEFAULT_DESTINATION);
	}
	
	public void setDestinationFolder(String destination) {
		if (destination != null)
			pref.put(DESTINATION, destination);
		else
			pref.put(DESTINATION, DEFAULT_DESTINATION);
	}
	
	public Preferences getPreferences() {
		return pref;
	}
}
