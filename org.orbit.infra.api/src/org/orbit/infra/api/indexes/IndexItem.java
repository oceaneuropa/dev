package org.orbit.infra.api.indexes;

import java.util.Map;

public interface IndexItem {

	public Integer getIndexItemId();

	public String getIndexProviderId();

	public String getType();

	public String getName();

	public Map<String, Object> getProperties();

}
