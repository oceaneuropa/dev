package org.orbit.infra.api.indexes;

import java.util.Date;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.model.TransientPropertyAware;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface IndexProviderItem extends TransientPropertyAware, IAdaptable {

	String getId();

	String getName();

	String getDescription();

	Date getDateCreated();

	Date getDateModified();

}
