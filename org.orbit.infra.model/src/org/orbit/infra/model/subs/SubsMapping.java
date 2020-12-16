package org.orbit.infra.model.subs;

import java.util.Date;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface SubsMapping {

	Integer getId();

	void setId(Integer id);

	Integer getSourceId();

	void setSourceId(Integer sourceId);

	Integer getTargetId();

	void setTargetId(Integer targetId);

	String getClientId();

	void setClientId(String clientId);

	String getClientURL();

	void setClientURL(String clientURL);

	Date getClientHeartbeatTime();

	void setClientHeartbeatTime(Date heartbeatTime);

	String getServerId();

	void setServerId(String serverId);

	String getServerURL();

	void setServerURL(String serverURL);

	Date getServerHeartbeatTime();

	void setServerHeartbeatTime(Date heartbeatTime);

	Map<String, String> getProperties();

	Date getCreatedTime();

	void setCreatedTime(Date createdTime);

	Date getModifiedTime();

	void setModifiedTime(Date createdTime);

}
