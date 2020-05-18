package org.orbit.infra.api.indexes;

import java.util.Date;
import java.util.Map;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.model.TransientPropertyAware;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface IndexItem extends TransientPropertyAware, IAdaptable {

	Integer getIndexItemId();

	String getIndexProviderId();

	String getType();

	String getName();

	Map<String, Object> getProperties();

	Date getDateCreated();

	Date getDateModified();

}
