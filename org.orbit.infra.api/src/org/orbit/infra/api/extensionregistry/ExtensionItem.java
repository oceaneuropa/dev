package org.orbit.infra.api.extensionregistry;

import java.util.Map;

public interface ExtensionItem {

	public String getPlatformId();

	public String getTypeId();

	public String getExtensionId();

	public String getName();

	public String getDescription();

	public Map<String, Object> getProperties();

}

// public Integer getId();
