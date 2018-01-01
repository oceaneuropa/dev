package org.spirit.world.api.preference;

public interface IPreferenceSetting {

	String[] getPreferenceNames();

	String getStringValue(String name);

	void setStringValue(String name, String value);

}
