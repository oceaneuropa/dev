package org.orbit.infra.model.subs;

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

	long getClientHeartbeatTime();

	void setClientHeartbeatTime(long heartbeatTime);

	Map<String, Object> getProperties();

	long getDateCreated();

	void setDateCreated(long dateCreated);

	long getDateModified();

	void setDateModified(long dateModified);

}
