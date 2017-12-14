package org.origin.common.loadbalance.other;

import java.util.Map;

public interface ServiceConnectorFactory<Connector> {

	/**
	 * 
	 * @param properties
	 * @return
	 */
	Connector create(Map<Object, Object> properties);

}
