package org.orbit.infra.api.subs;

import java.util.Map;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface SubsSource {

	SubsServerAPI getAPI();

	void setAPI(SubsServerAPI api);

	Integer getId();

	void setId(Integer id);

	String getType();

	void setType(String type);

	String getInstanceId();

	void setInstanceId(String instanceId);

	String getName();

	void setName(String name);

	Map<String, Object> getProperties();

	long getDateCreated();

	void setDateCreated(long dateCreated);

	long getDateModified();

	void setDateModified(long dateModified);

}