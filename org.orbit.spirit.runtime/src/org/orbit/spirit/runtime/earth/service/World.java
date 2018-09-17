package org.orbit.spirit.runtime.earth.service;

import java.util.List;

public interface World {

	String getGaiaId();

	void setGaiaId(String gaiaId);

	String getEarthId();

	void setEarthId(String earthId);

	String getId();

	void setId(String id);

	String getName();

	void setName(String name);

	List<String> getAccountIds();

}
