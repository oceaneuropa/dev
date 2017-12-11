package org.orbit.infra.api.indexes;

import java.util.Map;

public interface IndexServiceConnector {

	IndexService getService(Map<Object, Object> properties);

}
