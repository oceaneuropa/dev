package org.orbit.infra.model.subs;

import java.util.Date;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface SubsSource {

	Integer getId();

	void setId(Integer id);

	String getName();

	void setName(String name);

	String getType();

	void setType(String type);

	String getTypeId();

	void setTypeId(String typeId);

	Map<String, String> getProperties();

	Date getCreatedTime();

	void setCreatedTime(Date createdTime);

	Date getModifiedTime();

	void setModifiedTime(Date createdTime);

}
