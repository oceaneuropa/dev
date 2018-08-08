package org.orbit.spirit.runtime.gaia.world;

import java.util.Map;

public interface World {

	String getName();

	void setName(String name);

	Map<String, String> getStatus();

	void setStatus(Map<String, String> status);

	void setStatus(String key, String value);

}
