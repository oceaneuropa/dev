package org.orbit.infra.model.subs;

import java.util.Map;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface SubsTarget {

	Integer getId();

	void setId(Integer id);

	String getType();

	void setType(String type);

	String getTypeId();

	void setTypeId(String typeId);

	String getName();

	void setName(String name);

	String getServerId();

	void setServerId(String serverId);

	String getServerURL();

	void setServerURL(String serverURL);

	long getServerHeartbeatTime();

	void setServerHeartbeatTime(long heartbeatTime);

	Map<String, Object> getProperties();

	long getDateCreated();

	void setDateCreated(long dateCreated);

	long getDateModified();

	void setDateModified(long dateModified);

}
