package org.orbit.infra.api.indexes;

import java.util.Date;
import java.util.Map;

public interface IndexItem {

	Integer getIndexItemId();

	String getIndexProviderId();

	String getType();

	String getName();

	Map<String, Object> getProperties();

	Date getCreateTime();

	Date getUpdateTime();

}
