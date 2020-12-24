package org.orbit.infra.api.subscription;

public interface SubsType {

	Integer getId();

	void setId(Integer id);

	String getType();

	void setType(String type);

	String getName();

	void setName(String name);

	long getDateCreated();

	void setDateCreated(long dateCreated);

	long getDateModified();

	void setDateModified(long dateModified);

}
