package org.orbit.platform.model.platform.dto;

import java.util.Map;

public interface ServiceInstanceInfo {

	String getExtensionTypeId();

	String getExtensionId();

	Map<String, Object> getProperties();

}
