package org.origin.common.launch.impl;

import org.origin.common.extensions.core.IExtension;
import org.origin.common.launch.LaunchType;

public class LaunchTypeImpl implements LaunchType {

	protected IExtension extension;
	protected String[] launcherIds;

	/**
	 * 
	 * @param extension
	 */
	public LaunchTypeImpl(IExtension extension) {
		this.extension = extension;
	}

	@Override
	public String getId() {
		return this.extension.getId();
	}

	@Override
	public String getName() {
		return this.extension.getName();
	}

}

// @Override
// public synchronized String[] getLauncherIds() {
// if (this.launcherIds == null) {
// Object propValue = this.extension.getProperty(LaunchType.PROP_LAUNCHER_IDS);
// if (propValue instanceof String[]) {
// this.launcherIds = (String[]) propValue;
//
// } else if (propValue instanceof String) {
// String propString = (String) propValue;
// if (propString.contains(",")) {
// this.launcherIds = propString.split(",");
// } else {
// this.launcherIds = new String[] { propString };
// }
// }
// }
// if (this.launcherIds == null) {
// return new String[0];
// }
// return this.launcherIds;
// }
