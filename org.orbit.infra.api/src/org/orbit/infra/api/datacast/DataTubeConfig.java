package org.orbit.infra.api.datacast;

import java.util.Map;

public interface DataTubeConfig {

	DataCastClient getDataCastClient();

	String getId();

	void setId(String id);

	String getDataCastId();

	void setDataCastId(String dataCastId);

	String getDataTubeId();

	void setDataTubeId(String dataTubeId);

	String getName();

	void setName(String name);

	boolean isEnabled();

	void setEnabled(boolean enabled);

	Map<String, Object> getProperties();

	long getDateCreated();

	void setDateCreated(long dateCreated);

	long getDateModified();

	void setDateModified(long dateModified);

}