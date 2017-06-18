package org.orbit.component.api.tier2.appstore;

import java.util.Date;

public interface AppManifest {

	/**
	 * Get app id.
	 * 
	 * @return
	 */
	String getAppId();

	/**
	 * Get app name.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Get app version.
	 * 
	 * @return
	 */
	String getVersion();

	/**
	 * Get app type.
	 * 
	 * @return
	 */
	String getType();

	/**
	 * 
	 * @return
	 */
	int getPriority();

	/**
	 * 
	 * @return
	 */
	String getFileName();

	/**
	 * Get app description.
	 * 
	 * @return
	 */
	String getDescription();

	/**
	 * 
	 * @return
	 */
	Date getDateCreated();

	/**
	 * 
	 * @return
	 */
	Date getDateModified();

	/**
	 * 
	 * @return
	 */
	String getManifestString();

}
