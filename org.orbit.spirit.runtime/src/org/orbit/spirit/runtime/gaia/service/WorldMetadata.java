package org.orbit.spirit.runtime.gaia.service;

import java.util.Map;

public interface WorldMetadata {

	String getName();

	void setName(String name);

	Map<String, String> getStatus();

	void setStatus(Map<String, String> status);

	void setStatus(String key, String value);

}
