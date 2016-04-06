package org.nb.drive.rest;

import java.util.Properties;

import org.nb.drive.api.DriveConfig;

public class RestDriveConfig implements DriveConfig {

	protected Properties properties;

	/**
	 * @param properties
	 */
	public RestDriveConfig(Properties properties) {
		this.properties = properties;
	}

}
