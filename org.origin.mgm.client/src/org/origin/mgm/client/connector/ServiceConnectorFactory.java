package org.origin.mgm.client.connector;

import java.util.Map;

public interface ServiceConnectorFactory<Connector> {

	/**
	 * 
	 * @param properties
	 * @return
	 */
	Connector create(Map<Object, Object> properties);

}
