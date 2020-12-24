package org.orbit.infra.model.subs;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
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
