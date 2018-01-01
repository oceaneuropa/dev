package org.spirit.world.api.preference;

public interface IPreferenceSettings {

	IPreferenceSetting getSystemPreferenceSetting(String categoryId);

	IPreferenceSetting getProgramPreferenceSetting(String programId);

}
