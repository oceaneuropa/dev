package org.orbit.component.api.tier2.appstore;

import java.util.Date;

public interface AppManifest {

	int getId();

	String getAppId();

	String getAppVersion();

	String getType();

	String getName();

	String getManifest();

	String getFileName();

	long getFileLength();

	String getDescription();

	Date getDateCreated();

	Date getDateModified();

}
