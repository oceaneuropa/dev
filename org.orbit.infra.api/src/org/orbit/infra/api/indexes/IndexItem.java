package org.orbit.infra.api.indexes;

import java.util.Map;

public interface IndexItem {

	// index item properties
	public static String LAST_HEARTBEAT_TIME = "last_heartbeat_time";

	public Integer getIndexItemId();

	public String getIndexProviderId();

	public String getType();

	public String getName();

	public Map<String, Object> getProperties();

}
