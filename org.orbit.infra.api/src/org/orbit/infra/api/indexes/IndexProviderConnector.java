package org.orbit.infra.api.indexes;

import java.util.Map;

public interface IndexProviderConnector {

	IndexProvider getService(Map<Object, Object> properties);

}
