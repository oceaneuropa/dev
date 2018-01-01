package org.spirit.world.api;

import org.spirit.world.api.preference.IPreferenceSettings;
import org.spirit.world.api.program.IPrograms;
import org.spirit.world.api.user.IUser;

public class WorldInput {

	protected IUser user;
	protected IPrograms programs;
	protected IPreferenceSettings settings;

	public WorldInput() {
	}

	public IUser getUser() {
		return this.user;
	}

	public void setUser(IUser user) {
		this.user = user;
	}

	public IPrograms getPrograms() {
		return this.programs;
	}

	public void setPrograms(IPrograms programs) {
		this.programs = programs;
	}

	public IPreferenceSettings getPreferenceSettings() {
		return this.settings;
	}

	public void setPreferenceSettings(IPreferenceSettings prefSettings) {
		this.settings = prefSettings;
	}

}
