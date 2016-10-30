package org.orbit.component.api.appstore;

import java.util.Date;

public interface AppManifest {

	/**
	 * Get app id.
	 * 
	 * @return
	 */
	String getAppId();

	/**
	 * Get app namespace.
	 * 
	 * @return
	 */
	String getNamespace();

	/**
	 * Get app categoryId.
	 * 
	 * @return
	 */
	String getCategoryId();

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
